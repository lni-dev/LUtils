/*
 * Copyright (c) 2024-2025 Linus Andera
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
import de.linusdev.lutils.nat.struct.abstracts.StructureStaticVariables;
import de.linusdev.lutils.nat.struct.annos.StructValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

public class NativeInt32Array extends NativePrimitiveTypeArray<Integer> {

    @SuppressWarnings("unused")
    public static PrimitiveArrayStaticGenerator GENERATOR = new PrimitiveArrayStaticGenerator(NativeType.INT32);

    /**
     * @see StructureStaticVariables#newUnallocated()
     */
    public static NativeInt32Array newUnallocated() {
        return new NativeInt32Array(null, false);
    }

    /**
     * @see StructureStaticVariables#newAllocatable(StructValue)
     */
    public static NativeInt32Array newAllocatable(@NotNull StructValue structValue) {
        return new NativeInt32Array(structValue, true);
    }

    /**
     * @see StructureStaticVariables#newAllocated(StructValue)
     */
    public static NativeInt32Array newAllocated(@NotNull StructValue structValue) {
        NativeInt32Array ret = newAllocatable(structValue);
        ret.allocate();
        return ret;
    }

    protected NativeInt32Array(
            @Nullable StructValue structValue,
            boolean generateInfo
    ) {
        super(structValue, generateInfo, GENERATOR);
    }

    @Override
    public Integer get(@Range(from = 0, to = Integer.MAX_VALUE) int index) {
        return getInt(index);
    }

    public int getInt(@Range(from = 0, to = Integer.MAX_VALUE) int index) {
        return byteBuf.getInt(positions.position(index));
    }

    @Override
    public void set(int index, Integer item) {
        setInt(index, item);
    }

    public void setInt(int index, int item) {
        byteBuf.putInt(positions.position(index), item);
    }

}
