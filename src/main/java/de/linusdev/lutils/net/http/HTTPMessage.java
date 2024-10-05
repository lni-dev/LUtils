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
import de.linusdev.lutils.net.http.header.Header;
import de.linusdev.lutils.net.http.header.HeaderMap;
import de.linusdev.lutils.net.http.version.HTTPVersion;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

public abstract class HTTPMessage<B> {

    protected static HeaderMap parseHeaders(@NotNull HTTPMessageReader reader) throws IOException {
        HeaderMap headers = new HeaderMap();
        HTTPMessageReader.LineReader lineReader = reader.getLineReader();

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

        return headers;
    }

    protected final @NotNull HTTPVersion version;
    protected final @NotNull HeaderMap headers;
    protected final @Nullable B body;

    protected HTTPMessage(@NotNull HTTPVersion version, @NotNull HeaderMap headers, @Nullable B body) {
        this.version = version;
        this.headers = headers;
        this.body = body;
    }

    /**
     * {@link HTTPVersion} of this message.
     * @return {@link HTTPVersion}
     */
    public @NotNull HTTPVersion getVersion() {
        return version;
    }

    /**
     * Map of {@link Header headers} of this message.
     * @return Map of {@link Header headers}
     */
    public @NotNull HeaderMap getHeaders() {
        return headers;
    }

    /**
     * {@link B Body} of this message.
     * @return {@link B Body} or {@code null}, if the {@link BodyParser} returned {@code null}.
     */
    public @Nullable B getBody() {
        return body;
    }

}
