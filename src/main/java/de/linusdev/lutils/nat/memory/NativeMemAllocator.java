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
import de.linusdev.lutils.nat.abi.ABI;
import de.linusdev.lutils.nat.struct.abstracts.Structure;
import org.jetbrains.annotations.NotNull;

public interface NativeMemAllocator {

    /**
     * Allocate memory of {@code size}. Memory allocated with this function must be explicitly freed
     * using the {@link AllocatedMemory#close() close} method.
     * @param size the amount of memory to allocate in bytes
     * @return Freshly allocated memory
     */
    @NotNull AllocatedMemory allocOwned(long size);

    /**
     * Allocate memory of {@code size}. Memory allocated with this function must be explicitly freed
     * using the {@link AllocatedMemory#close() close} method.
     * @param size the amount of memory to allocate in bytes
     * @param debugId identifier for the returned memory, which may be useful when receiving memory leak errors.
     * @return Freshly allocated memory
     */
    @NotNull AllocatedMemory allocOwned(long size, @NotNull Identifier debugId);

    /**
     * Same as {@link #allocOwned(long)} but the memory will immediately be
     * {@link Structure#claimMemory(NativeMemBuffer, long) claimed} by given {@code structure}.
     * <br><br>
     * Allocate memory of {@code size}. Memory allocated with this function must be explicitly freed
     * using the {@link AllocatedMemory#close() close} method.
     * <br><br>
     * The size of the memory is the {@link Structure#getRequiredSize() size of given structure}.
     * @param structure a {@link de.linusdev.lutils.nat.struct.abstracts.StructureStaticVariables#newAllocatable(ABI, int[], Class[]) allocatable}
     *                  structure
     * @return Freshly allocated memory
     */
    default @NotNull AllocatedMemory allocOwned(@NotNull Structure structure) {
        AllocatedMemory buffer = allocOwned(structure.getRequiredSize());
        structure.claimMemory(buffer, 0);
        return buffer;
    }

    /**
     * Same as {@link #allocOwned(long, Identifier)} but the memory will immediately be
     * {@link Structure#claimMemory(NativeMemBuffer, long) claimed} by given {@code structure}.
     * <br><br>
     * Allocate memory of {@code size}. Memory allocated with this function must be explicitly freed
     * using the {@link AllocatedMemory#close() close} method.
     * <br><br>
     * The size of the memory is the {@link Structure#getRequiredSize() size of given structure}.
     * @param structure a {@link de.linusdev.lutils.nat.struct.abstracts.StructureStaticVariables#newAllocatable(ABI, int[], Class[]) allocatable}
     *                  structure
     * @param debugId identifier for the returned memory, which may be useful when receiving memory leak errors.
     * @return Freshly allocated memory
     */
    default @NotNull AllocatedMemory allocOwned(@NotNull Structure structure, @NotNull Identifier debugId) {
        AllocatedMemory buffer = allocOwned(structure.getRequiredSize(), debugId);
        structure.claimMemory(buffer, 0);
        return buffer;
    }

    /**
     * Allocated memory of given {@code size}. Memory allocated with this function will be freed automatically using
     * a {@link java.lang.ref.Cleaner cleaner}.
     * @param size the amount of memory to allocate in bytes
     * @return Freshly allocated memory.
     */
    @NotNull NativeMemBuffer allocManaged(long size);


    /**
     * Same as {@link #allocManaged(long)} but the memory will immediately be
     * {@link Structure#claimMemory(NativeMemBuffer, long) claimed} by given {@code structure}.
     * <br><br>
     * Memory allocated with this function will be freed automatically using
     * a {@link java.lang.ref.Cleaner cleaner}.
     * <br><br>
     * The size of the memory is the {@link Structure#getRequiredSize() size of given structure}.
     * @param structure a {@link de.linusdev.lutils.nat.struct.abstracts.StructureStaticVariables#newAllocatable(ABI, int[], Class[]) allocatable}
     *                  structure
     * @return given {@code structure}
     */
    @SuppressWarnings("UnusedReturnValue")
    default @NotNull NativeMemBuffer allocManaged(@NotNull Structure structure) {
        NativeMemBuffer buffer = allocManaged(structure.getRequiredSize());
        structure.claimMemory(buffer, 0);
        return buffer;
    }

}
