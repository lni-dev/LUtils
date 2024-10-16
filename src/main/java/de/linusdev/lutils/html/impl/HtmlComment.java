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
import de.linusdev.lutils.html.parser.HtmlParserState;
import de.linusdev.lutils.html.parser.ParseException;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Writer;

/**
 * Html comment with the following syntax: {@code <!--comment-->}.
 */
public class HtmlComment implements HtmlObject {

    public static final @NotNull HtmlObjectParser<HtmlComment> PARSER =
            (parser, reader) -> {
                char c;

                if((c = reader.read()) != '<') throw new ParseException(c);
                if((c = reader.read()) != '!') throw new ParseException(c);
                if((c = reader.read()) != '-') throw new ParseException(c);
                if((c = reader.read()) != '-') throw new ParseException(c);

                StringBuilder comment = new StringBuilder();

                while (true) {
                    String text = reader.readUntil('>');

                    if(!text.endsWith("--")) {
                        comment.append(text);
                        comment.append('>');
                    } else {
                        break;
                    }
                }

                return new HtmlComment(comment.toString());
            };


    private final @NotNull String text;

    public HtmlComment(@NotNull String text) {
        this.text = text;
    }

    @Override
    public @NotNull HtmlObjectType type() {
        return HtmlObjectType.TEXT;
    }

    @Override
    public void write(@NotNull HtmlParserState state, @NotNull Writer writer) throws IOException {
        writer.append("<!--").append(text).append("-->");
    }
}
