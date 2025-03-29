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

package de.linusdev.lutils.net.http.header;

import de.linusdev.lutils.net.http.header.value.BasicHeaderValue;
import de.linusdev.lutils.net.http.header.value.HeaderValueParser;
import org.jetbrains.annotations.NotNull;

public interface Header {

    /**
     * Create new {@link Header}.
     * @param header header string with the following format: "key: value"
     * @return new {@link Header}.
     */
    static @NotNull Header of(@NotNull String header) {
        int sep = header.indexOf(':');
        return new HeaderImpl(header.substring(0, sep), header.substring(sep + 2));
    }

    /**
     * Create new {@link Header}.
     * @param key header key
     * @param value header value
     * @return new {@link Header}
     */
    static @NotNull Header of(@NotNull String key, @NotNull String value) {
        return new HeaderImpl(key, value);
    }

    /**
     * Key of this {@link Header}.
     * @return key as {@link String}
     */
    @NotNull String getKey();

    /**
     * Value of this {@link Header}.
     * @return value as {@link String}
     */
    @NotNull String getValue();

    /**
     * parse this headers value using given {@code parser}
     * @param parser {@link HeaderValueParser}
     * @return parsed header value
     * @param <T> class to parse to
     * @see BasicHeaderValue#PARSER
     */
    default @NotNull <T> T parseValue(@NotNull HeaderValueParser<T> parser) {
        return parser.parse(this);
    }

    /**
     * This header as {@link String}.
     * @return this header
     */
    default @NotNull String asString() {
        return getKey() + ": " + getValue();
    }

    /**
     * Checks {@link #getKey()} and {@link #getValue()} of both headers and ignores case.
     * @param header {@link Header} to compare
     * @param other object to check if it represents the same {@link Header} as given {@code header}.
     * @return {@code true} if both headers have the same {@link #getKey() key} and {@link #getValue() value} (ignores case)
     */
    static boolean equals(Header header, Object other) {
        if (header == other)
            return true;
        if (!(other instanceof Header otherHeader))
            return false;

        return header.getKey().equalsIgnoreCase(otherHeader.getKey())
                && header.getValue().equalsIgnoreCase(otherHeader.getValue());
    }

    /**
     * Generate the hashcode for a {@link Header} implementation
     * @param header the {@link Header} to generate the hashcode for
     * @return hashcode
     */
    static int hashCode(Header header) {
        int result = header.getKey().toLowerCase().hashCode();
        result = 31 * result + header.getValue().toLowerCase().hashCode();
        return result;
    }
}
