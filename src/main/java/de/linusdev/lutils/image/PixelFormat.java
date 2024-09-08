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

package de.linusdev.lutils.image;

import org.jetbrains.annotations.NotNull;

public abstract class PixelFormat<V> {

    public static final PixelFormat<Integer> R8G8B8A8_SRGB = new PixelFormat<>("R8G8B8A8_SRGB") {

        @Override
        public <U> Integer from(@NotNull PixelFormat<U> other, U value) {
            return other.toR8G8B8A8_SRGB(value);
        }

        @Override
        public int toR8G8B8A8_SRGB(Integer value) {
            return value;
        }

        @Override
        public int toA8R8G8B8_SRGB(Integer value) {
            int alpha = (value & 0x000000FF) << 24;
            return (value >>> 8) | alpha;
        }

    };

    public static final PixelFormat<Integer> A8R8G8B8_SRGB = new PixelFormat<>("A8R8G8B8_SRGB") {

        @Override
        public <U> Integer from(@NotNull PixelFormat<U> other, U value) {
            return other.toA8R8G8B8_SRGB(value);
        }

        @Override
        public int toR8G8B8A8_SRGB(Integer value) {
            int alpha = (value & 0xFF000000) >>> 24;
            return (value << 8) | alpha;
        }

        @Override
        public int toA8R8G8B8_SRGB(Integer value) {
            return value;
        }
    };

    protected final @NotNull String name;

    protected PixelFormat(@NotNull String name) {
        this.name = name;
    }

    public abstract  <U> V from(@NotNull PixelFormat<U> other, U value);

    public int toR8G8B8A8_SRGB(V value) {
        throw new UnsupportedOperationException();
    }

    public int toA8R8G8B8_SRGB(V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        return name;
    }
}
