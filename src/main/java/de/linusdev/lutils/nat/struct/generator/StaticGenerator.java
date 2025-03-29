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

import de.linusdev.lutils.nat.abi.ABI;
import de.linusdev.lutils.nat.abi.OverwriteChildABI;
import de.linusdev.lutils.nat.struct.abstracts.Structure;
import de.linusdev.lutils.nat.struct.annos.StructValue;
import de.linusdev.lutils.nat.struct.annos.StructureSettings;
import de.linusdev.lutils.nat.struct.info.StructureInfo;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @see #calculateInfo(Class, StructValue, StructValue[], ABI, OverwriteChildABI) 
 */
public interface StaticGenerator {

    /**
     * This method is required to be overwritten, if {@link StructureSettings#requiresCalculateInfoMethod()} is set
     * to {@code true}. In that case the return value must not be {@code null}. Before this method is called,
     * given {@code structValue} was checked if it conforms to the {@link StructureSettings} specified by given {@code selfClazz}.
     *
     * @param selfClazz           the class of the {@link Structure} itself
     * @param structValue         the fixed length annotation if any is given
     * @param elementsStructValue array of struct values for the elements.
     */
    @SuppressWarnings("unused")
    default @NotNull StructureInfo calculateInfo(
            @NotNull Class<?> selfClazz,
            @Nullable StructValue structValue,
            @NotNull StructValue @NotNull [] elementsStructValue,
            @NotNull ABI abi,
            @NotNull OverwriteChildABI overwriteChildAbi
    ) {
        //noinspection DataFlowIssue: Example only
        return null;
    }

    @ApiStatus.Experimental
    default @Nullable StructCodeGenerator codeGenerator() {
        return null;
    }

}
