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

package de.linusdev.lutils.http.status;

import org.jetbrains.annotations.NotNull;

import static de.linusdev.lutils.http.status.ResponseStatusCodeType.*;

/**
 * @see <a href="https://developer.mozilla.org/en-US/docs/Web/HTTP/Status">Mozilla Developer</a>
 */
public enum StatusCodes implements ResponseStatusCode {
    OK(200, "OK", SUCCESSFUL),
    CREATED(201, "Created", SUCCESSFUL),
    NO_CONTENT(204, "No Content", SUCCESSFUL),

    MOVED_PERMANENTLY(301, "Moved Permanently", REDIRECT),
    TEMPORARY_REDIRECT(307, "Temporary Redirect", REDIRECT),
    PERMANENT_REDIRECT(208, "Permanent Redirect", REDIRECT),

    BAD_REQUEST(400, "Bad Request", CLIENT_ERROR),
    UNAUTHORIZED(401, "Unauthorized", CLIENT_ERROR),
    FORBIDDEN(403, "Forbidden", CLIENT_ERROR),
    NOT_FOUND(404, "Not Found", CLIENT_ERROR),
    METHOD_NOT_ALLOWED(405, "Method Not Allowed", CLIENT_ERROR),

    INTERNAL_SERVER_ERROR(500, "Internal Server Error", SERVER_ERROR),
    NOT_IMPLEMENTED(501, "Not Implemented", SERVER_ERROR),
    BAD_GATEWAY(502, "Bad Gateway", SERVER_ERROR),

    ;

    static {
        for (StatusCodes statusCode : StatusCodes.values())
            STATUS_CODES.put(statusCode.getStatusCode(), statusCode);
    }

    private final @NotNull String name;
    private final int statusCode;
    private final @NotNull ResponseStatusCodeType type;

    StatusCodes(int statusCode, @NotNull String name,  @NotNull ResponseStatusCodeType type) {
        this.name = name;
        this.statusCode = statusCode;
        this.type = type;
    }

    @Override
    public @NotNull ResponseStatusCodeType getType() {
        return type;
    }

    @Override
    public int getStatusCode() {
        return statusCode;
    }

    @Override
    public @NotNull String getName() {
        return name;
    }
}
