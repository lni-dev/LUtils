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

package de.linusdev.lutils.collections;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.function.Function;

public interface BiIterable<K, V> extends Iterable<Entry<K, V>> {

    static <K, V, I> @NotNull BiIterable<K, V> of(
            @NotNull Iterable<I> iterable,
            @NotNull Function<I, K> key,
            @NotNull Function<I, V> value
    ) {
        return () -> new BiIterator<>() {
            final Iterator<I> it = iterable.iterator();

            @Override
            public @NotNull EntryImpl<K, V> next() {
                I next = it.next();
                return new EntryImpl<>(key.apply(next), value.apply(next));
            }

            @Override
            public boolean hasNext() {
                return it.hasNext();
            }
        };
    }

    @Override
    @NotNull BiIterator<K, V> iterator();
}
