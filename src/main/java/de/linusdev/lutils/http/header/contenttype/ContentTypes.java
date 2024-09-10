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

package de.linusdev.lutils.http.header.contenttype;

import de.linusdev.lutils.http.header.value.BasicHeaderValueImpl;
import de.linusdev.lutils.http.header.value.parameters.BasicHeaderValueWithCharset;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ContentTypes extends BasicHeaderValueImpl implements ContentType {

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

        protected Text(@NotNull String name) {
            super("text/" + name);
        }

        @Override
        public @NotNull Text setCharset(@Nullable String charset) {
            BasicHeaderValueWithCharset.super.setCharset(charset);
            return this;
        }
    }

    ContentTypes(@NotNull String name) {
        super(name);
    }
}
