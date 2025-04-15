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

package de.linusdev.lutils.other.str;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

public class PartsString implements ConstructableString {

    private final @NotNull Part @NotNull [] parts;

    public PartsString(@NotNull Part @NotNull [] parts) {
        this.parts = parts;
    }

    @Override
    public @NotNull String construct(@NotNull Resolver resolver) {
        StringBuilder sb = new StringBuilder();
        for (@NotNull Part part : parts) {
            sb.append(part.get(resolver));
        }

        return sb.toString();
    }

    @Override
    public boolean isConstant() {
        return false;
    }

    public static class Builder {
        private final @NotNull ArrayList<Part> parts = new ArrayList<>();

        public void add(@NotNull Part part) {
            parts.add(part);
        }

        public void addConstant(@NotNull String constant) {
            add(Part.constant(constant));
        }

        public void addPlaceholder(@NotNull String key) {
            add(Part.placeholder(key));
        }

        public @NotNull PartsString build() {
            return new PartsString(parts.toArray(Part[]::new));
        }
    }


    public interface Part {

        static Part constant(@NotNull String str) {
            return new Const(str);
        }

        static Part placeholder(@NotNull String key) {
            return new Placeholder(key);
        }

        @NotNull String get(@NotNull PartsString.Resolver resolver);

        class Const implements Part {

            private final @NotNull String string;

            public Const(@NotNull String string) {
                this.string = string;
            }

            @Override
            public @NotNull String get(@NotNull PartsString.Resolver resolver) {
                return string;
            }
        }

        class Placeholder implements Part {
            private final @NotNull String key;

            public Placeholder(@NotNull String key) {
                this.key = key;
            }

            @Override
            public @NotNull String get(@NotNull PartsString.Resolver values) {
                return Objects.requireNonNull(values.resolve(key), () -> "'" + key + "' is missing!");
            }
        }

    }
}
