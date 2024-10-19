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

package de.linusdev.lutils.html.impl;

import de.linusdev.lutils.html.HtmlObject;
import de.linusdev.lutils.html.HtmlObjectParser;
import de.linusdev.lutils.html.HtmlObjectType;
import de.linusdev.lutils.html.parser.HtmlWritingState;
import de.linusdev.lutils.result.BiResult;
import org.jetbrains.annotations.NotNull;

import java.io.EOFException;
import java.io.IOException;
import java.io.Writer;

public class HtmlText implements HtmlObject {

    public static final @NotNull HtmlObjectParser<HtmlText> PARSER =
            (state, reader) -> {
                StringBuilder text = new StringBuilder();

                BiResult<String, Character> res = reader.readEscapedUntil('<', '\n');
                text.append(res.result1());

                while(res.result2() != '<') {

                    if(!reader.availableSkipNewLinesAndSpaces())
                        throw new EOFException();

                    res = reader.readEscapedUntil('<', '\n');

                    if(!res.result1().isEmpty()) {
                        text.append(' ');
                        text.append(res.result1());
                    }

                }

                reader.pushBack('<');
                return new HtmlText(text.toString());

            };


    private final @NotNull String text;

    public HtmlText(@NotNull String text) {
        this.text = text;
    }

    @Override
    public @NotNull HtmlObjectType type() {
        return HtmlObjectType.TEXT;
    }

    @Override
    public void write(@NotNull HtmlWritingState state, @NotNull Writer writer) throws IOException {
        //TODO: escape text
        String processed = text.replaceAll("\n", "\n" + state.getIndent());
        writer.write(processed);
    }
}
