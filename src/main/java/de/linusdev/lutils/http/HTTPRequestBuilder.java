/*
 * Copyright (c) 2023 Linus Andera
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

package de.linusdev.lutils.http;

import de.linusdev.lutils.http.body.Body;
import de.linusdev.lutils.http.header.Header;
import de.linusdev.lutils.http.header.HeaderMap;
import de.linusdev.lutils.http.header.HeaderName;
import de.linusdev.lutils.http.header.value.HeaderValue;
import de.linusdev.lutils.http.method.Methods;
import de.linusdev.lutils.http.method.RequestMethod;
import de.linusdev.lutils.http.version.HTTPVersion;
import de.linusdev.lutils.http.version.HTTPVersions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;


@SuppressWarnings("UnusedReturnValue")
public class HTTPRequestBuilder {

    public static final Charset CHARSET = StandardCharsets.UTF_8;

    public static final byte[] LINE_SEPARATOR = "\r\n".getBytes(CHARSET);
    public static final byte SPACE = 0x20;

    private @NotNull RequestMethod method;
    private @Nullable String path;
    private @NotNull HTTPVersion version;
    private @NotNull HeaderMap headers;
    private @Nullable Body body;

    public HTTPRequestBuilder() {
        this.headers = new HeaderMap();
        this.version = HTTPVersions.HTTP_1_1;
        this.method = Methods.GET;
    }

    public HTTPRequestBuilder setMethod(@NotNull RequestMethod method) {
        this.method = method;
        return this;
    }

    public HTTPRequestBuilder setPath(@Nullable String path) {
        this.path = path;
        return this;
    }

    public HTTPRequestBuilder setVersion(@NotNull HTTPVersion version) {
        this.version = version;
        return this;
    }

    public HTTPRequestBuilder setBody(@Nullable Body body) {
        if(this.body != null)
            this.body.removeHeaders(headers);

        this.body = body;

        if(this.body != null)
            this.body.adjustHeaders(this.headers);

        return this;
    }

    public HTTPRequestBuilder setHeader(@NotNull String key, @Nullable String value) {
        if(value == null)
            headers.remove(key);
        else
            headers.put(key, Header.of(key, value));

        return this;
    }

    public HTTPRequestBuilder setHeader(@NotNull Header header) {
        headers.put(header);
        return this;
    }

    public HTTPRequestBuilder setHeader(@NotNull HeaderName name, @Nullable String value) {
        if(value == null)
            headers.remove(name.getName());
        else
            headers.put(name.with(value));

        return this;
    }

    public HTTPRequestBuilder setHeader(@NotNull HeaderName name, @Nullable HeaderValue value) {
        if(value == null)
            headers.remove(name.getName());
        else
            headers.put(name.with(value));

        return this;
    }

    public HTTPRequestBuilder setHeaders(@NotNull HeaderMap headers) {
        this.headers = headers;
        return this;
    }

    public HTTPRequestBuilder GET(@Nullable String path) {
        setMethod(Methods.GET);
        setPath(path);
        return this;
    }

    public HTTPRequestBuilder POST(@Nullable String path, @NotNull Body body) {
        setMethod(Methods.POST);
        setPath(path);
        setBody(body);
        return this;
    }

    /**
     * build as string.
     * @see #build()
     */
    public String build() throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        build(stream);
        return stream.toString(StandardCharsets.UTF_8);
    }

    /**
     * {@link #build(OutputStream, int)} with {@code maxBufferSize} set to {@code 2048}
     * @see #build()
     */
    public void build(@NotNull OutputStream stream) throws IOException {
        build(stream, 2048);
    }

    public void build(@NotNull OutputStream stream, int maxBufferSize) throws IOException {
        stream.write(method.getName().getBytes(CHARSET));
        stream.write(SPACE);
        if(path != null) {
            stream.write(path.getBytes(CHARSET));
            stream.write(SPACE);
        }
        stream.write(version.asString().getBytes(CHARSET));
        stream.write(LINE_SEPARATOR);

        for(Header header : headers.values()) {
            stream.write(header.asString().getBytes(CHARSET));
            stream.write(LINE_SEPARATOR);
        }

        stream.write(LINE_SEPARATOR);

        if(body != null) {
            byte[] buffer = body.length() == -1 ? new byte[maxBufferSize] : new byte[Math.min(body.length(), maxBufferSize)];
            try (InputStream bodyStream = body.stream()) {

                int len;
                while ((len = bodyStream.read(buffer)) != -1) {
                    stream.write(buffer, 0, len);
                }
            }
        }
    }
}
