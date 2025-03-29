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

package de.linusdev.lutils.nat.struct.generator;

import de.linusdev.lutils.nat.struct.abstracts.Structure;
import de.linusdev.lutils.nat.struct.info.StructureInfo;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ApiStatus.Experimental
public interface StructCodeGenerator {

    /**
     * Can be overwritten if struct code can be generated.
     * @param language {@link Language}
     * @param selfClazz the class of the {@link Structure} itself
     * @param info The {@link StructureInfo} of this structure
     * @return generated struct code or {@code null} if it cannot be generated.
     */
    @SuppressWarnings("unused")
    default @Nullable String generateStructCode(
            @NotNull Language language,
            @NotNull Class<?> selfClazz,
            @NotNull StructureInfo info
    ) {
        return null;
    }

    /**
     * Must be implemented.
     * @param language {@link Language}
     * @param selfClazz the class of the {@link Structure} itself
     * @param info The {@link StructureInfo} of this structure
     * @return struct type name
     */
    @NotNull String getStructTypeName(
            @NotNull Language language,
            @NotNull Class<?> selfClazz,
            @NotNull StructureInfo info
    );

    /**
     * Can be overwritten if required. For example for arrays.
     * @param language {@link org.intellij.lang.annotations.Language}
     * @param selfClazz the class of the {@link Structure} itself
     * @param info The {@link StructureInfo} of this structure
     * @param varName the name of the variable
     * @return struct variable definition
     */
    @SuppressWarnings("unused")
    default @NotNull String getStructVarDef(
            @NotNull Language language,
            @NotNull Class<?> selfClazz,
            @NotNull StructureInfo info,
            @NotNull String varName
    ) {
        return getStructTypeName(language, selfClazz, info) + " " + varName + language.lineEnding;
    }

}
