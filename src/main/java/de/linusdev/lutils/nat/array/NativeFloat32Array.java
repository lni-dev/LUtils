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

public class NativeFloat32Array extends NativePrimitiveTypeArray<Float> {

    @SuppressWarnings("unused")
    public static PrimitiveArrayStaticGenerator GENERATOR = new PrimitiveArrayStaticGenerator(NativeType.FLOAT32);

    /**
     * @see StructureStaticVariables#newUnallocated()
     */
    public static NativeFloat32Array newUnallocated() {
        return new NativeFloat32Array();
    }

    /**
     * @see StructureStaticVariables#newAllocatable(ABI, int[], Class[]) 
     */
    public static NativeFloat32Array newAllocatable(@Nullable ABI abi, int length) {
        return new NativeFloat32Array(abi, length);
    }

    protected NativeFloat32Array(@Nullable ABI abi, int length) {
        super(GENERATOR, abi, length);
    }

    protected NativeFloat32Array() {
        super(GENERATOR);
    }

    @Override
    public Float get(@Range(from = 0, to = Integer.MAX_VALUE) int index) {
        return getFloat(index);
    }

    public float getFloat(@Range(from = 0, to = Integer.MAX_VALUE) int index) {
        return nativeMem.getFloat(posInBuf(index));
    }

    @Override
    public void set(int index, Float item) {
        setFloat(index, item);
    }

    public void setFloat(int index, float item) {
        nativeMem.setFloat(posInBuf(index), item);
    }

}
