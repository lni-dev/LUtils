/*
 * Copyright (c) 2024-2026 Linus Andera
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.linusdev.lutils.nat.struct.abstracts;

import de.linusdev.lutils.nat.NativeParsable;
import de.linusdev.lutils.nat.abi.ABI;
import de.linusdev.lutils.nat.memory.NMemInfo;
import de.linusdev.lutils.nat.memory.NativeMemBuffer;
import de.linusdev.lutils.nat.struct.generator.Language;
import de.linusdev.lutils.nat.struct.generator.StaticGenerator;
import de.linusdev.lutils.nat.struct.info.StructureInfo;
import de.linusdev.lutils.nat.struct.utils.SSMUtils;
import de.linusdev.lutils.other.str.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Class to support native structs.
 * <br><br>
 * Classes extending {@link Structure} must comply to some rules, in order for them to work. The
 * rules are described in {@link StructureStaticVariables}.
 * <br><br>
 * Many classes extending {@link Structure} follow another pattern. They provide three static methods to
 * create an instance of the structure:
 * <ul>
 *     <li>{@link StructureStaticVariables#newUnallocated() newUnallocated()}</li>
 *     <li>{@link StructureStaticVariables#newAllocatable(ABI, int[], Class[])  newAllocatable(StructValue)}</li>
 * </ul>
 **/
public abstract class Structure implements NativeParsable {

    /**
     * Creates a union with given structure {@code actual}.
     * The structure {@code view} will start at the same location in {@code actual}'s byte buffer.
     * Thus changes to {@code view} will also change {@code actual} and wise versa.
     * Modifications to {@code view} will also mark the most parental structure of {@code actual} as modified
     * if track modifications is enabled.
     * @param view {@link S} that can be allocated. Must not already be allocated.
     * @param actual allocated {@link Structure}.
     * @return {@code view} now backed by the {@link #getNativeMemBuffer() buffer} of {@code actual}
     * @param <S> your struct type (required for return type only)
     */
    public static <S extends Structure>  @NotNull S unionWith(@NotNull S view, @NotNull Structure actual) {
        view.useBuffer(actual.getMostParentStructure(), actual.getOffset(), view.getInfoOrFail());
        return view;
    }

    /**
     * Generate struct code for given {@code structClass}
     * @param language {@link Language} to generate the struct code in
     * @param structClass Class of the structure to generate code for
     * @return struct code
     * @throws IllegalArgumentException if given class cannot generate struct code.
     */
    @SuppressWarnings("DeprecatedIsStillUsed")
    @Deprecated
    public static @NotNull String generateStructCode(
            @NotNull Language language,
            @NotNull Class<? extends Structure> structClass
    ) {
        StaticGenerator generator = SSMUtils.getGenerator(structClass);
        StructureInfo info = SSMUtils.getInfo(generator, structClass, null, null,  null);

        //noinspection DataFlowIssue
        String code = generator.codeGenerator().generateStructCode(language, structClass, info);

        if(code == null)
            throw new IllegalArgumentException("Structure '" + structClass.getCanonicalName() + "' cannot generate struct code.");

        return code;
    }

    protected Structure mostParentStructure;
    /**
     * @see #getInfo()
     * @see #setInfo(StructureInfo)
     * @see #onSetInfo(StructureInfo)
     */
    private StructureInfo info;
    protected NativeMemBuffer nativeMem;
    protected long offset;
    protected volatile boolean modified;
    protected ABI abi;

    public Structure(@Nullable ABI abi) {
        this.abi = abi;
    }

    /**
     * Set this {@link Structure} to be a child of {@code mostParentStructure}.
     * @param mostParentStructure most parental structure
     * @param offset start of this structure
     * @param info the info for this structure, which was previously acquired.
     */
    protected void useBuffer(
            @NotNull Structure mostParentStructure,
            long offset,
            @NotNull StructureInfo info
    ) {
        if(this.abi == null)
            this.abi = info.getAbi();
        else if(!this.abi.equals(info.getAbi()))
            throw new IllegalStateException("ABI mismatch. ABI passed to useBuffer is '" + info.getAbi().identifier() + "', but ABI passed in constructor is '" + this.abi.identifier() + "'.");

        setInfo(info);

        this.mostParentStructure = mostParentStructure;
        this.offset = offset;
        this.nativeMem = mostParentStructure.nativeMem;
    }

    /**
     * Call {@link #useBuffer(Structure, long, StructureInfo) usebuffer(...)} of {@code structure}
     */
    protected void callUseBufferOf(
            @NotNull Structure structure,
            @NotNull Structure mostParentStructure,
            long offset,
            @NotNull StructureInfo info
    ) {
        structure.useBuffer(mostParentStructure, offset, info);
    }

    /**
     * The most parental structure will be {@code this}.
     * @param buffer {@link NativeMemBuffer} to claim.
     * @throws IllegalStateException if this structure cannot claim a buffer in its current state. For example,
     * because it was created with not enough information to create its {@link #info}.
     */
    public void claimMemory(@NotNull NativeMemBuffer buffer, long offset) {
        this.nativeMem = buffer;
        useBuffer(this, offset, getInfoOrFail());
    }

