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

import org.jetbrains.annotations.NotNull;

/**
 * Combines two arrays into a single {@link ArrayWrapper}.
 * @param <T> the element type
 */
public class CombinedArray<T> implements ArrayWrapper<T> {

    private final @NotNull ArrayWrapper<T> array1;
    private final @NotNull ArrayWrapper<T> array2;
    private final int length;

    public CombinedArray(@NotNull ArrayWrapper<T> array1, @NotNull ArrayWrapper<T> array2) {
        this.array1 = array1;
        this.array2 = array2;
        this.length = array1.length() + array2.length();
    }

    @Override
    public int length() {
        return length;
    }

    @Override
    public T get(int index) {
        if(index >= array1.length())
            return array2.get(index - array1.length());
        return array1.get(index);
    }
}
