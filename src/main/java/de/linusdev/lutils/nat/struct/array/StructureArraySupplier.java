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

package de.linusdev.lutils.nat.struct.array;

import de.linusdev.lutils.nat.memory.DirectMemoryManager;
import de.linusdev.lutils.nat.struct.abstracts.Structure;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
@FunctionalInterface
public interface StructureArraySupplier<T extends Structure> {

        /**
         * May be used by methods, that require a native array. Enables the method caller to define
         * how the array may be created (for example on a custom {@link DirectMemoryManager}).
         * @param size size of the {@link StructureArray} that should be supplied
         * @param elementClazz the elementClass for the array
         * @param creator {@link de.linusdev.lutils.nat.struct.array.StructureArray.ElementCreator ElementCreator}
         * @return {@link StructureArray} with given {@code size}, {@code elementClazz} and {@code creator}.
         */
        @NotNull StructureArray<T> supply(
                int size,
                @NotNull Class<?> elementClazz,
                @NotNull StructureArray.ElementCreator<T> creator
        );
}