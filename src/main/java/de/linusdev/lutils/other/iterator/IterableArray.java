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

package de.linusdev.lutils.other.iterator;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public class IterableArray<T> implements Iterable<T> {

    private final T @NotNull [] array;

    public IterableArray(T @NotNull [] array) {
        this.array = array;
    }

    @Override
    public @NotNull Iterator<T> iterator() {
        return new It();
    }

    private class It implements Iterator<T> {

        private int index = 0;

        @Override
        public boolean hasNext() {
            return array.length > index;
        }

        @Override
        public T next() {
            return array[index++];
        }
    }
}
