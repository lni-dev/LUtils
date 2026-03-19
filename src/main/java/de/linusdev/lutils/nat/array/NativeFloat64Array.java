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

public class NativeFloat64Array extends NativePrimitiveTypeArray<Double> {

    @SuppressWarnings("unused")
    public static PrimitiveArrayStaticGenerator GENERATOR = new PrimitiveArrayStaticGenerator(NativeType.FLOAT64);

    /**
     * @see StructureStaticVariables#newUnallocated()
     */
    public static NativeFloat64Array newUnallocated() {
        return new NativeFloat64Array();
    }

    /**
     * @see StructureStaticVariables#newAllocatable(ABI, int[], Class[]) 
     */
    public static NativeFloat64Array newAllocatable(@NotNull ABI abi, int length) {
        return new NativeFloat64Array(abi, length);
    }

    protected NativeFloat64Array(@Nullable ABI abi, int length) {
        super(GENERATOR, abi, length);
    }

    protected NativeFloat64Array() {
        super(GENERATOR);
    }

    @Override
    public Double get(@Range(from = 0, to = Integer.MAX_VALUE) int index) {
        return getFloat64(index);
    }

    public double getFloat64(@Range(from = 0, to = Integer.MAX_VALUE) int index) {
        return nativeMem.getDouble(positions.position(index));
    }

    @Override
    public void set(int index, Double item) {
        setFloat64(index, item);
    }

    public void setFloat64(int index, double item) {
        nativeMem.setDouble(positions.position(index), item);
    }

}
