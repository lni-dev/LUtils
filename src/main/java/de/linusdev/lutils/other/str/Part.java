/*
 * Copyright (c) 2024 Linus Andera
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

package de.linusdev.lutils.other.str;

import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;

public interface Part {

    static Part constant(@NotNull String str) {
        return new Const(str);
    }

    static Part placeholder(@NotNull String key) {
        return new Placeholder(key);
    }

    @NotNull String get(@NotNull Map<String, String> values);

    class Const implements Part {

        private final @NotNull String string;

        public Const(@NotNull String string) {
            this.string = string;
        }

        @Override
        public @NotNull String get(@NotNull Map<String, String> values) {
            return string;
        }
    }

    class Placeholder implements Part {
        private final @NotNull String key;

        public Placeholder(@NotNull String key) {
            this.key = key;
        }

        @Override
        public @NotNull String get(@NotNull Map<String, String> values) {
            return Objects.requireNonNull(values.get(key), () -> "'" + key + "' is missing!");
        }
    }

}
