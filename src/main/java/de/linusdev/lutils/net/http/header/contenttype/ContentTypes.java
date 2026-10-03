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

/**
 * Common HTTP Content-Type values.
 *
 * <p>This class provides convenient representations of frequently used
 * MIME types, grouped by their top-level media type.</p>
 */
public class ContentTypes extends BasicHeaderValueImpl implements ContentType {

    private final @NotNull String type;
    private final @NotNull String subtype;

    ContentTypes(@NotNull String type, @NotNull String subType) {
        super(type + "/" + subType);
        this.type = type;
        this.subtype = subType;
    }

    /**
     * Returns the top-level media type.
     * @return the media type
     */
    @Override
    public @NotNull String type() {
        return type;
    }

    /**
     * Returns the media subtype.
     * @return the media subtype
     */
    @Override
    public @NotNull String subtype() {
        return subtype;
    }

    /**
     * Content types in the {@code text/*} family.
     */
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

    /**
     * Content types in the {@code image/*} family.
     */
    public static class Image extends ContentTypes implements BasicHeaderValue {

        @Contract(" -> new")
        public static @NotNull Image png() {
            return new Image("png");
        }

        protected Image(@NotNull String name) {
            super("image", name);
        }
    }

    /**
     * Content types in the {@code application/*} family.
     */
    public static class Application extends ContentTypes implements BasicHeaderValueWithCharset {

        /**
         * Returns {@code application/json}.
         *
         * @return the JSON content type
         */
        @Contract(" -> new")
        public static @NotNull Application json() {
            return new Application("json");
        }

        /**
         * Returns {@code application/xml}.
         *
         * @return the XML content type
         */
        @Contract(" -> new")
        public static @NotNull Application xml() {
            return new Application("xml");
        }

        /**
         * Returns {@code application/pdf}.
         *
         * @return the PDF content type
         */
        @Contract(" -> new")
        public static @NotNull Application pdf() {
            return new Application("pdf");
        }

        /**
         * Returns {@code application/zip}.
         *
         * @return the ZIP archive content type
         */
        @Contract(" -> new")
        public static @NotNull Application zip() {
            return new Application("zip");
        }

        /**
         * Returns {@code application/gzip}.
         *
         * @return the GZIP content type
         */
        @Contract(" -> new")
        public static @NotNull Application gzip() {
            return new Application("gzip");
        }

        /**
         * Returns {@code application/x-www-form-urlencoded}.
         *
         * @return the form URL-encoded content type
         */
        @Contract(" -> new")
        public static @NotNull Application formUrlEncoded() {
            return new Application("x-www-form-urlencoded");
        }

        /**
         * Returns {@code application/octet-stream}.
         *
         * @return the generic binary content type
         */
        @Contract(" -> new")
        public static @NotNull Application octetStream() {
            return new Application("octet-stream");
        }

        /**
         * Returns {@code application/wasm}.
         *
         * @return the WebAssembly content type
         */
        @Contract(" -> new")
        public static @NotNull Application wasm() {
            return new Application("wasm");
        }

        /**
         * Returns {@code application/rtf}.
         *
         * @return the Rich Text Format content type
         */
        @Contract(" -> new")
        public static @NotNull Application rtf() {
            return new Application("rtf");
        }

        @Override
        public @NotNull Application setCharset(@Nullable String charset) {
            BasicHeaderValueWithCharset.super.setCharset(charset);
            return this;
        }

        /**
         * Creates an application content type.
         *
         * @param name the media subtype
         */
        protected Application(@NotNull String name) {
            super("application", name);
        }
    }
}
