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

package de.linusdev.lutils.nat.integer;

import de.linusdev.lutils.nat.MemorySizeable;
import de.linusdev.lutils.nat.abi.ABI;
import de.linusdev.lutils.nat.struct.abstracts.Structure;
import de.linusdev.lutils.nat.struct.abstracts.StructureStaticVariables;
import de.linusdev.lutils.nat.struct.annos.RequirementType;
import de.linusdev.lutils.nat.struct.generator.SimpleStaticGenerator;
import de.linusdev.lutils.nat.struct.generator.StaticGenerator;
import de.linusdev.lutils.nat.struct.info.StructureInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NativeInteger extends Structure {

    @SuppressWarnings("unused") // accessed via reflection
    public static final @NotNull StaticGenerator GENERATOR = new SimpleStaticGenerator(
            RequirementType.NOT_SUPPORTED,
            RequirementType.NOT_SUPPORTED
    ) {
        @Override
        public @NotNull StructureInfo calculateInfoChecked(
                @NotNull Class<?> selfClazz,
                @NotNull ABI abi,
                int[] length,
                @NotNull Class<?>[] elementTypes
        ) {
            MemorySizeable memorySizeable = abi.types().integer();
            return new StructureInfo(
                    abi, memorySizeable.getAlignment(), false,
                    0, memorySizeable.getRequiredSize(), 0
            );
        }
    };

    /**
     * @see StructureStaticVariables#newUnallocated()
     */
    public static NativeInteger newUnallocated() {
        return new NativeInteger(null, false);
    }

    /**
     * @see StructureStaticVariables#newAllocatable(ABI, int[], Class[]) 
     */
    public static NativeInteger newAllocatable(@Nullable ABI abi) {
        return new NativeInteger(abi, true);
    }


    protected NativeInteger(@Nullable ABI abi, boolean genInfo) {
        super(abi);
        if(genInfo)
            setInfo(getInfoOrFail());
    }

    @Override
    protected @Nullable StaticGenerator getGenerator() {
        return GENERATOR;
    }

    @Override
    protected @Nullable StructureInfo generateInfo() {
        return GENERATOR.calculateInfo(this.getClass(), abi, null, null);
    }

    /**
     * @throws ArithmeticException if given long {@code value} does not fit into this (signed) integer without losing information.
     * @param value value to set
     */
    public void set(long value) {
        long size = this.getRequiredSize();
        if(size == 2) {
            if ((short) value != value) {
                throw new ArithmeticException("short overflow");
            }
            nativeMem.setShort(offset, (short) value);
        }
        else if(size == 4)
            nativeMem.setInt(offset,  Math.toIntExact(value));
        else if(size == 8)
            nativeMem.setLong(offset, value);
        else
            throw new Error("Unexpected Integer Size");
    }

    public long get() {
        long size = this.getRequiredSize();
        if(size == 2)
            return nativeMem.getShort(offset);
        else if(size == 4)
            return nativeMem.getInt(offset);
        else if(size == 8)
            return nativeMem.getLong(offset);
        else
            throw new Error("Unexpected Integer Size");
    }

}
