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
import org.jetbrains.annotations.Nullable;

public interface NativeMemAllocator {

    /**
     * Allocate memory of {@code size}. Memory allocated with this function must be explicitly freed
     * using the {@link OwnedNativeMemBuffer#close() close} method.
     * @param size the amount of memory to allocate in bytes
     * @param debugId identifier for the returned memory, which may be useful when receiving memory leak errors.
     * @param zeroMemory whether to zero-initialise the memory.
     * @return Freshly allocated memory
     */
    @NotNull OwnedNativeMemBuffer allocOwned(long size, boolean zeroMemory, @Nullable Identifier debugId);

    /**
     * Allocate memory of {@code size}. Memory allocated with this function must be explicitly freed
     * using the {@link OwnedNativeMemBuffer#close() close} method. The memory will be zero-initialised.
     * @param size the amount of memory to allocate in bytes
     * @return Freshly allocated memory
     */
    default @NotNull OwnedNativeMemBuffer allocOwned(long size) {
        return allocOwned(size, true, null);
    }

    /**
     * Allocate memory of {@code size}. Memory allocated with this function must be explicitly freed
     * using the {@link OwnedNativeMemBuffer#close() close} method.
     * @param size the amount of memory to allocate in bytes.
     * @param zeroMemory whether to zero-initialise the memory.
     * @return Freshly allocated memory
     */
    default @NotNull OwnedNativeMemBuffer allocOwned(long size, boolean zeroMemory) {
        return allocOwned(size, zeroMemory, null);
    }

    /**
     * Allocate memory of {@code size}. Memory allocated with this function must be explicitly freed
     * using the {@link OwnedNativeMemBuffer#close() close} method. The memory will be zero-initialised.
     * @param size the amount of memory to allocate in bytes
     * @param debugId identifier for the returned memory, which may be useful when receiving memory leak errors.
     * @return Freshly allocated memory
     */
    default @NotNull OwnedNativeMemBuffer allocOwned(long size, @NotNull Identifier debugId) {
        return allocOwned(size, true, debugId);
    }



    /**
     * Same as {@link #allocOwned(long)} but the memory will immediately be
     * {@link Structure#claimMemory(NativeMemBuffer, long) claimed} by given {@code structure}.
     * <br><br>
     * Allocate memory of {@code size}. Memory allocated with this function must be explicitly freed
     * using the {@link OwnedNativeMemBuffer#close() close} method.
     * <br><br>
     * The size of the memory is the {@link Structure#getRequiredSize() size of given structure}.
     * <br><br>
     * The memory will be zero-initialised.
     * @param structure a {@link de.linusdev.lutils.nat.struct.abstracts.StructureStaticVariables#newAllocatable(ABI, int[], Class[]) allocatable}
     *                  structure
     * @return Freshly allocated memory
     */
    default @NotNull OwnedNativeMemBuffer allocOwned(@NotNull Structure structure) {
        OwnedNativeMemBuffer buffer = allocOwned(structure.getRequiredSize(), true, null);
        structure.claimMemory(buffer, 0);
        return buffer;
    }

    /**
     * Same as {@link #allocOwned(long, Identifier)} but the memory will immediately be
     * {@link Structure#claimMemory(NativeMemBuffer, long) claimed} by given {@code structure}.
     * <br><br>
     * Allocate memory of {@code size}. Memory allocated with this function must be explicitly freed
     * using the {@link OwnedNativeMemBuffer#close() close} method.
     * <br><br>
     * The size of the memory is the {@link Structure#getRequiredSize() size of given structure}.
     * <br><br>
     * The memory will be zero-initialised.
     * @param structure a {@link de.linusdev.lutils.nat.struct.abstracts.StructureStaticVariables#newAllocatable(ABI, int[], Class[]) allocatable}
     *                  structure
     * @param debugId identifier for the returned memory, which may be useful when receiving memory leak errors.
     * @return Freshly allocated memory
     */
    default @NotNull OwnedNativeMemBuffer allocOwned(@NotNull Structure structure, @Nullable Identifier debugId) {
        OwnedNativeMemBuffer buffer = allocOwned(structure.getRequiredSize(), true, debugId);
        structure.claimMemory(buffer, 0);
        return buffer;
    }

