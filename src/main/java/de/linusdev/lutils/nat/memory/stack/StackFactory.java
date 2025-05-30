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

package de.linusdev.lutils.nat.memory.stack;

import de.linusdev.lutils.nat.memory.stack.impl.DirectMemoryStack64;
import org.jetbrains.annotations.NotNull;

/**
 * Factory to create new {@link Stack}s.
 */
public interface StackFactory {

    /**
     * {@link StackFactory}, which creates {@link DirectMemoryStack64}. Default size is the same as
     * {@link DirectMemoryStack64#DirectMemoryStack64()}.
     */
    @NotNull StackFactory DEFAULT = new StackFactory() {
        @Override
        public @NotNull Stack create(long size) {
            return new DirectMemoryStack64((int) size);
        }

        @Override
        public @NotNull Stack create() {
            return new DirectMemoryStack64();
        }
    };

    /**
     * Create a {@link Stack} with given {@code size}.
     */
    @NotNull Stack create(long size);

    /**
     * Create a {@link Stack} with a suitable default size.
     */
    @NotNull Stack create();

}
