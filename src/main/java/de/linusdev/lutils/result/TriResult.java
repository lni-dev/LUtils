/*
 * Copyright (c) 2024-2025 Linus Andera
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

package de.linusdev.lutils.result;

public record TriResult<T, U, V>(
        T result1,
        U result2,
        V result3
) implements Result {

    @Override
    public int count() {
        return 3;
    }

    @Override
    public Object get(int index) {
        if (index == 0) return result1;
        if (index == 1) return result2;
        if (index == 2) return result3;
        throw new IndexOutOfBoundsException(index);
    }
}
