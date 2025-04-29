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

public class CombinedIterable<T> implements Iterable<T> {

    private final @NotNull Iterable<T> iterable1;
    private final @NotNull Iterable<T> iterable2;

    public CombinedIterable(@NotNull Iterable<T> iterable1, @NotNull Iterable<T> iterable2) {
        this.iterable1 = iterable1;
        this.iterable2 = iterable2;
    }


    @Override
    public @NotNull Iterator<T> iterator() {
        return new It();
    }

    private class It implements Iterator<T> {

        private final Iterator<T> it1 = iterable1.iterator();
        private final Iterator<T> it2 = iterable2.iterator();

        @Override
        public boolean hasNext() {
            return it1.hasNext() || it2.hasNext();
        }

        @Override
        public T next() {
            return it1.hasNext() ? it1.next() : it2.next();
        }
    }
}
