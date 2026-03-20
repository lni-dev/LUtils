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

package de.linusdev.lutils.nat.array;

import de.linusdev.lutils.nat.NativeType;
import de.linusdev.lutils.nat.abi.ABI;
import de.linusdev.lutils.nat.memory.IndexOutOfStructRange;
import de.linusdev.lutils.nat.struct.abstracts.StructureStaticVariables;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

public class NativeInt8Array extends NativePrimitiveTypeArray<Byte> {

    @SuppressWarnings("unused")
    public static PrimitiveArrayStaticGenerator GENERATOR = new PrimitiveArrayStaticGenerator(NativeType.INT8);

    /**
     * @see StructureStaticVariables#newUnallocated()
     */
    public static NativeInt8Array newUnallocated() {
        return new NativeInt8Array();
    }

    /**
     * @see StructureStaticVariables#newAllocatable(ABI, int[], Class[]) 
     */
    public static NativeInt8Array newAllocatable(@Nullable ABI abi, int length) {
        return new NativeInt8Array(abi, length);
    }

    protected NativeInt8Array(@Nullable ABI abi, int length) {
        super(GENERATOR, abi, length);
    }

    protected NativeInt8Array() {
        super(GENERATOR);
    }

    @Override
    public Byte get(@Range(from = 0, to = Integer.MAX_VALUE) int index) {
        return getInt8(index);
    }

    public byte getInt8(@Range(from = 0, to = Integer.MAX_VALUE) int index) {
        return nativeMem.getByte(posInBuf(index));
    }

    @Override
    public void set(int index, Byte item) {
        setInt8(index, item);
    }

    public void setInt8(int index, byte item) {
        nativeMem.setByte(posInBuf(index), item);
    }

    public void set(byte @NotNull [] values) {
        if(values.length  > length())
            throw new IndexOutOfStructRange(values.length - 1, this);

        if(getInfo().getStride() == Byte.BYTES) {
            nativeMem.fill(posInBuf(0), values, 0, values.length);
        } else {
            // If for some reason the stride of a byte array is not 1, we have to copy each element individually.
            for (int i = 0; i < length(); i++) {
                nativeMem.setByte(posInBuf(i), values[i]);
            }
        }
    }

    public void get(byte @NotNull [] values) {
        if(getInfo().getStride() == Byte.BYTES) {
            nativeMem.getBytes(posInBuf(0), values, 0L, Math.min(length(), values.length));
        } else {
            // If for some reason the stride of a byte array is not 1, we have to copy each element individually.
            for (int i = 0; i < length(); i++) {
                values[i] = nativeMem.getByte(posInBuf(i));
            }
        }
    }

}
