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

package de.linusdev.lutils.nat.memory;

import de.linusdev.lutils.nat.NativeParsable;
import org.jetbrains.annotations.NotNull;

/**
 * Instances of this interface manage directly allocated memory.
 * @see DirectMemoryStack64
 * @see Stack
 */
public interface DirectMemoryManager extends NativeParsable {

    /**
     * size of the managed memory in bytes.
     */
    long memorySize();

    /**
     * amount of bytes currently in use
     */
    long usedByteCount();

    /**
     * amount of bytes currently free
     */
    default long freeByteCount() {
        return memorySize() - usedByteCount();
    }

    /**
     * Percentage of bytes currently in use.
     */
    default double usedBytesPercentage() {
        return ((double) usedByteCount()) / ((double) freeByteCount());
    }

    /**
     * Amount of structures currently managed by this {@link DirectMemoryManager}.
     */
    int currentStructCount();

    default @NotNull String info() {
        return String.format(
                "%s { used:%.3f%% (%d bytes), structs: %d }",
                getClass().getSimpleName(), usedBytesPercentage(), usedByteCount(), currentStructCount()
        );
    }

    /**
     * @return {@code true} if given {@code address} is inside the address space of this memory manager.
     */
    default boolean isAddressInside(long address) {
        if((address -= getPointer()) < 0L) return false; // too small
        return address < memorySize(); // too big
    }

}