    /**
     * Check if this structure already has an info or if it can generate one. This method will throw an exception
     * if it is not possible to return an info.
     * @return {@link StructureInfo} of this structure or freshly generated
     * @throws IllegalStateException if this structure cannot claim a buffer in its current state. For example,
     * because it was created with not enough information to create its {@link #info}.
     */
    protected @NotNull StructureInfo getInfoOrFail() {
        StructureInfo rInfo = info;
        if(rInfo == null && (rInfo = generateInfo()) == null)
            throw new IllegalStateException("This structure is missing information to generate an info and no info is currently present.");

        return rInfo;
    }

    /**
     * If this {@link Structure} can safely generate its {@link StructureInfo}, this method should be overwritten
     * and return a generated {@link StructureInfo}.
     * @return {@link StructureInfo} for this structure or {@code null}
     */
    protected @Nullable StructureInfo generateInfo() {
        return null;
    }

    protected @NotNull StructureInfo generateInfo(@Nullable ABI abi, int @Nullable [] length, @NotNull Class<?> @Nullable [] elementTypes) {
        return SSMUtils.getInfo(getGenerator(), this.getClass(), abi, length, elementTypes);
    }

    /**
     * Get the {@link StructureStaticVariables#GENERATOR generator} if stored somewhere accessible quickly.
     * Otherwise {@code null}.
     */
    protected abstract @Nullable StaticGenerator getGenerator();

    /**
     * returns {@link StructureInfo} of this structure instance if available
     * @throws IllegalStateException if the info is not yet available.
     */
    public @NotNull StructureInfo getInfo() {
        if(info == null)
            throw new IllegalStateException("Info is not yet available. allocate or useBuffer must be called first.");
        return info;
    }

    /**
     * This will return the current {@link #info} of this structure. If that is {@code null},
     * it will try to {@link #generateInfo() generate} the info and store it inside this structure.
     * @return {@link #info}
     * @throws IllegalStateException see {@link #getInfoOrFail()}
     */
    public @NotNull StructureInfo getOrGenerateInfo() {
        setInfo(getInfoOrFail());
        return getInfo();
    }

    /**
     * Set the {@link #info} of this structure instance. Calls {@link #onSetInfo(StructureInfo)}.
     * @param info the {@link StructureInfo} this structure instance should use.
     */
    protected final void setInfo(@NotNull StructureInfo info) {
        assert info != null;
        if(this.info == info)
            return;
        this.info = info;
        onSetInfo(info);
    }

    /**
     * Called in {@link #setInfo(StructureInfo)} after {@link #info} has been set.
     * @param info the info which was set.
     */
    protected void onSetInfo(@NotNull StructureInfo info) {}

    @Override
    public int getAlignment() {
        return getInfo().getAlignment();
    }

    @Override
    public long getRequiredSize() {
        return getInfo().getRequiredSize();
    }

    /**
     * Offset in the byte buffer of the {@link #mostParentStructure most parental structure}.
     * @return offset in bytes
     */
    public long getOffset() {
        return offset;
    }

    @Override
    public NMemInfo getNativeMemBuffer() {
        return new NMemInfo(nativeMem, offset);
    }

    @Override
    public boolean isInitialised() {
        return nativeMem != null;
    }

    /**
     *
     * @param name name of this struct
     * @param content content to be inside the {@code {content}}
     * @return A nice string describing this structure
     */
    protected String toString(@NotNull String name, @Nullable String content) {
        return String.format("%s (alignment=%d, size=%d, offsetStart=%d, offsetEnd=%d) %s",
                name,
                getAlignment(),
                getRequiredSize(),
                getOffset(),
                getOffset() + getRequiredSize(),
                content == null ? "" : ("{\n" + StringUtils.indent(content, "    ", true) + "\n}")
        );
    }

    /*
     *
     * Modification Stuff
     *
     */

    /**
     * Mark {@code size} bytes at {@code offset} as modified.
     * @param offset region start
     * @param size region size
     */
    public void modified(long offset, long size) {
        mostParentStructure.onModification(offset, size);
    }

    /**
     * Mark the whole structure as modified.
     */
    public void modified() {
        modified(offset, getRequiredSize());
    }

    /**
     * Marks this structure has not modified. May only be called on the most parental structure.
     */
    public void unmodified() {
        modified = false;
    }

    /**
     * Will be {@code true} after a call to {@link #modified(long, long)}. Will be reset by {@link #unmodified()}.
     * Note that modifications are only tracked on the most parental structure. The return value of this method is undefined,
     * if it is not the most parental structure.
     * @return whether this {@link Structure} has been modified.
     */
    public boolean isModified() {
        return modified;
    }

    /**
     * Called on the most parental structure if on it or any of its children {@link #modified(long, long)} is called.
     * @param offset modified region start
     * @param size modified region size
     */
    protected void onModification(long offset, long size) {
        modified = true;
    }

    /**
     * Will never be {@code null} after either {@link #claimMemory(NativeMemBuffer, long)} or {@link #useBuffer(Structure, long, StructureInfo)}
     * has been called.
     * @return the most parental structure.
     */
    public Structure getMostParentStructure() {
        return mostParentStructure;
    }

}
