/*
 * Copyright (c) 2023-2025 Linus Andera
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

package de.linusdev.lutils.net.http;

import de.linusdev.lutils.net.http.body.BodyParser;
import de.linusdev.lutils.net.http.header.HeaderMap;
import de.linusdev.lutils.net.http.method.RequestMethod;
import de.linusdev.lutils.net.http.path.PathAndQuery;
import de.linusdev.lutils.net.http.version.HTTPVersion;
import de.linusdev.lutils.net.http.version.HTTPVersions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;

public class HTTPRequest<B> extends HTTPMessage<B> {

    private final @NotNull RequestMethod method;
    private final @Nullable String path;

    public HTTPRequest(
            @NotNull RequestMethod method, @Nullable String path, @NotNull HTTPVersion version,
            @NotNull HeaderMap headers, @Nullable B body
    ) {
        super(version, headers, body);
        this.method = method;
        this.path = path;
    }

    public static <B> @NotNull HTTPRequest<B> parse(@NotNull InputStream in, @NotNull BodyParser<B> parser) throws IOException {
        HTTPMessageReader reader = new HTTPMessageReader(in);
        HTTPMessageReader.LineReader lineReader = reader.getLineReader();

        final RequestMethod method;
        final String path;
        final HTTPVersion version;
        final HeaderMap headers;
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

        headers = parseHeaders(reader);
        body = parser.parse(headers, reader.getInputStreamForRemaining());

        return new HTTPRequest<>(method, path, version, headers, body);
    }

    public static @NotNull HTTPRequest<InputStream> parse(@NotNull InputStream in) throws IOException {
        return parse(in, (hs, in1) -> in1);
    }

    public static @NotNull HTTPMessageBuilder builder() {
        return new HTTPMessageBuilder();
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
     * Requested path as {@link PathAndQuery}.
     */
    public @Nullable PathAndQuery getPathAsPath() {
        return path == null ? null : new PathAndQuery(path);
    }

}
