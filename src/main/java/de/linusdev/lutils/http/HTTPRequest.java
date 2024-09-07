/*
 * Copyright (c) 2023-2024 Linus Andera
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

import de.linusdev.lutils.http.body.BodyParser;
import de.linusdev.lutils.http.header.Header;
import de.linusdev.lutils.http.header.HeaderMap;
import de.linusdev.lutils.http.method.RequestMethod;
import de.linusdev.lutils.http.version.HTTPVersion;
import de.linusdev.lutils.http.version.HTTPVersions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;

public class HTTPRequest<B> {

    private final @NotNull RequestMethod method;
    private final @Nullable String path;
    private final @NotNull HTTPVersion version;

    private final @NotNull HeaderMap headers;

    private final @Nullable B body;

    public HTTPRequest(@NotNull RequestMethod method, @Nullable String path, @NotNull HTTPVersion version,
                       @NotNull HeaderMap headers, @Nullable B body) {
        this.method = method;
        this.path = path;
        this.version = version;
        this.headers = headers;
        this.body = body;
    }

    public static <B> @NotNull HTTPRequest<B> parse(@NotNull InputStream in, @NotNull BodyParser<B> parser) throws IOException {
        HTTPRequestReader reader = new HTTPRequestReader(in);
        HTTPRequestReader.LineReader lineReader = reader.getLineReader();

        final RequestMethod method;
        final String path;
        final HTTPVersion version;
        final HeaderMap headers = new HeaderMap();
        final B body;

        // Read request method
        method = RequestMethod.of(lineReader.readUntil(' '));

        if(lineReader.eol)
            throw new IllegalArgumentException("Malformed HTTP request. Missing HTTP version.");

        // Read path and version
        String pathOrVersion = lineReader.readUntil(' ');
        if(lineReader.eol) {
            path = null;
            version = HTTPVersions.of(pathOrVersion);
        } else {
            path = pathOrVersion;
            version = HTTPVersions.of(lineReader.readUntilLineFeed());
        }

        String key;
        String value;
        while(!lineReader.eof) {
            key = lineReader.readUntil(':');

            if(key.isEmpty()) //end of headers
                break;
            if(lineReader.eol)
                throw new IllegalArgumentException("Malformed HTTP request. Header line: " + key);

            value = lineReader.readUntilLineFeed().stripLeading();

            Header header = Header.of(key, value);
            headers.put(key, header);
        }

        body = parser.parse(headers, reader.getInputStreamForRemaining());

        return new HTTPRequest<>(method, path, version, headers, body);
    }

    public static @NotNull HTTPRequest<Void> parse(@NotNull InputStream in) throws IOException {
        return parse(in, (hs, in1) -> null);
    }

    public static @NotNull HTTPRequestBuilder builder() {
        return new HTTPRequestBuilder();
    }


        /**
         * {@link RequestMethod} of this request.
         * @return {@link RequestMethod}
         */
    public @NotNull RequestMethod getMethod() {
        return method;
    }

    /**
     * Requested path
     * @return requested path as {@link String}.
     */
    public @Nullable String getPath() {
        return path;
    }

    /**
     * {@link HTTPVersion} of this request.
     * @return {@link HTTPVersion}
     */
    public @NotNull HTTPVersion getVersion() {
        return version;
    }

    /**
     * Map of {@link Header headers} of this request.
     * @return Map of {@link Header headers}
     */
    public @NotNull HeaderMap getHeaders() {
        return headers;
    }

    /**
     * {@link B Body} of this request.
     * @return {@link B Body} or {@code null}, if the {@link BodyParser} returned {@code null}.
     */
    public @Nullable B getBody() {
        return body;
    }
}
