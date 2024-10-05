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

package de.linusdev.lutils.net.http.status;

import de.linusdev.lutils.other.UnknownConstantException;
import org.jetbrains.annotations.NotNull;

public enum ResponseStatusCodeType {

    INFORMATION     (100, 199),
    SUCCESSFUL      (200, 299),
    REDIRECT        (300, 399),
    CLIENT_ERROR    (400, 499),
    SERVER_ERROR    (500, 599),
    ;

    public static @NotNull ResponseStatusCodeType of(int statusCode) {
        if(INFORMATION.isInRange(statusCode))
            return INFORMATION;
        if(SUCCESSFUL.isInRange(statusCode))
            return SUCCESSFUL;
        if(REDIRECT.isInRange(statusCode))
            return REDIRECT;
        if(CLIENT_ERROR.isInRange(statusCode))
            return CLIENT_ERROR;
        if(SERVER_ERROR.isInRange(statusCode))
            return SERVER_ERROR;

        throw new UnknownConstantException(statusCode);
    }

    /**
     * Start of status code range, inclusive.
     */
    private final int statusCodeRangeStart;
    /**
     * End of status code range, inclusive.
     */
    private final int statusCodeRangeEnd;

    ResponseStatusCodeType(int statusCodeRangeStart, int statusCodeRangeEnd) {
        this.statusCodeRangeStart = statusCodeRangeStart;
        this.statusCodeRangeEnd = statusCodeRangeEnd;
    }

    /**
     * returns {@code true} if given {@code statusCode} is in the range of this type.
     */
    public boolean isInRange(int statusCode) {
        return statusCode >= statusCodeRangeStart && statusCode <= statusCodeRangeEnd;
    }
}
