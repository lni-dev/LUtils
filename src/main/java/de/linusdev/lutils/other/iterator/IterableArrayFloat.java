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

public class IterableArrayFloat implements ArrayWrapper<Float> {

    private final float @NotNull [] array;

    public IterableArrayFloat(float @NotNull [] array) {
        this.array = array;
    }

    @Override
    public @NotNull Iterator<Float> iterator() {
        return new It();
    }

    @Override
    public int length() {
        return array.length;
    }

    @Override
    public Float get(int index) {
        return array[index];
    }

    private class It implements Iterator<Float> {

        private int index = 0;

        @Override
        public boolean hasNext() {
            return array.length > index;
        }

        @Override
        public Float next() {
            return array[index++];
        }
    }
}
