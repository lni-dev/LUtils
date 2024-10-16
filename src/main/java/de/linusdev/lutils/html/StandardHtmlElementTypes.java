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

package de.linusdev.lutils.html;

import de.linusdev.lutils.html.impl.StandardHtmlElement;
import org.jetbrains.annotations.NotNull;

public class StandardHtmlElementTypes {
    public final static @NotNull Type HTML = new Type("html", false);
    public final static @NotNull Type HEAD = new Type("head", false);
    public final static @NotNull Type BODY = new Type("body", false);

    public final static @NotNull Type TITLE = new Type("title", true);

    public final static @NotNull Type PARAGRAPH = new Type("p", true);
    public final static @NotNull Type DIV = new Type("div", false);

    public final static @NotNull Type H1 = new Type("h1", true);
    public final static @NotNull Type H2 = new Type("h2", true);
    public final static @NotNull Type H3 = new Type("h3", true);
    public final static @NotNull Type H4 = new Type("h4", true);
    public final static @NotNull Type H5 = new Type("h5", true);
    public final static @NotNull Type H6 = new Type("h6", true);
    public final static @NotNull Type H7 = new Type("h7", true);
    public final static @NotNull Type H8 = new Type("h8", true);
    public final static @NotNull Type H9 = new Type("h9", true);


    public final static @NotNull HtmlElementType<?> @NotNull [] VALUES = new HtmlElementType[] {
            HTML,
            HEAD,
            BODY,
            TITLE,
            PARAGRAPH,
            DIV,
            H1,
            H2,
            H3,
            H4,
            H5,
            H6,
            H7,
            H8,
            H9,
    };

    private StandardHtmlElementTypes() { }

    public static class Type implements HtmlElementType<StandardHtmlElement.Builder> {

        private final @NotNull String name;
        private final boolean inline;

        public Type(@NotNull String name, boolean inline) {
            this.name = name;
            this.inline = inline;
        }

        @Override
        public @NotNull String name() {
            return name;
        }

        @Override
        public @NotNull StandardHtmlElement.Builder builder() {
            return new StandardHtmlElement.Builder(this);
        }

        @Override
        public @NotNull HtmlObjectParser<? extends HtmlElement> parser() {
            return new StandardHtmlElement.Parser(this);
        }

        @SuppressWarnings("BooleanMethodIsAlwaysInverted")
        public boolean isInline() {
            return inline;
        }
    }

}
