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

package de.linusdev.lutils.http_WIP;

import de.linusdev.lutils.http_WIP.body.BodySupplier;
import de.linusdev.lutils.http_WIP.header.Header;
import de.linusdev.lutils.http_WIP.method.Methods;
import de.linusdev.lutils.http_WIP.method.RequestMethod;
import de.linusdev.lutils.http_WIP.version.HTTPVersion;
import de.linusdev.lutils.http_WIP.version.HTTPVersions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public class HTTPRequestBuilder {

    public static final Charset CHARSET = StandardCharsets.UTF_8;

    public static final byte[] LINE_SEPARATOR = "\r\n".getBytes(CHARSET);
    public static final byte SPACE = 0x20;

    private @NotNull RequestMethod method;
    private @Nullable String path;
    private @NotNull HTTPVersion version;
    private @NotNull Map<@NotNull String, @NotNull Header> headers;
    private @Nullable BodySupplier supplier;

    public HTTPRequestBuilder() {
        this.headers = new HashMap<>();
        this.version = HTTPVersions.HTTP_1_1;
        this.method = Methods.GET;
    }

    public void setMethod(@NotNull RequestMethod method) {
        this.method = method;
    }

    public void setPath(@Nullable String path) {
        this.path = path;
    }

    public void setVersion(@NotNull HTTPVersion version) {
        this.version = version;
    }

    public void setBodySupplier(@Nullable BodySupplier supplier) {
        this.supplier = supplier;
    }

    public void setHeader(@NotNull String key, @Nullable String value) {
        if(value == null)
            headers.remove(key);
        else
            headers.put(key, Header.of(key, value));
    }

    public void setHeaders(@NotNull Map<@NotNull String, @NotNull Header> headers) {
        this.headers = headers;
    }

    public void GET(@Nullable String path) {
        setMethod(Methods.GET);
        setPath(path);
    }

    public void POST(@Nullable String path, @NotNull BodySupplier supplier) {
        setMethod(Methods.POST);
        setPath(path);
        setBodySupplier(supplier);
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

        if(supplier != null ) {
            byte[] buffer = supplier.length() == -1 ? new byte[maxBufferSize] : new byte[Math.min(supplier.length(), maxBufferSize)];
            try (InputStream body = supplier.stream()) {

                int len;
                while ((len = body.read(buffer)) != -1) {
                    stream.write(buffer, 0, len);
                }
            }
        }
    }
}
