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
import java.util.Objects;
import java.util.function.Function;

/**
 * @see de.linusdev.lutils.other.array.ArrayUtils
 */
@SuppressWarnings("unused")
public class IteratorUtils {

    public static <E> @NotNull EmptyIterator<E> emptyIterator() {
        return new EmptyIterator<>();
    }

    public static <E> @NotNull SingleElementIterator<E> singleElementIterator(E element) {
        return new SingleElementIterator<>(element);
    }

    public static <R, E> @NotNull IteratorView<R, E> iteratorView(@NotNull Iterable<R> original, @NotNull Function<R, E> converter) {
        return new IteratorView<>(original, converter);
    }

    public static <E> @NotNull CombinedIterable<E> combine(@NotNull Iterable<E> iterable1, @NotNull Iterable<E> iterable2) {
        return new CombinedIterable<>(iterable1, iterable2);
    }

    /**
     * Creates a string in array format containing all items of given {@code it}.
     * The string for each item will be created using {@link Objects#toString(Object)}. If an item-string contains
     * multiple lines it will be cut off before the first line break and {@code ...} will be appended instead.
     * @param it the iterator which should be converted to a string
     * @return a string of the following form {@code [ item1, item2, item3 ]} as described above.
     */
    public static @NotNull String toString(@NotNull Iterator<?> it) {

        StringBuilder sb = new StringBuilder("[ ");
        boolean first = true;

        while(it.hasNext()) {
            Object item = it.next();

            if(first) first = false;
            else sb.append(", ");

            String sItem = Objects.toString(item);

            int nlIndex;
            if((nlIndex = sItem.indexOf("\n")) != -1) {
               sItem = sItem.substring(0, nlIndex) + " ...";
            }

            sb.append(sItem);
        }

        return sb.append(" ]").toString();
    }
}
