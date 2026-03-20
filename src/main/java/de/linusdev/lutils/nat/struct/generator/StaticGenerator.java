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

package de.linusdev.lutils.nat.struct.generator;

import de.linusdev.lutils.nat.abi.ABI;
import de.linusdev.lutils.nat.struct.abstracts.Structure;
import de.linusdev.lutils.nat.struct.info.StructureInfo;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @see #calculateInfo(Class, ABI, int[], Class[]) 
 * @see SimpleStaticGenerator
 */
public interface StaticGenerator {

    /**
     * Calculates the {@link StructureInfo} of a specific structure. This method must also check if the arguments
     * passed are valid and may throw {@link StructureLayoutGenerationException} if some information is missing or invalid.
     * <br><br>
     * Each structure may support different arguments, which should be stated in the documentation in the structure itself.
     * If nothing is specified it is expected that this structure supports any {@code abi} and {@code length} and {@code elementTypes}
     * must be {@code null}.
     *
     * @param selfClazz the class of the {@link Structure} itself.
     * @param abi the {@link ABI} to use while creating the {@link StructureInfo}.
     * @param length  length information if required/supported by the structure.
     * @param elementTypes element type information if required/supported by the structure.
     * @return A {@link StructureInfo}
     * @throws StructureLayoutGenerationException if some information is missing or invalid.
     */
    @SuppressWarnings("unused")
    @NotNull StructureInfo calculateInfo(
            @NotNull Class<?> selfClazz,
            @Nullable ABI abi,
            int @Nullable [] length,
            @NotNull Class<?> @Nullable [] elementTypes
    );

    @ApiStatus.Experimental
    default @Nullable StructCodeGenerator codeGenerator() {
        return null;
    }

}
