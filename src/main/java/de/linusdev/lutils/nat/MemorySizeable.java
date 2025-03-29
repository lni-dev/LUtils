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

package de.linusdev.lutils.nat;

import org.jetbrains.annotations.NotNull;

/**
 * An object that may be stored in the heap memory.
 */
public interface MemorySizeable {

    static @NotNull MemorySizeable of(int size, int alignment) {
        return new MemorySizeable() {
            @Override
            public int getRequiredSize() {
                return size;
            }

            @Override
            public int getAlignment() {
                return alignment;
            }
        };
    }

    static @NotNull MemorySizeable of(int size) {
        return new MemorySizeable() {
            @Override
            public int getRequiredSize() {
                return size;
            }

            @Override
            public int getAlignment() {
                return size;
            }
        };
    }

    /**
     * Required size in bytes including padding.
     * @return the size in bytes required by this object in memory
     */
    int getRequiredSize();

    /**
     * The recommended alignment of this object in memory.
     * Either {@code 1, 2, 4, 8} or {@code 16}.
     * @return recommended alignment in bytes
     */
    int getAlignment();

}
