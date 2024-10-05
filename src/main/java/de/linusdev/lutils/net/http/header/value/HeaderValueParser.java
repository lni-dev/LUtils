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

package de.linusdev.lutils.net.http.header.value;

import de.linusdev.lutils.net.http.header.Header;
import de.linusdev.lutils.net.http.header.HeaderName;
import org.jetbrains.annotations.NotNull;

public interface HeaderValueParser<T> {

    /**
     * Parses given {@code header} to a header-value of type {@link T}
     * @param header {@link Header} who's value should be parsed
     * @return {@link T}
     */
    @NotNull T parse(@NotNull Header header);

    /**
     * Parse given header-value of type {@link T} to a header-ready string.
     * @param value header-value of type {@link T}
     * @return header-ready string representing {@link T}
     */
    @NotNull String parse(@NotNull T value);

    /**
     * Parses given header-value of type {@link T} to a {@link Header}.
     * @param name {@link HeaderName}
     * @param value header-value of type {@link T}
     * @return {@link Header} with given {@code name} and {@code value}
     */
    default @NotNull Header parse(@NotNull HeaderName name, @NotNull T value) {
        return name.with(parse(value));
    }
}
