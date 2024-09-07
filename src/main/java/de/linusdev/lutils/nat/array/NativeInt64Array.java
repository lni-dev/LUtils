/*
 * Copyright (c) 2024 Linus Andera
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

public class NativeInt64Array extends NativePrimitiveTypeArray<Long> {

    @SuppressWarnings("unused")
    public static PrimitiveArrayStaticGenerator GENERATOR = new PrimitiveArrayStaticGenerator(NativeType.INT64);

    /**
     * @see StructureStaticVariables#newUnallocated()
     */
    public static NativeInt64Array newUnallocated() {
        return new NativeInt64Array(null, false);
    }

    /**
     * @see StructureStaticVariables#newAllocatable(StructValue)
     */
    public static NativeInt64Array newAllocatable(@NotNull StructValue structValue) {
        return new NativeInt64Array(structValue, true);
    }

    /**
     * @see StructureStaticVariables#newAllocated(StructValue)
     */
    public static NativeInt64Array newAllocated(@NotNull StructValue structValue) {
        NativeInt64Array ret = newAllocatable(structValue);
        ret.allocate();
        return ret;
    }

    protected NativeInt64Array(
            @Nullable StructValue structValue,
            boolean generateInfo
    ) {
        super(structValue, generateInfo, GENERATOR);
    }

    @Override
    public Long get(@Range(from = 0, to = Integer.MAX_VALUE) int index) {
        return getLong(index);
    }

    public long getLong(@Range(from = 0, to = Integer.MAX_VALUE) int index) {
        return byteBuf.getLong(positions.position(index));
    }

    @Override
    public void set(int index, Long item) {
        setLong(index, item);
    }

    public void setLong(int index, long item) {
        byteBuf.putLong(positions.position(index), item);
    }

}
