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

public class NativeInt8Array extends NativePrimitiveTypeArray<Byte> {

    @SuppressWarnings("unused")
    public static PrimitiveArrayStaticGenerator GENERATOR = new PrimitiveArrayStaticGenerator(NativeType.INT8);

    /**
     * @see StructureStaticVariables#newUnallocated()
     */
    public static NativeInt8Array newUnallocated() {
        return new NativeInt8Array(null, false);
    }

    /**
     * @see StructureStaticVariables#newAllocatable(StructValue)
     */
    public static NativeInt8Array newAllocatable(@NotNull StructValue structValue) {
        return new NativeInt8Array(structValue, true);
    }

    /**
     * @see StructureStaticVariables#newAllocated(StructValue)
     */
    public static NativeInt8Array newAllocated(@NotNull StructValue structValue) {
        NativeInt8Array ret = newAllocatable(structValue);
        ret.allocate();
        return ret;
    }

    protected NativeInt8Array(
            @Nullable StructValue structValue,
            boolean generateInfo
    ) {
        super(structValue, generateInfo, GENERATOR);
    }
    @Override
    public Byte get(@Range(from = 0, to = Integer.MAX_VALUE) int index) {
        return getInt8(index);
    }

    public byte getInt8(@Range(from = 0, to = Integer.MAX_VALUE) int index) {
        return byteBuf.get(positions.position(index));
    }

    @Override
    public void set(int index, Byte item) {
        setInt8(index, item);
    }

    public void setInt8(int index, byte item) {
        byteBuf.put(positions.position(index), item);
    }

    public void set(byte @NotNull [] value) {
        byteBuf.clear();
        byteBuf.put(value);
        byteBuf.clear();
    }

}
