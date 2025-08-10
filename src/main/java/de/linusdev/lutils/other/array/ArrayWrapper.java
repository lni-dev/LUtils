/*
 * Copyright (c) 2025 Linus Andera
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

package de.linusdev.lutils.other.array;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

/**
 * Wraps an array to make it {@link Iterable}.
 * @see ArrayUtils#iterableArray(Object[])
 * @param <T>
 */
public interface ArrayWrapper<T> extends Iterable<T> {

    int length();

    T get(int index);

    @Override
    default @NotNull Iterator<T> iterator() {
        return new Iterator<>() {
            int index = 0;

            @Override
            public boolean hasNext() {
                return index < length();
            }

            @Override
            public T next() {
                return get(index++);
            }
        };
    }
}
