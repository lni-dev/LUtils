/*
 * Copyright (c) 2026 Linus Andera
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

package de.linusdev.lutils.nat.memory;

import de.linusdev.lutils.nat.memory.buffer.UnsafeNativeMemBuffer;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteOrder;

public interface NativeMemBuffer {

    static @NotNull NativeMemBuffer of(long address, long size, @NotNull ByteOrder byteOrder) {
        return new UnsafeNativeMemBuffer(address, size, byteOrder);
    }

    long address();

    long size();

    byte getByte(long index);
    void setByte(long index, byte value);

    short getShort(long index);
    void setShort(long index, short value);

    void setChar(long index, char value);
    char getChar(long index);

    int getInt(long index);
    void setInt(long index, int value);

    long getLong(long index);
    void setLong(long index, long value);

    float getFloat(long index);
    void setFloat(long index, float value);

    double getDouble(long index);
    void setDouble(long index, double value);

    void fill(long index, long size, byte value);

    default void fill(byte value) {
        fill(0, size(), value);
    }

    void fill(long index, byte[] values, long srcOffset, long srcLength);

    default void fill(byte[] values) {
        fill(0, values, 0, Math.min(values.length, size()));
    }

    @NotNull ByteOrder byteOrder();

}
