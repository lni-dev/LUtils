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

package de.linusdev.lutils.http.body;

import de.linusdev.lutils.http.header.HeaderMap;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;

public class UnparsedBody {

    private final @NotNull HeaderMap headers;
    private final @NotNull InputStream inputStream;

    public UnparsedBody(@NotNull HeaderMap headers, @NotNull InputStream inputStream) {
        this.headers = headers;
        this.inputStream = inputStream;
    }

    public <B> B parseTo(@NotNull BodyParser<B> parser) throws IOException {
        return parser.parse(headers, inputStream);
    }

}
