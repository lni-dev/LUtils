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

import de.linusdev.lutils.other.iterator.*;
import org.jetbrains.annotations.NotNull;

/**
 * @see de.linusdev.lutils.other.iterator.IteratorUtils
 */
@SuppressWarnings("unused")
public class ArrayUtils {

    public static <E> @NotNull IterableArray<E> iterableArray(E @NotNull [] array) {
        return new IterableArray<>(array);
    }

    public static @NotNull IterableArrayLong iterableArray(long @NotNull [] array) {
        return new IterableArrayLong(array);
    }

    public static @NotNull IterableArrayInteger iterableArray(int @NotNull [] array) {
        return new IterableArrayInteger(array);
    }

    public static @NotNull IterableArrayFloat iterableArray(float @NotNull [] array) {
        return new IterableArrayFloat(array);
    }

    public static @NotNull IterableArrayDouble iterableArray(double @NotNull [] array) {
        return new IterableArrayDouble(array);
    }

    public static <E> @NotNull CombinedArray<E> combine(@NotNull ArrayWrapper<E> array1, @NotNull ArrayWrapper<E> array2) {
        return new CombinedArray<>(array1, array2);
    }

    public static <E> @NotNull CombinedArray<E> combine(@NotNull E[] array1, @NotNull E[] array2) {
        return new CombinedArray<>(iterableArray(array1), iterableArray(array2));
    }

}
