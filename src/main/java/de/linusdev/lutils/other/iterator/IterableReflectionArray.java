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

import java.lang.reflect.Array;
import java.util.Iterator;

public class IterableReflectionArray implements Iterable<Object> {

    private final @NotNull Object array;
    private final int length;

    public IterableReflectionArray(@NotNull Object array) {
        if(!array.getClass().isArray())
            throw new IllegalArgumentException("Given object is not an array.");
        this.array = array;
        this.length = Array.getLength(array);
    }

    @Override
    public @NotNull Iterator<Object> iterator() {
        return new It();
    }

    private class It implements Iterator<Object> {

        private int index = 0;

        @Override
        public boolean hasNext() {
            return length > index;
        }

        @Override
        public Object next() {
            return Array.get(array, index++);
        }
    }
}
