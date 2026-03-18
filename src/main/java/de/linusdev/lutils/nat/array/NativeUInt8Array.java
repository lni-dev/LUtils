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
import de.linusdev.lutils.nat.struct.abstracts.StructureStaticVariables;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

/**
 * Same as {@link NativeInt8Array}, but with a different name (To differentiate between int8 and unsigned int8)
 */
public class NativeUInt8Array extends NativeInt8Array {

    @SuppressWarnings("unused")
    public static PrimitiveArrayStaticGenerator GENERATOR = new PrimitiveArrayStaticGenerator(NativeType.INT8);

    /**
     * @see StructureStaticVariables#newUnallocated()
     */
    public static NativeUInt8Array newUnallocated() {
        return new NativeUInt8Array(null);
    }

    /**
     * @see StructureStaticVariables#newAllocatable(ABI, int[], Class[]) 
     */
    public static NativeUInt8Array newAllocatable(@NotNull ABI abi, int length) {
        return new NativeUInt8Array(abi, length);
    }

    protected NativeUInt8Array(@Nullable ABI abi, int length) {
        super(abi, length);
    }

    protected NativeUInt8Array(@Nullable ABI abi) {
        super(abi);
    }

    public byte getUInt8(@Range(from = 0, to = Integer.MAX_VALUE) int index) {
        return nativeMem.getByte(positions.position(index));
    }

    public void setUInt8(int index, byte item) {
        nativeMem.setByte(positions.position(index), item);
    }

}
