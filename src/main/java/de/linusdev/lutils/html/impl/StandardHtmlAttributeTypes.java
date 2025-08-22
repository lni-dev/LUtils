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
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashSet;
import java.util.List;

/**
 * Some standard html attribute types.
 * @see #VALUES
 */
public class StandardHtmlAttributeTypes {
    public static final @NotNull OrderedSetType     CLASS            =   new OrderedSetType("class");

    public static final @NotNull StringType         ID               =   new StringType("id");
    public static final @NotNull StringType         HREF             =   new StringType("href");
    public static final @NotNull StringType         SRC              =   new StringType("src");
    public static final @NotNull StringType         REL              =   new StringType("rel");
    public static final @NotNull StringType         ONCLICK          =   new StringType("onclick");
    public static final @NotNull StringType         ONSUBMIT         =   new StringType("onsubmit");
    public static final @NotNull StringType         ONCHANGE         =   new StringType("onchange");
    public static final @NotNull StringType         ON_MOUSE_ENTER   =   new StringType("onmouseenter");
    public static final @NotNull StringType         ON_MOUSE_LEAVE   =   new StringType("onmouseleave");
    public static final @NotNull StringType         ACCEPT_CHARSET   =   new StringType("accept-charset");
    public static final @NotNull StringType         TYPE             =   new StringType("type");
    public static final @NotNull StringType         AUTOCOMPLETE     =   new StringType("autocomplete");
    public static final @NotNull StringType         VALUE            =   new StringType("value");
    public static final @NotNull StringType         NAME             =   new StringType("name");
    public static final @NotNull StringType         CONTENT          =   new StringType("content");
    public static final @NotNull StringType         STYLE            =   new StringType("style");


    public static final @NotNull NoValueType        OPEN             =   new NoValueType("open");
    public static final @NotNull NoValueType        REQUIRED         =   new NoValueType("required");


    public static final @NotNull HtmlAttributeType<?> @NotNull [] VALUES = new HtmlAttributeType[] {
            CLASS,

            ID, HREF, SRC, REL, ONCLICK , TYPE, AUTOCOMPLETE, VALUE, ONSUBMIT, ACCEPT_CHARSET, NAME,
            CONTENT, ON_MOUSE_ENTER, ON_MOUSE_LEAVE, STYLE, ONCHANGE,

            REQUIRED, OPEN
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
        public @Nullable String convertValue(String value) {
            return value;
        }

        @Override
        public @NotNull String convertValue(@NotNull HtmlAttribute attribute) {
            String val = attribute.value();
            return val == null ? "" : val;
        }

        @Override
        public @Nullable String add(@Nullable String current, @NotNull String item) {
            if(current == null)
                return item;
            return current + item;
        }
    }

    public static class OrderedSetType extends Type<LinkedHashSet<String>> {

        public OrderedSetType(@NotNull String name) {
            super(name);
        }

        @Override
        public @Nullable String convertValue(LinkedHashSet<String> value) {
            return value.stream().reduce("", (accumulator, next) -> accumulator + " " + next);
        }

        @Override
        public @NotNull LinkedHashSet<@NotNull String> convertValue(@NotNull HtmlAttribute attribute) {
            String val = attribute.value();
            return new LinkedHashSet<>(List.of(val == null ? new String[0] : val.split(" ")));
        }

        @Override
        public @NotNull String add(@Nullable String current, @NotNull String item) {
            if(current == null)
                return item;

            if(current.contains(" " + item + " "))
                return current;

            return current + " " + item;
        }

        @Override
        public @Nullable String remove(@Nullable String current, @NotNull String item) {
            if(current == null)
                return null;

            if(current.contains(" " + item + " "))
                return current.replace(" " + item, "");

            return current;
        }
    }

    public static class NoValueType extends Type<Void> {

        public NoValueType(@NotNull String name) {
            super(name);
        }

        @Override
        public @Nullable String convertValue(Void value) {
            return null;
        }

        @Override
        public @Nullable Void convertValue(@NotNull HtmlAttribute attribute) {
            return null;
        }

        public @NotNull HtmlAttribute of() {
            return of(null);
        }
    }
}
