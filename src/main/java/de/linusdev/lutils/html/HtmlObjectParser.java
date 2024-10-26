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

package de.linusdev.lutils.html;

import de.linusdev.lutils.html.parser.HtmlParserState;
import de.linusdev.lutils.html.parser.HtmlReader;
import de.linusdev.lutils.html.parser.ParseException;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * - Any object parser must ignore all spaces after a line feed. A chunk of spaces not preceded by a line feed must not be ignored.<br>
 * - Line feed character may be converted to a single space while parsing<br>
 * - Line feed character may also be removed while parsing<br>
 */
public interface HtmlObjectParser<O extends HtmlObject> {

    /**
     * Parse given {@code reader} to a {@link O}.
     * @param state state to use while parsing
     * @param reader reader to read from
     * @return parsed {@link O}
     * @throws IOException while reading
     * @throws ParseException while parsing
     */
    @NotNull O parse(@NotNull HtmlParserState state, @NotNull HtmlReader reader) throws IOException, ParseException;

}
