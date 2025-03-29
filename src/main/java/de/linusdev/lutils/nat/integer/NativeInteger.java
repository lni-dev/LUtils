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

package de.linusdev.lutils.nat.integer;

import de.linusdev.lutils.nat.MemorySizeable;
import de.linusdev.lutils.nat.abi.ABI;
import de.linusdev.lutils.nat.abi.OverwriteChildABI;
import de.linusdev.lutils.nat.struct.abstracts.Structure;
import de.linusdev.lutils.nat.struct.abstracts.StructureStaticVariables;
import de.linusdev.lutils.nat.struct.annos.RequirementType;
import de.linusdev.lutils.nat.struct.annos.StructValue;
import de.linusdev.lutils.nat.struct.annos.StructureLayoutSettings;
import de.linusdev.lutils.nat.struct.annos.StructureSettings;
import de.linusdev.lutils.nat.struct.generator.StaticGenerator;
import de.linusdev.lutils.nat.struct.info.StructureInfo;
import de.linusdev.lutils.nat.struct.utils.SSMUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@StructureSettings(
        requiresCalculateInfoMethod = true,
        customLayoutOption = RequirementType.OPTIONAL
)
@StructureLayoutSettings
public class NativeInteger extends Structure {

    @SuppressWarnings("unused") // accessed via reflection
    public static final @NotNull StaticGenerator GENERATOR = new StaticGenerator() {
        @Override
        public @NotNull StructureInfo calculateInfo(
                @NotNull Class<?> selfClazz,
                @Nullable StructValue structValue,
                @NotNull StructValue @NotNull [] elementsStructValue,
                @NotNull ABI abi,
                @NotNull OverwriteChildABI overwriteChildAbi
        ) {
            MemorySizeable memorySizeable = abi.types().integer();
            return new StructureInfo(
                    memorySizeable.getAlignment(),
                    false,
                    0,
                    memorySizeable.getRequiredSize(),
                    0
            );
        }
    };

    /**
     * @see StructureStaticVariables#newUnallocated()
     */
    public static NativeInteger newUnallocated() {
        return new NativeInteger(false, null);
    }

    /**
     * @see StructureStaticVariables#newAllocatable(StructValue)
     */
    public static NativeInteger newAllocatable(@Nullable StructValue structValue) {
        return new NativeInteger(true, structValue);
    }

    /**
     * @see StructureStaticVariables#newAllocated(StructValue)
     */
    public static NativeInteger newAllocated(@Nullable StructValue structValue) {
        NativeInteger ret = newAllocatable(structValue);
        ret.allocate();
        return ret;
    }


    protected NativeInteger(boolean generateInfo, @Nullable StructValue structValue) {
        if(generateInfo) {
            setInfo(SSMUtils.getInfo(
                    this.getClass(),
                    structValue,
                    null,
                    null,
                    null,
                    null,
                    GENERATOR
            ));
        }
    }


    /**
     * @throws ArithmeticException if given long {@code value} does not fit into this (signed) integer without losing information.
     * @param value value to set
     */
    public void set(long value) {
        int size = this.getRequiredSize();
        if(size == 2) {
            if ((short) value != value) {
                throw new ArithmeticException("short overflow");
            }
            byteBuf.putShort(0, (short) value);
        }
        else if(size == 4)
            byteBuf.putInt(0,  Math.toIntExact(value));
        else if(size == 8)
            byteBuf.putLong(0, value);
        else
            throw new Error("Unexpected Integer Size");
    }

    public long get() {
        int size = this.getRequiredSize();
        if(size == 2)
            return byteBuf.getShort(0);
        else if(size == 4)
            return byteBuf.getInt(0);
        else if(size == 8)
            return byteBuf.getLong(0);
        else
            throw new Error("Unexpected Integer Size");
    }

}
