/*
 * Copyright (c) 2026 Linus Andera
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

package de.linusdev.lutils.nat.memory;

import de.linusdev.lutils.id.Identifier;
import de.linusdev.lutils.nat.memory.allocator.MallocAllocator;
import de.linusdev.lutils.nat.memory.allocator.UnsafeAllocator;
import de.linusdev.lutils.nat.struct.abstracts.Structure;
import org.jetbrains.annotations.NotNull;

public interface Allocators {

    NativeMemAllocator UNSAFE_ALLOCATOR = new UnsafeAllocator();
    NativeMemAllocator MALLOC_ALLOCATOR = new MallocAllocator();

    NativeMemAllocator DEFAULT_ALLOCATOR = MALLOC_ALLOCATOR;

    static AllocatedMemory allocOwned(@NotNull Structure structure, @NotNull Identifier debugId) {
        return DEFAULT_ALLOCATOR.allocOwned(structure, debugId);
    }

    static AllocatedMemory allocOwned(@NotNull Structure structure) {
        return DEFAULT_ALLOCATOR.allocOwned(structure);
    }

    static <T extends Structure> @NotNull T allocManaged(@NotNull T structure) {
        DEFAULT_ALLOCATOR.allocManaged(structure);
        return structure;
    }

}