    /**
     * Same as {@link #allocOwned(long)} but the memory will immediately be
     * {@link Structure#claimMemory(NativeMemBuffer, long) claimed} by given {@code structure}.
     * <br><br>
     * Allocate memory of {@code size}. Memory allocated with this function must be explicitly freed
     * using the {@link OwnedNativeMemBuffer#close() close} method.
     * <br><br>
     * The size of the memory is the {@link Structure#getRequiredSize() size of given structure}.
     * <br><br>
     * The memory will be zero-initialised.
     * @param structure a {@link de.linusdev.lutils.nat.struct.abstracts.StructureStaticVariables#newAllocatable(ABI, int[], Class[]) allocatable}
     *                  structure
     * @param zeroMemory whether to zero-initialise the memory.
     * @return Freshly allocated memory
     */
    default @NotNull OwnedNativeMemBuffer allocOwned(@NotNull Structure structure, boolean zeroMemory) {
        OwnedNativeMemBuffer buffer = allocOwned(structure.getRequiredSize(), zeroMemory, null);
        structure.claimMemory(buffer, 0);
        return buffer;
    }

    /**
     * Same as {@link #allocOwned(long, Identifier)} but the memory will immediately be
     * {@link Structure#claimMemory(NativeMemBuffer, long) claimed} by given {@code structure}.
     * <br><br>
     * Allocate memory of {@code size}. Memory allocated with this function must be explicitly freed
     * using the {@link OwnedNativeMemBuffer#close() close} method.
     * <br><br>
     * The size of the memory is the {@link Structure#getRequiredSize() size of given structure}.
     * <br><br>
     * The memory will be zero-initialised.
     * @param structure a {@link de.linusdev.lutils.nat.struct.abstracts.StructureStaticVariables#newAllocatable(ABI, int[], Class[]) allocatable}
     *                  structure
     * @param zeroMemory whether to zero-initialise the memory.
     * @param debugId identifier for the returned memory, which may be useful when receiving memory leak errors.
     * @return Freshly allocated memory
     */
    default @NotNull OwnedNativeMemBuffer allocOwned(@NotNull Structure structure, boolean zeroMemory, @NotNull Identifier debugId) {
        OwnedNativeMemBuffer buffer = allocOwned(structure.getRequiredSize(), zeroMemory, debugId);
        structure.claimMemory(buffer, 0);
        return buffer;
    }

    /**
     * Allocated memory of given {@code size}. Memory allocated with this function will be freed automatically using
     * a {@link java.lang.ref.Cleaner cleaner}.
     * @param size the amount of memory to allocate in bytes
     * @param zeroMemory whether to zero-initialise the memory.
     * @return Freshly allocated memory.
     */
    @NotNull NativeMemBuffer allocManaged(long size, boolean zeroMemory);

    /**
     * Allocated memory of given {@code size}. Memory allocated with this function will be freed automatically using
     * a {@link java.lang.ref.Cleaner cleaner}. The memory will be zero-initialised.
     * @param size the amount of memory to allocate in bytes
     * @return Freshly allocated memory.
     */
    default @NotNull NativeMemBuffer allocManaged(long size) {
        return allocManaged(size, true);
    }


    /**
     * Same as {@link #allocManaged(long)} but the memory will immediately be
     * {@link Structure#claimMemory(NativeMemBuffer, long) claimed} by given {@code structure}.
     * <br><br>
     * Memory allocated with this function will be freed automatically using
     * a {@link java.lang.ref.Cleaner cleaner}.
     * <br><br>
     * The size of the memory is the {@link Structure#getRequiredSize() size of given structure}.
     * <br><br>
     * The memory will be zero-initialised.
     * @param structure a {@link de.linusdev.lutils.nat.struct.abstracts.StructureStaticVariables#newAllocatable(ABI, int[], Class[]) allocatable}
     *                  structure
     * @return given {@code structure}
     */
    @SuppressWarnings("UnusedReturnValue")
    default @NotNull NativeMemBuffer allocManaged(@NotNull Structure structure) {
        NativeMemBuffer buffer = allocManaged(structure.getRequiredSize(), true);
        structure.claimMemory(buffer, 0);
        return buffer;
    }

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
     * @param zeroMemory whether to zero-initialise the memory.
     * @return given {@code structure}
     */
    @SuppressWarnings("UnusedReturnValue")
    default @NotNull NativeMemBuffer allocManaged(@NotNull Structure structure, boolean zeroMemory) {
        NativeMemBuffer buffer = allocManaged(structure.getRequiredSize(), zeroMemory);
        structure.claimMemory(buffer, 0);
        return buffer;
    }

}
