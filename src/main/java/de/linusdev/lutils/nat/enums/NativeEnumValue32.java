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

package de.linusdev.lutils.nat.enums;

import de.linusdev.lutils.math.vector.buffer.intn.BBInt1;
import de.linusdev.lutils.nat.abi.ABI;
import de.linusdev.lutils.nat.struct.abstracts.StructureStaticVariables;
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
        return new NativeEnumValue32<>(null);
    }

    /**
     * @see StructureStaticVariables#newAllocatable(ABI, int[], Class[])  
     */
    public static <T extends NativeEnumMember32> NativeEnumValue32<T> newAllocatableT(@Nullable ABI abi)  {
        return new NativeEnumValue32<>(abi);
    }

    protected NativeEnumValue32(@Nullable ABI abi) {
        super(abi);
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
