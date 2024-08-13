/*
 * Copyright (c) 2023 Linus Andera
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

package de.linusdev.lutils.nat.struct.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

/**
 * Util class for creating, slicing and aligning {@link Buffer}
 */
public class BufferUtils {

    private static final long BUFFER_VARIABLE_ADDRESS_OFFSET;
    static {
        try {
            //noinspection deprecation: It may be deprecated, but we cannot use var handles, as the module is not an open module.
            BUFFER_VARIABLE_ADDRESS_OFFSET = Utils.UNSAFE.objectFieldOffset(Buffer.class.getDeclaredField("address"));
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * creates an aligned direct {@link ByteBuffer}
     * @param capacity the capacity of the buffer
     * @param alignment the alignment of the buffer address
     * @return an aligned buffer with given capacity
     */
    public static @NotNull ByteBuffer createAligned(int capacity, long alignment) {
        ByteBuffer buffer = ByteBuffer.allocateDirect((int) (capacity + alignment - 1));
        long address = getHeapAddress(buffer);

        if((address % (alignment)) == 0) {
            buffer.position(0);
            buffer.limit(capacity);
            return buffer.slice().order(ByteOrder.nativeOrder());
        }

        buffer.position((int) (alignment - (address % (alignment))));
        buffer.limit(buffer.position() + capacity);
        //System.out.printf("Buffer alignment (%d) fixed. Was %d. Moved by %d.\n", alignment, address, (alignment - (address % (alignment))));
        return buffer.slice().order(ByteOrder.nativeOrder());
    }

    /**
     * creates an 64 bit (8-byte) aligned direct {@link ByteBuffer}.
     * Useful on 64 bit host systems.
     * @param capacity the capacity of the buffer
     * @return an 8-byte aligned buffer with given capacity
     */
    public static @NotNull ByteBuffer create64BitAligned(int capacity) {
        return createAligned(capacity, 8);
    }

    /**
     * @param buffer {@link ByteBuffer} to get the address of
     * @return the address of given {@code buffer}
     */
    public static long getHeapAddress(@NotNull ByteBuffer buffer) {
        return Utils.UNSAFE.getLong(buffer, BUFFER_VARIABLE_ADDRESS_OFFSET);
    }

    /**
     * The string must fill the whole {@link ByteBuffer}.
     * @param str {@link ByteBuffer} containing the string
     * @param endsWithZero {@code true} if the string ends with a 0 character (this character will then be removed.)
     * @return {@link String}
     */
    public static @NotNull String readString(@NotNull ByteBuffer str, boolean endsWithZero) {
        if (endsWithZero) {
            str.limit(str.limit() - 1);
            str = str.slice();
        }
        return StandardCharsets.UTF_8.decode(str).toString();
    }

    /**
     * Works the same as the java 13 {@link Buffer#slice(int, int)} method.
     * @param buffer {@link ByteBuffer} to slice
     * @param start start pos
     * @param length index
     * @return slice of given buffer
     */
    public static @NotNull ByteBuffer slice(@NotNull ByteBuffer buffer, int start, int length) {
        int op = buffer.position();
        int ol = buffer.limit();
        buffer.position(start);
        buffer.limit(start + length);

        ByteBuffer slice = buffer.slice();

        buffer.position(op);
        buffer.limit(ol);

        return slice;
    }

    @FunctionalInterface
    public interface ByteBufferFromPointerMethod {
         @NotNull ByteBuffer getByteBufferFromPointer(long pointer, int capacity);
    }

    private static @Nullable ByteBufferFromPointerMethod byteBufferFromPointerMethod = null;

    public static void setByteBufferFromPointerMethod(@NotNull ByteBufferFromPointerMethod method) {
        byteBufferFromPointerMethod = method;
    }

    public static @NotNull ByteBuffer getByteBufferFromPointer(long pointer, int capacity) {
        if(byteBufferFromPointerMethod == null) {
            throw new IllegalStateException("BufferUtils.byteBufferFromPointerMethod must be set," +
                    " before calling this method. Please call " +
                    "BufferUtils.setByteBufferFromPointerMethod() with an appropriate method.");
        }

        return byteBufferFromPointerMethod.getByteBufferFromPointer(pointer, capacity);
    }

}
