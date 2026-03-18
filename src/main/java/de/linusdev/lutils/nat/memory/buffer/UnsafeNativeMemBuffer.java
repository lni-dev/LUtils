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
import org.jetbrains.annotations.NotNull;

import java.nio.ByteOrder;

import static de.linusdev.lutils.nat.struct.utils.Utils.UNSAFE;

@SuppressWarnings("removal")
public class UnsafeNativeMemBuffer implements NativeMemBuffer {

    private final long address;
    private final long size;
    private final @NotNull ByteOrder byteOrder;

    public UnsafeNativeMemBuffer(long address, long size, @NotNull ByteOrder byteOrder) {
        this.address = address;
        this.size = size;
        this.byteOrder = byteOrder;
    }

    @Override
    public long address() {
        return address;
    }

    @Override
    public long size() {
        return size;
    }

    @Override
    public byte getByte(long index) {
        return UNSAFE.getByte(address + index);
    }

    @Override
    public void setByte(long index, byte value) {
        UNSAFE.putByte(address + index, value);
    }

    @Override
    public short getShort(long index) {
        return convEndian(UNSAFE.getShort(address + index));
    }

    @Override
    public void setShort(long index, short value) {
        UNSAFE.putShort(address + index, convEndian(value));
    }

    @Override
    public void setChar(long index, char value) {
        UNSAFE.putChar(address + index, convEndian(value));
    }

    @Override
    public char getChar(long index) {
        return convEndian(UNSAFE.getChar(address + index));
    }

    @Override
    public int getInt(long index) {
        return convEndian(UNSAFE.getInt(address + index));
    }

    @Override
    public void setInt(long index, int value) {
        UNSAFE.putInt(address + index, convEndian(value));
    }

    @Override
    public long getLong(long index) {
        return convEndian(UNSAFE.getLong(address + index));
    }

    @Override
    public void setLong(long index, long value) {
        UNSAFE.putLong(address + index, convEndian(value));
    }

    @Override
    public float getFloat(long index) {
        return Float.intBitsToFloat(convEndian(UNSAFE.getInt(address + index)));
    }

    @Override
    public void setFloat(long index, float value) {
        UNSAFE.putInt(address + index, convEndian(Float.floatToIntBits(value)));
    }

    @Override
    public double getDouble(long index) {
        return Double.longBitsToDouble(convEndian(UNSAFE.getLong(address + index)));
    }

    @Override
    public void setDouble(long index, double value) {
        UNSAFE.putLong(address + index, convEndian(Double.doubleToLongBits(value)));
    }

    @Override
    public void fill(long index, long size, byte value) {
        UNSAFE.setMemory(address + index, size, value);
    }

    private static final long BYTE_ARRAY_BASE_OFFSET = UNSAFE.arrayBaseOffset(byte[].class);

    @Override
    public void fill(long index, byte[] values, long srcOffset, long srcLength) {
        UNSAFE.copyMemory(values, BYTE_ARRAY_BASE_OFFSET + srcOffset, null, address, srcLength);
    }

    @Override
    public @NotNull ByteOrder byteOrder() {
        return byteOrder;
    }

    private static final @NotNull ByteOrder NATIVE_ORDER = ByteOrder.nativeOrder();

    private char convEndian(char n) {
        return byteOrder == NATIVE_ORDER ? n : Character.reverseBytes(n);
    }

    private short convEndian(short n) {
        return byteOrder == NATIVE_ORDER ? n : Short.reverseBytes(n);
    }

    private int convEndian(int n) {
        return byteOrder == NATIVE_ORDER ? n : Integer.reverseBytes(n);
    }

    private long convEndian(long n) {
        return byteOrder == NATIVE_ORDER ? n : Long.reverseBytes(n);
    }
}
