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

import de.linusdev.lutils.http_WIP.body.BodyParser;
import de.linusdev.lutils.http_WIP.header.Header;
import de.linusdev.lutils.http_WIP.method.RequestMethod;
import de.linusdev.lutils.http_WIP.version.HTTPVersion;
import de.linusdev.lutils.http_WIP.version.HTTPVersions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public class HTTPRequest<B> {

    private final @NotNull RequestMethod method;
    private final @Nullable String path;
    private final @NotNull HTTPVersion version;

    private final @NotNull Map<String, Header> headers;

    private final @Nullable B body;

    public HTTPRequest(@NotNull RequestMethod method, @Nullable String path, @NotNull HTTPVersion version,
                       @NotNull Map<String, Header> headers, @Nullable B body) {
        this.method = method;
        this.path = path;
        this.version = version;
        this.headers = headers;
        this.body = body;
    }

    public static <B> @NotNull HTTPRequest<B> parse(@NotNull InputStream in, @NotNull BodyParser<B> parser) throws IOException {

        HTTPRequestReader reader = new HTTPRequestReader(in);

        String first = reader.readLine();
        String[] parts = first.split(" ");

        if(parts.length <= 1)
            throw new IllegalArgumentException("Malformed HTTP Request: first line: " + first);

        RequestMethod method = RequestMethod.of(parts[0]);
        String path = null;
        HTTPVersion version;
        Map<String, Header> headers = new HashMap<>();
        B body;

        if(parts.length == 3){
            path = parts[1];
            version = HTTPVersions.of(parts[2]);
        } else {
            version = HTTPVersions.of(parts[1]);
        }

        String line;
        while((line = reader.readLine()) != null) {
            if(line.isEmpty()) { //end of headers
                break;
            }
            Header header = Header.of(line);
            headers.put(header.getKey(), header);
        }

        body = parser.parse(reader.getInputStreamForRemaining());

        return new HTTPRequest<>(method, path, version, headers, body);
    }

    public static @NotNull HTTPRequest<Void> parse(@NotNull InputStream in) throws IOException {
        return parse(in, in1 -> null);
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
    public @NotNull Map<String, Header> getHeaders() {
        return Collections.unmodifiableMap(headers);
    }

    /**
     * {@link B Body} of this request.
     * @return {@link B Body} or {@code null}, if the {@link BodyParser} returned {@code null}.
     */
    public @Nullable B getBody() {
        return body;
    }
}
