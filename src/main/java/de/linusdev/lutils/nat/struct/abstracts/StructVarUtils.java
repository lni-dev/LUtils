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

package de.linusdev.lutils.nat.struct.abstracts;

import de.linusdev.lutils.nat.abi.ABI;
import de.linusdev.lutils.nat.abi.OverwriteChildABI;
import de.linusdev.lutils.nat.struct.annos.StructValue;
import de.linusdev.lutils.nat.struct.info.StructVarInfo;
import de.linusdev.lutils.nat.struct.info.StructureInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;

public interface StructVarUtils {

    static <T> T getStructVars(
            @NotNull Class<?> clazz,
            @NotNull ABI abi,
            @Nullable OverwriteChildABI overwriteChildAbi,
            @NotNull StructVarResultCollector<T> collector
    ) {
        assert assertNoPrivateStructValues(clazz);
        Field[] fields = clazz.getFields();

        StructVarInfo[] varInfos = new StructVarInfo[fields.length];
        int index = 0;
        int size = 0;

        for(Field field : fields) {
            StructVarInfo info = StructVarInfo.ofField(field, abi, overwriteChildAbi);

            if(info == null) continue;

            //get INFO
            if(info.getStructValue().value() == -1) varInfos[index++] = info;
            else varInfos[info.getStructValue().value()] = info;
            size++;
        }

        StructureInfo[] infos = new StructureInfo[size];
        for (int i = 0; i < size; i++)
            infos[i] = varInfos[i].getInfo();

        return collector.collect(
                Arrays.copyOf(varInfos, size),
                infos
        );
    }

    static boolean assertNoPrivateStructValues(@NotNull Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            if(Modifier.isPublic(field.getModifiers())) {
                continue;
            }

            if(field.getAnnotation(StructValue.class) != null) {
                throw new AssertionError("ComplexStructure '" + clazz.getCanonicalName() + "' contains non-public fields annotated with @StructValue.");
            }
        }

        return true;
    }

    @FunctionalInterface
    interface StructVarResultCollector<T> {

        T collect(
                @NotNull StructVarInfo @NotNull [] varInfos,
                @NotNull StructureInfo @NotNull [] infos
        );
    }

}
