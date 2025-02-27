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

package de.linusdev.lutils.html.impl;

import de.linusdev.lutils.html.HtmlAttribute;
import de.linusdev.lutils.html.HtmlAttributeType;
import org.jetbrains.annotations.NotNull;

/**
 * Some standard html attribute types.
 * @see #VALUES
 */
public class StandardHtmlAttributeTypes {
    public static final @NotNull HtmlAttributeType<String[]> CLASS = new ListType("class");
    public static final @NotNull HtmlAttributeType<String> ID = new StringType("id");
    public static final @NotNull HtmlAttributeType<String> HREF = new StringType("href");
    public static final @NotNull HtmlAttributeType<String> REL = new StringType("rel");

    public static final @NotNull HtmlAttributeType<?> @NotNull [] VALUES = new HtmlAttributeType[] {
            CLASS,
            ID,
            HREF,
            REL
    };

    public static abstract class Type<V> implements HtmlAttributeType<V> {

        private final @NotNull String name;

        public Type(@NotNull String name) {
            this.name = name;
        }

        @Override
        public @NotNull String name() {
            return name;
        }

        @Override
        public int hashCode() {
            return HtmlAttributeType.hashcode(this);
        }
    }

    public static class StringType extends Type<String> {

        public StringType(@NotNull String name) {
            super(name);
        }

        @Override
        public @NotNull String convertValue(@NotNull HtmlAttribute attribute) {
            String val = attribute.value();
            return val == null ? "" : val;
        }
    }

    public static class ListType extends Type<String[]> {

        public ListType(@NotNull String name) {
            super(name);
        }

        @Override
        public String @NotNull [] convertValue(@NotNull HtmlAttribute attribute) {
            String val = attribute.value();
            return val == null ? new String[0] : val.split(" ");
        }
    }
}
