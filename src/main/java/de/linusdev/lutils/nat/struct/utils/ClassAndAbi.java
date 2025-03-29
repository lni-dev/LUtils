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

package de.linusdev.lutils.nat.struct.utils;

import de.linusdev.lutils.nat.abi.ABI;
import org.jetbrains.annotations.NotNull;

public class ClassAndAbi {
    private final @NotNull Class<?> clazz;
    private final @NotNull ABI abi;

    public ClassAndAbi(@NotNull Class<?> clazz, @NotNull ABI abi) {
        this.clazz = clazz;
        this.abi = abi;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ClassAndAbi)) return false;

        ClassAndAbi that = (ClassAndAbi) o;
        return clazz.equals(that.clazz) && abi.identifier().equals(that.abi.identifier());
    }

    @Override
    public int hashCode() {
        int result = clazz.hashCode();
        result = 31 * result + abi.identifier().hashCode();
        return result;
    }
}
