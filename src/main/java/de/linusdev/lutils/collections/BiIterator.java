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

import de.linusdev.lutils.interfaces.TBiConsumer;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public interface BiIterator<K, V> extends Iterator<Entry<K, V>> {

    static <K, V> @NotNull BiIterator<K, V> of(@NotNull Iterable<Entry<K, V>> iterable) {
        return new BiIterator<>() {
            final Iterator<Entry<K, V>> it = iterable.iterator();

            @Override
            public @NotNull Entry<K, V> next() {
                return it.next();
            }

            @Override
            public boolean hasNext() {
                return it.hasNext();
            }
        };
    }

    @SuppressWarnings("unused")
    default <E extends Throwable> void forEachRemaining(@NotNull TBiConsumer<K, V, E> action) throws E {
        while (hasNext()) {
            var entry = next();
            action.consume(entry.key(), entry.value());
        }
    }

    @Override
    @NotNull Entry<K, V> next();
}
