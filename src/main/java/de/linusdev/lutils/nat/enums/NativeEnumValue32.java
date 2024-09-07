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

package de.linusdev.lutils.nat.enums;

import de.linusdev.lutils.math.vector.buffer.intn.BBInt1;
import de.linusdev.lutils.nat.struct.abstracts.StructureStaticVariables;
import de.linusdev.lutils.nat.struct.annos.StructValue;
import org.jetbrains.annotations.Nullable;

/**
 * Type corresponding to a 32 bit native enum type.
 * @param <M> The enum this value must be of.
 * @see NativeEnumMember32
 */
@SuppressWarnings("unused")
public class NativeEnumValue32<M extends NativeEnumMember32> extends BBInt1 implements EnumValue32<M> {

    /**
     * @see StructureStaticVariables#newUnallocated()
     */
    public static <T extends NativeEnumMember32> NativeEnumValue32<T> newUnallocatedT()  {
        return new NativeEnumValue32<>(false, null);
    }

    /**
     * @see StructureStaticVariables#newAllocatable(StructValue) 
     */
    public static <T extends NativeEnumMember32> NativeEnumValue32<T> newAllocatableT(
            @Nullable StructValue structValue
    )  {
        return new NativeEnumValue32<>(true, structValue);
    }

    /**
     * @see StructureStaticVariables#newAllocated(StructValue) 
     */
    public static <T extends NativeEnumMember32> NativeEnumValue32<T> newAllocatedT(
            @Nullable StructValue structValue
    )  {
        NativeEnumValue32<T> ret = newAllocatableT(structValue);
        ret.allocate();
        return ret;
    }

    protected NativeEnumValue32(
            boolean generateInfo,
            @Nullable StructValue structValue
    ) {
        super(generateInfo, structValue);
    }

    @Override
    public int get() {
        return super.get();
    }

    @Override
    public void set(int value) {
        super.set(value);
    }

    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    @Override
    public boolean equals(Object obj) {
        return EnumValue32.equals(this, obj);
    }
}
