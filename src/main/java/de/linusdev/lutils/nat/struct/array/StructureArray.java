/*
 * Copyright (c) 2023-2026 Linus Andera
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

package de.linusdev.lutils.nat.struct.array;

import de.linusdev.lutils.nat.abi.ABI;
import de.linusdev.lutils.nat.array.NativeArray;
import de.linusdev.lutils.nat.memory.NativeMemAllocator;
import de.linusdev.lutils.nat.memory.NativeMemBuffer;
import de.linusdev.lutils.nat.struct.UStructSupplier;
import de.linusdev.lutils.nat.struct.abstracts.Structure;
import de.linusdev.lutils.nat.struct.abstracts.StructureStaticVariables;
import de.linusdev.lutils.nat.struct.annos.RequirementType;
import de.linusdev.lutils.nat.struct.generator.Language;
import de.linusdev.lutils.nat.struct.generator.SimpleStaticGenerator;
import de.linusdev.lutils.nat.struct.generator.StaticGenerator;
import de.linusdev.lutils.nat.struct.generator.StructCodeGenerator;
import de.linusdev.lutils.nat.struct.info.ArrayInfo;
import de.linusdev.lutils.nat.struct.info.StructureInfo;
import de.linusdev.lutils.nat.struct.mod.ModTrackingStructure;
import de.linusdev.lutils.nat.struct.utils.SSMUtils;
import de.linusdev.lutils.other.str.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.Iterator;

public class StructureArray<T extends Structure> extends ModTrackingStructure implements NativeArray<T> {

    public static final @NotNull StaticGenerator GENERATOR = new SimpleStaticGenerator(
            RequirementType.REQUIRED, RequirementType.REQUIRED
    ) {

        @Override
        public @NotNull StructureInfo calculateInfoChecked(
                @NotNull Class<?> selfClazz,
                @NotNull ABI abi,
                int[] length,
                @NotNull Class<?>[] elementTypes
        ) {

            StructureInfo elementInfo = SSMUtils.getInfo(
                    null,
                    elementTypes[0],
                    abi,
                    length.length > 1 ? Arrays.copyOfRange(length, 1, length.length) : null,
                    elementTypes.length > 1 ? Arrays.copyOfRange(elementTypes, 1, elementTypes.length) : null
            );

            ArrayInfo info = abi.calculateArrayLayout(
                    false,
                    elementInfo,
                    length[0],
                    -1
            );

            return new StructureArrayInfo(
                    abi,
                    info.getAlignment(),
                    info.isCompressed(),
                    info.getRequiredSize(),
                    info.getSizes(),
                    info.getLength(),
                    info.getStride(),
                    info.getPositions(),
                    elementTypes[0],
                    elementInfo
            );
        }

        @Override
        public @NotNull StructCodeGenerator codeGenerator() {
            return new StructCodeGenerator() {
                @Override
                public @NotNull String getStructTypeName(@NotNull Language language, @NotNull Class<?> selfClazz, @NotNull StructureInfo info) {
                    StructureArrayInfo arrayInfo = (StructureArrayInfo) info;
                    StructureInfo elementInfo = arrayInfo.elementInfo;
                    StaticGenerator elementGenerator = SSMUtils.getGenerator(arrayInfo.elementClass);
                    //noinspection DataFlowIssue
                    return elementGenerator.codeGenerator().getStructTypeName(language, arrayInfo.elementClass, elementInfo) + "[]";
                }

                @Override
                public @NotNull String getStructVarDef(@NotNull Language language, @NotNull Class<?> selfClazz, @NotNull StructureInfo info, @NotNull String varName) {
                    StructureArrayInfo arrayInfo = (StructureArrayInfo) info;
                    StructureInfo elementInfo = arrayInfo.elementInfo;
                    StaticGenerator elementGenerator = SSMUtils.getGenerator(arrayInfo.elementClass);

                    //noinspection DataFlowIssue
                    return elementGenerator.codeGenerator().getStructTypeName(language, arrayInfo.elementClass, elementInfo) + " " + varName
                            + "[" + arrayInfo.getLength() + "]" + language.lineEnding;
                }
            };
        }
    };

    /**
     * Creates an unallocated {@link StructureArray}, whose {@link #info} must be supplied when {@link #useBuffer(Structure, long, StructureInfo)}
     * is called. A call to {@link NativeMemAllocator#allocate(Structure) allocate} is not supported.
     * @param trackModifications see {@link #trackModifications}
     * @param creator see {@link #creator}
     * @return unallocated {@link StructureArray} as described above.
     * @param <T> element type
     * @see StructureStaticVariables#newUnallocated()
     */
    @SuppressWarnings("JavadocReference")
    public static <T extends Structure> @NotNull StructureArray<T> newUnallocated(
            boolean trackModifications,
            @NotNull UStructSupplier<T> creator
    ) {
        return new StructureArray<>(trackModifications, creator);
    }

    /**
     * Creates an allocatable {@link StructureArray}.
     * It can be allocated using {@link NativeMemAllocator#allocate(Structure) allocate} or {@link #claimMemory(NativeMemBuffer, long)}
     * @param trackModifications see {@link #trackModifications}
     * @param abi the {@link ABI} to use for this structure
     * @param length the length of the array
     * @param elementType the element type of the array
     * @param creator  see {@link #creator}
     * @return allocatable {@link StructureArray} as described above.
     * @param <T> element type
     * @see StructureStaticVariables#newAllocatable(ABI, int[], Class[])  
     */
    public static <T extends Structure> @NotNull StructureArray<T> newAllocatable(
            boolean trackModifications,
            @Nullable ABI abi,
            int length,
            @NotNull Class<? extends Structure> elementType,
            @NotNull UStructSupplier<T> creator
    ) {
        return new StructureArray<>(trackModifications, abi, new int[]{length}, new Class<?>[]{elementType}, creator);
    }

    /**
     * Creates an allocatable {@link StructureArray}.
     * It can be allocated using {@link NativeMemAllocator#allocate(Structure)} or {@link #claimMemory(NativeMemBuffer, long)}
     * @param trackModifications see {@link #trackModifications}
     * @param abi the {@link ABI} to use for this structure
     * @param length Array containing length information. First index must be the length of this array. The following indexes are passed
     *               to the elements of this array when creating their {@link StructureInfo}.
     * @param elementType Array containing element type information. First index must be the element type of this array. The following indexes are passed
     *                    to the elements of this array when creating their {@link StructureInfo}.
     * @param creator  see {@link #creator}
     * @return allocatable {@link StructureArray} as described above.
     * @param <T> element type
     * @see StructureStaticVariables#newAllocatable(ABI, int[], Class[])
     */
    public static <T extends Structure> @NotNull StructureArray<T> newAllocatable(
            boolean trackModifications,
            @Nullable ABI abi,
            int @NotNull [] length,
            @NotNull Class<?> @NotNull [] elementType,
            @NotNull UStructSupplier<T> creator
    ) {
        return new StructureArray<>(trackModifications, abi, length, elementType, creator);
    }

    /**
     * @param trackModifications see {@link #trackModifications}
     * @param elementType clazz of array elements
     * @param length array length
     * @param pointer pointer to the native array
     * @param creator see {@link #creator}
     * @return {@link StructureArray} backed by given pointer
     * @param <T> array element type
     */
    public static <T extends Structure> @NotNull StructureArray<T> ofPointer(
            boolean trackModifications,
            @Nullable ABI abi,
            @NotNull Class<? extends Structure> elementType,
            int length,
            long pointer,
            @NotNull UStructSupplier<T> creator
    ) {
        StructureArray<T> sArray = StructureArray.newAllocatable(trackModifications, abi, length, elementType, creator);
        sArray.claimMemory(NativeMemBuffer.of(pointer, sArray.getRequiredSize(), ByteOrder.nativeOrder()), 0);
        return sArray;
    }

    private final @NotNull UStructSupplier<T> creator;
    private ArrayInfo.ArrayPositionFunction positions;

    private StructureInfo elementInfo;
    private Structure @Nullable [] items;
    private int size;

    /**
     * @see #newUnallocated(boolean, UStructSupplier)
     */
    protected StructureArray(boolean trackModifications, @NotNull UStructSupplier<T> creator) {
        super(null, trackModifications);
        this.creator = creator;
    }

    /**
     * Creates an allocatable struct-array
     * @see #newAllocatable(boolean, ABI, int[], Class[], UStructSupplier)
     * @see #newAllocatable(boolean, ABI, int, Class, UStructSupplier) 
     */
    protected StructureArray(
            boolean trackModifications,
            @Nullable ABI abi,
            int @NotNull [] length,
            @NotNull Class<?> @NotNull [] elementType,
            @NotNull UStructSupplier<T> creator
    ) {
        super(abi, trackModifications);
        setInfo(GENERATOR.calculateInfo(this.getClass(), abi, length, elementType));
        this.elementInfo = getInfo().elementInfo;
        this.size = getInfo().getLength();
        this.creator = creator;
    }

    /**
     * Start Caching the {@link Structure} instances of elements inside {@link #items}.
     */
    public void enableCaching() {
        this.items = new Structure[size];
    }

    /**
     * Set given {@code struct} to the native memory represented by given {@code index}. If {@link #enableCaching() caching}
     * is enabled, {@code struct} will be stored in {@link #items} for future retrieves using {@link #get(int)} or {@link #getOrNull(int)}.
     * @param index index to set.  Must be greater than 0 and smaller then {@link #length()}.
     * @param struct to set at index. Must unallocated!
     */
    public void set(int index, @NotNull T struct) {
        if(items != null)
            items[index] = struct;
        callUseBufferOf(
                struct,
                this.mostParentStructure,
                this.offset + positions.position(index),
                elementInfo
        );
    }

    @Override
    protected void useBuffer(
            @NotNull Structure mostParentStructure,
            long offset,
            @NotNull StructureInfo info
    ) {
        super.useBuffer(mostParentStructure, offset, info);
        StructureArrayInfo aInfo = getInfo();

        this.elementInfo = aInfo.elementInfo;
        this.size = aInfo.getLength();
    }

    @Override
    protected @Nullable StaticGenerator getGenerator() {
        return GENERATOR;
    }

    @Override
    public @NotNull StructureArrayInfo getInfo() {
        return (StructureArrayInfo) super.getInfo();
    }

    @Override
    protected void onSetInfo(@NotNull StructureInfo info) {
        super.onSetInfo(info);

        positions = ((ArrayInfo) info).getPositions();
    }

    @Override
    public int length() {
        return size;
    }

    /**
     * If {@link #enableCaching() caching} is disabled a new {@link T} instance will be returned, representing given index.
     * <br><br>
     * If {@link #enableCaching() caching} is enabled this method is similar to {@link #getOrNull(int)}, but if the item at {@code index} is still {@code null},
     * a new item will be {@link UStructSupplier#supply() created}.
     * @see #getOrNull(int)
     */
    @Override
    @SuppressWarnings("unchecked")
    public @NotNull T get(int index) {
        T item;
        if(items == null || items[index] == null) {
            item = creator.supply();
            callUseBufferOf(
                    item,
                    this.mostParentStructure,
                    this.offset + positions.position(index),
                    elementInfo
            );

            if(items != null)
                items[index] = item;

        } else {
            item = (T) items[index];
        }

        return item;
    }

    /**
     * Set the native memory used by given {@code struct} to the memory represented by given {@code index}.
     */
    public @NotNull T get(int index, @NotNull T struct) {
        callUseBufferOf(struct, this.mostParentStructure, this.offset + positions.position(index), elementInfo);
        return struct;
    }

    /**
     * Returns the item at given {@code index} or {@code null} if no item has been {@link #set(int, Structure)}
     * or {@link #get(int)} at given {@code index} before. Only works if {@link #enableCaching() caching} is enabled.
     */
    @SuppressWarnings("unchecked")
    public T getOrNull(int index) {
        if(items == null)
            throw new IllegalStateException("Caching must be enabled for this method.");
        return (T) items[index];
    }

    /**
     * Create a view on this structure array.
     * @param startIndex index the view starts
     * @param length length of the view
     * @return {@link NativeArrayView}
     */
    public @NotNull NativeArrayView<T> getView(int startIndex, int length) {
        long byteIndex = positions.position(startIndex);
        long byteLength = positions.position(startIndex + length) - byteIndex;
        return new NativeArrayView<>(this, nativeMem, startIndex, length, offset + byteIndex, byteLength);
    }

    @Override
    public @NotNull Iterator<T> iterator() {
        return new Iterator<>() {
            int index = 0;

            @Override
            public boolean hasNext() {
                return index < length();
            }

            @Override
            public T next() {
                return get(index++);
            }
        };
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("length=").append(length()).append("\n");
        sb.append("stride=").append(getInfo().getStride()).append("\n");
        sb.append("items={\n");

        for (int i = 0; i < size; i++) {
            Structure item = items == null ? null : items[i];
            sb.append(StringUtils.indent(i + " (offsetStart=" + (offset + positions.position(i)) + "): " + item, "    ", true)).append(",\n");
        }

        sb.append("}");

        return toString("StructureArray<" + getInfo().elementClass.getSimpleName() + ">", sb.toString());
    }



}
