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

package de.linusdev.lutils.net.http.body;

import de.linusdev.lutils.net.http.header.HeaderMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;

/**
 * Functional interface to parse an {@link InputStream} to custom body.
 * @param <B> body type
 */
@FunctionalInterface
public interface BodyParser<B> {

    /**
     * parses the body contained in given {@link InputStream} {@code in} to {@link B}.
     * @param in {@link InputStream} containing the http body.
     * @return parsed {@link B body}
     */
     @Nullable B parse(@NotNull HeaderMap headers, @NotNull InputStream in) throws IOException;

}
