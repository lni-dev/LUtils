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

package de.linusdev.lutils.net.http.body;

import de.linusdev.lutils.interfaces.TSupplier;
import de.linusdev.lutils.interfaces.Writable;
import de.linusdev.lutils.io.ResourceUtils;
import de.linusdev.lutils.net.http.header.contenttype.ContentType;
import de.linusdev.lutils.net.http.header.contenttype.ContentTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class Bodies {

    /**
     * Create a html body
     * @see Creator
     */
    public static @NotNull Creator html() {
        return new Creator(ContentTypes.Text.html());
    }

    /**
     * Create a css body
     * @see Creator
     */
    public static @NotNull Creator css() {
        return new Creator(ContentTypes.Text.css());
    }

    /**
     * Create a javascript body
     * @see Creator
     */
    public static @NotNull Creator javascript() {
        return new Creator(ContentTypes.Text.js());
    }

    /**
     * Create a string body
     * @see Creator
     */
    public static @NotNull Creator textUtf8() {
        return new Creator(ContentTypes.Text.plain().setCharset(StandardCharsets.UTF_8.name()));
    }

    /**
     * Create a png body
     * @see Creator
     */
    @SuppressWarnings("unused")
    public static @NotNull Creator png(){
        return new Creator(ContentTypes.Image.png());
    }

    /**
     * Created body of different entities.
     * <ul>
     *     <li>{@link #ofResource(Class, String)}: Create a body of a resource file.</li>
     * </ul>
     */
    public static class Creator {
        private final @NotNull ContentType contentType;

        public Creator(@NotNull ContentType contentType) {
            this.contentType = contentType;
        }

        /**
         * See {@link ResourceUtils#getURLConnectionOfResource(Class, String, boolean)}
         */
        public @NotNull Body ofResource(@Nullable Class<?> relClazz, @NotNull String path) {
            return new StreamURLConnectionBody(
                    ResourceUtils.getURLConnectionOfResource(relClazz, path, false), contentType
            );
        }

        /**
         * See {@link #ofResource(Class, String)} with {@code relClazz} being {@code null}.
         */
        @SuppressWarnings("unused")
        public @NotNull Body ofResource(@NotNull String path) {
            return ofResource(null, path);
        }

        /**
         *
         * @param pathToFile path to a {@link Files#isRegularFile(Path, LinkOption...) regular file}.
         * @return {@link Body} representing the contents of the file at given {@code pathToFile}
         */
        public @NotNull Body ofRegularFile(@NotNull Path pathToFile) {
            if(!Files.isRegularFile(pathToFile))
                throw new IllegalArgumentException("'" + pathToFile + "' is not a regular file.");

            try {
                return new InputStreamSupplierBody(
                        () -> Files.newInputStream(pathToFile, StandardOpenOption.READ),
                        contentType,
                        Files.size(pathToFile)
                );
            } catch (IOException e) {
               throw new IllegalStateException("Cannot read file size of file'" + pathToFile + "'", e);
            }
        }

        public @NotNull Body ofStringUtf8(@NotNull String string) {
            return new ByteArrayBody(string.getBytes(StandardCharsets.UTF_8), contentType);
        }

        @SuppressWarnings("unused")
        public @NotNull Body ofWritable(@NotNull Writable writable){
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try {
                writable.write(out);
            } catch (IOException ignored) {}
            return new ByteArrayBody(out.toByteArray(), contentType);
        }
    }

    private record InputStreamSupplierBody(
            @NotNull TSupplier<InputStream, IOException> supplier,
            @NotNull ContentType contentType,
            long contentLength
    ) implements Body {

        @Override
        public @NotNull ContentType contentType() {
            return contentType;
        }

        @Override
        public long length() {
            return contentLength;
        }

        @Override
        public @NotNull InputStream stream() throws IOException {
            return supplier.supply();
        }
    }

    private record StreamURLConnectionBody(
            @NotNull ResourceUtils.StreamURLConnection connection,
            @NotNull ContentType contentType
    ) implements Body {

        @Override
        public @NotNull ContentType contentType() {
            return contentType;
        }

        @Override
        public long length() {
            return connection.getContentLength();
        }

        @Override
        public @NotNull InputStream stream() throws IOException {
            return connection.openInputStream();
        }
    }

    private record ByteArrayBody(
            byte @NotNull [] bytes,
            @NotNull ContentType contentType
    ) implements Body {
        @Override
        public @NotNull ContentType contentType() {
            return contentType;
        }

        @Override
        public long length() {
            return bytes.length;
        }

        @Override
        public @NotNull InputStream stream() {
            return new ByteArrayInputStream(bytes);
        }
    }



}
