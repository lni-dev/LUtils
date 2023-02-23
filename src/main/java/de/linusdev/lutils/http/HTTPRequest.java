/*
 * Copyright (c) 2023 Linus Andera all rights reserved
 */

package de.linusdev.lutils.http;

import de.linusdev.lutils.http.body.BodyParser;
import de.linusdev.lutils.http.header.Header;
import de.linusdev.lutils.http.method.RequestMethod;
import de.linusdev.lutils.http.version.HTTPVersion;
import de.linusdev.lutils.http.version.HTTPVersions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
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

        InputStreamReader isr = new InputStreamReader(in, StandardCharsets.UTF_8);

        BufferedReader reader = new BufferedReader(isr);

        String first = reader.readLine();
        String[] parts = first.split(" ");

        if(parts.length <= 1)
            throw new IllegalArgumentException("Malformed HTTP Request");

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
            Header header = Header.of(line);

            headers.put(header.getKey(), header);

            if(line.isEmpty()) { //end of headers
                break;
            }
        }

        body = parser.parse(in);

        return new HTTPRequest<>(method, path, version, headers, body);
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
