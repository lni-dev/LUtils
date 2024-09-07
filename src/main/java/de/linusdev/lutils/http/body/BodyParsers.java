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
import de.linusdev.lutils.http.header.HeaderNames;
import de.linusdev.lutils.http.header.value.BasicHeaderValue;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class BodyParsers {

    private BodyParsers() {

    }

    @Contract(value = " -> new", pure = true)
    public static @NotNull BodyParser<String> newStringBodyParser() {
        return new BodyParser<>() {
            @Override
            public @NotNull String parse(@NotNull HeaderMap headers, @NotNull InputStream in) throws IOException {
                BasicHeaderValue contentType = headers.get(HeaderNames.CONTENT_TYPE).parseValue(BasicHeaderValue.PARSER);
                String charset = contentType.get("charset");

                if(charset == null)
                    charset = StandardCharsets.UTF_8.name();

                InputStreamReader inReader = new InputStreamReader(in, charset);

                BufferedReader reader = new BufferedReader(inReader);
                StringBuilder sb = new StringBuilder();

                String line;
                while((line = reader.readLine()) != null)
                    sb.append(line);

                return sb.toString();
            }
        };
    }
}
