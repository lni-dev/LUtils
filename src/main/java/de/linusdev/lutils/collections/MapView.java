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

import java.util.function.Function;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public class MapView<K, V> implements BiIterable<K, V> {

    protected final @NotNull Supplier<Integer> size;
    protected final @NotNull Function<K, V> get;
    protected final @NotNull BiIterable<K, V> iterable;

    protected MapView(
            @NotNull Supplier<Integer> size,
            @NotNull Function<K, V> get,
            @NotNull BiIterable<K, V> iterable
    ) {
        this.size = size;
        this.get = get;
        this.iterable = iterable;
    }

    @Override
    public @NotNull BiIterator<K, V> iterator() {
        return iterable.iterator();
    }

    public int size() {
        return size.get();
    }

    public boolean isEmpty() {
        return size.get() == 0;
    }

    public V get(K key) {
        return get.apply(key);
    }

}
