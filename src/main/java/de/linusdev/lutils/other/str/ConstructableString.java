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
import java.util.Map;

public class ConstructableString {

    private final @NotNull Part @NotNull [] parts;

    public ConstructableString(@NotNull Part @NotNull [] parts) {
        this.parts = parts;
    }


    public @NotNull String construct(@NotNull Map<String, String> values) {
        StringBuilder sb = new StringBuilder();
        for (@NotNull Part part : parts) {
            sb.append(part.get(values));
        }

        return sb.toString();
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

        public @NotNull ConstructableString build() {
            return new ConstructableString(parts.toArray(Part[]::new));
        }
    }
}
