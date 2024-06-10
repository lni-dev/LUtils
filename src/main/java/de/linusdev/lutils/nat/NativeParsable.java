package de.linusdev.lutils.nat;

import de.linusdev.lutils.nat.struct.utils.BufferUtils;

import java.nio.ByteBuffer;

public interface NativeParsable extends MemorySizeable {

    /**
     * Whether this {@link NativeParsable} is already backed by a {@link #getByteBuffer() buffer}.
     * @return {@code true} if initialised
     */
    boolean isInitialised();

    /**
     * The {@link ByteBuffer} containing the native data. May not be {@code null} if
     * {@link #isInitialised()} is {@code true}.
     * @return {@link ByteBuffer}
     */
    ByteBuffer getByteBuffer();

    /**
     * Get the pointer to the buffer of this NativeParsable as long.
     * @return pointer to {@link #getByteBuffer()}
     */
    default long getPointer() {
        ByteBuffer buffer = getByteBuffer();
        return buffer == null ? 0L : BufferUtils.getHeapAddress(buffer);
    }
}
