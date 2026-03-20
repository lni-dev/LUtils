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
import org.jetbrains.annotations.Nullable;

/**
 * This class provides multiple {@link NativeMemAllocator NativeMemAllocators} as well as convenience methods
 * to quickly allocate using the {@link #DEFAULT_ALLOCATOR}.
 * <br><br>
 * Allocators:
 * <ul>
 *     <li>{@link #UNSAFE_ALLOCATOR}</li>
 *     <li>{@link #MALLOC_ALLOCATOR}</li>
 * </ul>
 * Convenience methods which use the {@link #DEFAULT_ALLOCATOR}:
 * <ul>
 *     <li>{@link #allocManaged(Structure)}</li>
 *     <li>{@link #allocOwned(Structure, Identifier)}</li>
 *     <li>{@link #allocOwned(Structure)}</li>
 * </ul>
 */
@SuppressWarnings("removal")
public interface Allocators {

    /**
     * Allocator which uses {@link sun.misc.Unsafe#allocateMemory(long) Unsafe.allocateMemory()}.
     */
    NativeMemAllocator UNSAFE_ALLOCATOR = new UnsafeAllocator();

    /**
     * Allocator which uses {@code malloc} from the c-stdlib.
     */
    NativeMemAllocator MALLOC_ALLOCATOR = new MallocAllocator();

    /**
     * @see #MALLOC_ALLOCATOR
     */
    NativeMemAllocator DEFAULT_ALLOCATOR = MALLOC_ALLOCATOR;

    /**
     * @see NativeMemAllocator#allocOwned(Structure, Identifier)
     * @see #DEFAULT_ALLOCATOR
     */
    static OwnedNativeMemBuffer allocOwned(@NotNull Structure structure, @Nullable Identifier debugId) {
        return DEFAULT_ALLOCATOR.allocOwned(structure, debugId);
    }

    /**
     * @see NativeMemAllocator#allocOwned(Structure)
     * @see #DEFAULT_ALLOCATOR
     */
    static OwnedNativeMemBuffer allocOwned(@NotNull Structure structure) {
        return DEFAULT_ALLOCATOR.allocOwned(structure);
    }

    /**
     * @see NativeMemAllocator#allocManaged(Structure)
     * @see #DEFAULT_ALLOCATOR
     */
    static <T extends Structure> @NotNull T allocManaged(@NotNull T structure) {
        DEFAULT_ALLOCATOR.allocManaged(structure);
        return structure;
    }

}
