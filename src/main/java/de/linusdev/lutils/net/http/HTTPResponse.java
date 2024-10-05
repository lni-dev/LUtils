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

package de.linusdev.lutils.net.http;

import de.linusdev.lutils.net.http.body.BodyParser;
import de.linusdev.lutils.net.http.header.HeaderMap;
import de.linusdev.lutils.net.http.status.ResponseStatusCode;
import de.linusdev.lutils.net.http.version.HTTPVersion;
import de.linusdev.lutils.net.http.version.HTTPVersions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;

public class HTTPResponse<B> extends HTTPMessage<B> {

    public static <B> @NotNull HTTPResponse<B> parse(@NotNull InputStream in, @NotNull BodyParser<B> parser) throws IOException {
        HTTPMessageReader reader = new HTTPMessageReader(in);
        HTTPMessageReader.LineReader lineReader = reader.getLineReader();

        final HTTPVersion version;
        final ResponseStatusCode statusCode;
        final HeaderMap headers;
        final B body;

        // Read request method
        version = HTTPVersions.of(lineReader.readUntil(' '));

        if(lineReader.eol)
            throw new IllegalArgumentException("Malformed HTTP request. Missing status code.");

        String code = lineReader.readUntil(' ');
        String reasonPhrase;
        if(lineReader.eol) reasonPhrase = "";
        else reasonPhrase = lineReader.readUntilLineFeed();

        statusCode = ResponseStatusCode.of(code, reasonPhrase);

        headers = parseHeaders(reader);
        body = parser.parse(headers, reader.getInputStreamForRemaining());

        return new HTTPResponse<>(version, statusCode, headers, body);
    }

    public static @NotNull HTTPResponse<InputStream> parse(@NotNull InputStream in) throws IOException {
        return parse(in, (hs, in1) -> in1);
    }

    public static @NotNull HTTPMessageBuilder builder() {
        return new HTTPMessageBuilder();
    }

    private final @NotNull ResponseStatusCode statusCode;

    protected HTTPResponse(
            @NotNull HTTPVersion version, @NotNull ResponseStatusCode statusCode,
            @NotNull HeaderMap headers, @Nullable B body
    ) {
        super(version, headers, body);
        this.statusCode = statusCode;
    }

    public @NotNull ResponseStatusCode getStatusCode() {
        return statusCode;
    }
}
