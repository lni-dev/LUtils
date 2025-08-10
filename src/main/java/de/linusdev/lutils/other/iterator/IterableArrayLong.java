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

import de.linusdev.lutils.other.array.ArrayWrapper;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public class IterableArrayLong implements ArrayWrapper<Long> {

    private final long @NotNull [] array;

    public IterableArrayLong(long @NotNull [] array) {
        this.array = array;
    }

    @Override
    public int length() {
        return array.length;
    }

    @Override
    public Long get(int index) {
        return array[index];
    }

    @Override
    public @NotNull Iterator<Long> iterator() {
        return new It();
    }

    private class It implements Iterator<Long> {

        private int index = 0;

        @Override
        public boolean hasNext() {
            return array.length > index;
        }

        @Override
        public Long next() {
            return array[index++];
        }
    }
}
