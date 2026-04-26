/*
 * Copyright (c) 2026 Linus Andera
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

/**
 * A fake array which returns the same {@link #value} for every index.
 * @param <T> element type
 */
public class FakeArray<T> implements ArrayWrapper<T> {

    private final int length;
    private final T value;

    public FakeArray(int length, T value) {
        this.length = length;
        this.value = value;
    }

    @Override
    public int length() {
        return length;
    }

    @Override
    public T get(int index) {
        return value;
    }
}
