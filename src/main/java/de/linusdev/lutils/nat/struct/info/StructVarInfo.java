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

package de.linusdev.lutils.nat.struct.info;

import de.linusdev.lutils.nat.abi.ABI;
import de.linusdev.lutils.nat.struct.abstracts.Structure;
import de.linusdev.lutils.nat.struct.annos.StructValue;
import de.linusdev.lutils.nat.struct.exception.IllegalStructVarException;
import de.linusdev.lutils.nat.struct.utils.SSMUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;

public class StructVarInfo {

    public static @Nullable StructVarInfo ofField(
            @NotNull Field field,
            @Nullable ABI abi
    ) {
        @NotNull Class<?> fieldClass = field.getType();
        StructValue sv = field.getAnnotation(StructValue.class);

        if(sv == null) return null;

        int @Nullable [] length = sv.length();
        length = length[0] == -1 ? null : length;

        @NotNull Class<?> @Nullable [] elementTypes = sv.elementType();
        elementTypes = Structure.class.equals(elementTypes[0]) ? null : elementTypes;

        StructureInfo info;
        try {
            info = SSMUtils.getInfo(null, fieldClass, abi, length, elementTypes);
        } catch (Throwable t) {
            throw new IllegalStructVarException(field, t);
        }

        //noinspection unchecked: Checked in above method
        return new StructVarInfo(sv, (Class<? extends Structure>) fieldClass, field.getName(), info, field);
    }

    protected final @NotNull StructValue structValue;
    protected final @NotNull Class<? extends Structure> clazz;
    protected final @NotNull String varName;
    protected final @NotNull StructureInfo info;
    protected final @NotNull Field field;

    public StructVarInfo(
            @NotNull StructValue structValue,
            @NotNull Class<? extends Structure> clazz,
            @NotNull String varName,
            @NotNull StructureInfo info,
            @NotNull Field field) {
        this.structValue = structValue;
        this.clazz = clazz;
        this.varName = varName;
        this.info = info;
        this.field = field;
    }

    public @NotNull StructValue getStructValue() {
        return structValue;
    }

    public @NotNull Class<? extends Structure> getClazz() {
        return clazz;
    }

    public @NotNull String getVarName() {
        return varName;
    }

    public @NotNull StructureInfo getInfo() {
        return info;
    }

    public Structure get(@NotNull Structure instance) {
        try {
            return (Structure) field.get(instance);
        } catch (IllegalAccessException e) {
            throw new IllegalStructVarException(field, e);
        }
    }
}
