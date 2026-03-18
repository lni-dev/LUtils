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

package de.linusdev.lutils.nat.memory.buffer;

import de.linusdev.lutils.nat.memory.NativeMemBuffer;
import de.linusdev.lutils.nat.struct.utils.BufferUtils;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class DBBNativeMemBuffer implements NativeMemBuffer {

    public final @NotNull ByteBuffer byteBuffer;
    public final long address;

    public DBBNativeMemBuffer(@NotNull ByteBuffer byteBuffer) {
        this.byteBuffer = byteBuffer;
        this.address = BufferUtils.getHeapAddress(byteBuffer);
    }

    @Override
    public long address() {
        return address;
    }

    @Override
    public long size() {
        return byteBuffer.limit();
    }

    @Override
    public byte getByte(long index) {
        return byteBuffer.get(Math.toIntExact(index));
    }

    @Override
    public void setByte(long index, byte value) {
        byteBuffer.put(Math.toIntExact(index), value);
    }

    @Override
    public short getShort(long index) {
        return byteBuffer.getShort(Math.toIntExact(index));
    }

    @Override
    public void setShort(long index, short value) {
        byteBuffer.putShort(Math.toIntExact(index), value);
    }

    @Override
    public void setChar(long index, char value) {
        byteBuffer.putChar(Math.toIntExact(index), value);
    }

    @Override
    public char getChar(long index) {
        return byteBuffer.getChar(Math.toIntExact(index));
    }

    @Override
    public int getInt(long index) {
        return byteBuffer.getInt(Math.toIntExact(index));
    }

    @Override
    public void setInt(long index, int value) {
        byteBuffer.putInt(Math.toIntExact(index), value);
    }

    @Override
    public long getLong(long index) {
        return byteBuffer.getLong(Math.toIntExact(index));
    }

    @Override
    public void setLong(long index, long value) {
        byteBuffer.putLong(Math.toIntExact(index), value);
    }

    @Override
    public float getFloat(long index) {
        return byteBuffer.getFloat(Math.toIntExact(index));
    }

    @Override
    public void setFloat(long index, float value) {
        byteBuffer.putFloat(Math.toIntExact(index), value);
    }

    @Override
    public double getDouble(long index) {
        return byteBuffer.getDouble(Math.toIntExact(index));
    }

    @Override
    public void setDouble(long index, double value) {
        byteBuffer.putDouble(Math.toIntExact(index), value);
    }

    @Override
    public void fill(long index, long size, byte value) {
        BufferUtils.fill(byteBuffer.slice(Math.toIntExact(index), Math.toIntExact(size)), value);
    }

    @Override
    public void fill(long index, byte[] values, long srcOffset, long srcLength) {
        byteBuffer.put(Math.toIntExact(index), values, Math.toIntExact(srcOffset), Math.toIntExact(srcLength));
    }

    @Override
    public @NotNull ByteOrder byteOrder() {
        return byteBuffer.order();
    }
}
