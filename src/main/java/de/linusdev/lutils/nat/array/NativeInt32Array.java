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
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

public class NativeInt32Array extends NativePrimitiveTypeArray<Integer> {

    @SuppressWarnings("unused")
    public static PrimitiveArrayStaticGenerator GENERATOR = new PrimitiveArrayStaticGenerator(NativeType.INT32);

    /**
     * @see StructureStaticVariables#newUnallocated()
     */
    public static NativeInt32Array newUnallocated() {
        return new NativeInt32Array();
    }

    /**
     * @see StructureStaticVariables#newAllocatable(ABI, int[], Class[]) 
     */
    public static NativeInt32Array newAllocatable(@Nullable ABI abi, int length) {
        return new NativeInt32Array(abi, length);
    }

    protected NativeInt32Array(@Nullable ABI abi, int length) {
        super(GENERATOR, abi, length);
    }

    protected NativeInt32Array() {
        super(GENERATOR);
    }

    @Override
    public Integer get(@Range(from = 0, to = Integer.MAX_VALUE) int index) {
        return getInt(index);
    }

    public int getInt(@Range(from = 0, to = Integer.MAX_VALUE) int index) {
        return nativeMem.getInt(positions.position(index));
    }

    @Override
    public void set(int index, Integer item) {
        setInt(index, item);
    }

    public void setInt(int index, int item) {
        nativeMem.setInt(positions.position(index), item);
    }

}
