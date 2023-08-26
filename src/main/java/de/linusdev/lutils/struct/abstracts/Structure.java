package de.linusdev.lutils.struct.abstracts;

import de.linusdev.lutils.struct.info.StructureInfo;
import de.linusdev.lutils.struct.utils.BufferUtils;
import de.linusdev.lutils.struct.utils.Utils;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public abstract class Structure implements NativeParsable {

    protected Structure mostParentStructure;
    protected ByteBuffer byteBuf;
    protected int offset;
    protected volatile boolean modified;

    /**
     * Set this {@link Structure} to be a child of {@code mostParentStructure}.
     * @param mostParentStructure most parental structure
     * @param offset start of this structure
     */
    protected void useBuffer(@NotNull Structure mostParentStructure, int offset) {
        this.mostParentStructure = mostParentStructure;
        this.offset = offset;
        this.byteBuf = offset == 0 ?
                mostParentStructure.getByteBuffer().order(ByteOrder.nativeOrder()) :
                BufferUtils.slice(mostParentStructure.getByteBuffer(), offset, getRequiredSize()).order(ByteOrder.nativeOrder());
    }

    /**
     * Call {@link #useBuffer(Structure, int)} of {@code structure}
     */
    protected void callUseBufferOf(@NotNull Structure structure, @NotNull Structure mostParentStructure, int offset) {
        structure.useBuffer(mostParentStructure, offset);
    }

    /**
     * Will set the {@link ByteOrder} to native order.
     * The most parental structure will be {@code this}.
     * @param buffer {@link ByteBuffer} to claim
     */
    protected void claimBuffer(@NotNull ByteBuffer buffer) {
        this.mostParentStructure = this;
        this.offset = 0;
        this.byteBuf = buffer.order(ByteOrder.nativeOrder());
    }

    /**
     * Creates an 8 byte aligned direct byte buffer and calls {@link #useBuffer(Structure, int)}.
     */
    public void allocate() {
        claimBuffer(BufferUtils.createAligned(getRequiredSize(), 8));
        useBuffer(this, 0);
    }

    public abstract @NotNull StructureInfo getInfo();

    @Override
    public int getAlignment() {
        return getInfo().getAlignment();
    }

    @Override
    public int getRequiredSize() {
        return getInfo().getRequiredSize();
    }

    /**
     * Offset in the byte buffer of the {@link #mostParentStructure most parental structure}.
     * @return offset in bytes
     */
    public int getOffset() {
        return offset;
    }

    @Override
    public ByteBuffer getByteBuffer() {
        return byteBuf;
    }

    @Override
    public boolean isInitialised() {
        return byteBuf != null;
    }

    /**
     *
     * @param name name of this struct
     * @param content content to be inside the {@code {content}}
     * @return A nice string describing this structure
     */
    protected String toString(@NotNull String name, @Nullable String content) {
        return String.format("%s (alignment=%d, size=%d) %s",
                name,
                getAlignment(),
                getRequiredSize(),
                content == null ? "" : ("{\n" + Utils.indent(content, "    ") + "\n}")
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
    public void modified(int offset, int size) {
        mostParentStructure.onModification(offset, size);
    }

    /**
     * Mark the whole structure as modified.
     */
    public void modified() {
        modified(offset, getRequiredSize());
    }

    /**
     * Called on the most parental structure if on it or any of its children {@link #modified(int, int)} is called.
     * @param offset modified region start
     * @param size modified region size
     */
    @ApiStatus.OverrideOnly
    protected void onModification(int offset, int size) {
        modified = true;
    }

    /**
     * Will be {@code true} after a call to {@link #modified(int, int)}. Will be reset by {@link #unmodified()}.
     * Note that modifications are only tracked on the most parental structure. The return value of this method is undefined,
     * if it is not the most parental structure.
     * @return whether this {@link Structure} has been modified.
     */
    public boolean isModified() {
        return modified;
    }

    /**
     * Marks this structure has not modified. May only be called on the most parental structure.
     */
    @ApiStatus.Internal
    public void unmodified() {
        modified = false;
    }

    /**
     * Will never be {@code null} after either {@link #claimBuffer(ByteBuffer)} or {@link #useBuffer(Structure, int)}
     * has been called.
     * @return the most parental structure.
     */
    @ApiStatus.Internal
    public Structure getMostParentStructure() {
        return mostParentStructure;
    }

}
