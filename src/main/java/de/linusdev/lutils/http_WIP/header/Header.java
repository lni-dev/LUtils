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

package de.linusdev.lutils.http_WIP.header;

import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
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
     * This header as {@link String}.
     * @return this header
     */
    default @NotNull String asString() {
        return getKey() + ": " + getValue();
    }
}
