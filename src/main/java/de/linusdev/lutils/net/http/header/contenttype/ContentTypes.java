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

package de.linusdev.lutils.net.http.header.contenttype;

import de.linusdev.lutils.net.http.header.value.BasicHeaderValue;
import de.linusdev.lutils.net.http.header.value.BasicHeaderValueImpl;
import de.linusdev.lutils.net.http.header.value.parameters.BasicHeaderValueWithCharset;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ContentTypes extends BasicHeaderValueImpl implements ContentType {

    private final @NotNull String type;
    private final @NotNull String subtype;

    ContentTypes(@NotNull String type, @NotNull String subType) {
        super(type + "/" + subType);
        this.type = type;
        this.subtype = subType;
    }

    @Override
    public @NotNull String type() {
        return type;
    }

    @Override
    public @NotNull String subtype() {
        return subtype;
    }

    public static class Text extends ContentTypes implements BasicHeaderValueWithCharset {

        @Contract(" -> new")
        public static @NotNull Text html() {
            return new Text("html");
        }

        @Contract(" -> new")
        public static @NotNull Text plain() {
            return new Text("plain");
        }

        @Contract(" -> new")
        public static @NotNull Text csv() {
            return new Text("csv");
        }

        @Contract(" -> new")
        public static @NotNull Text css() {
            return new Text("css");
        }

        @Contract(" -> new")
        public static @NotNull Text js() {
            return new Text("javascript");
        }

        protected Text(@NotNull String name) {
            super("text", name);
        }

        @Override
        public @NotNull Text setCharset(@Nullable String charset) {
            BasicHeaderValueWithCharset.super.setCharset(charset);
            return this;
        }
    }

    public static class Image extends ContentTypes implements BasicHeaderValue {

        @Contract(" -> new")
        public static @NotNull Image png() {
            return new Image("png");
        }

        protected Image(@NotNull String name) {
            super("image", name);
        }
    }
}
