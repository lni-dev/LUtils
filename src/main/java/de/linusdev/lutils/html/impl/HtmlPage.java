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
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

/**
 * @see HtmlObjectType#PAGE
 */
public class HtmlPage implements HtmlObject {

    public static final @NotNull HtmlObjectParser<HtmlPage> PARSER = (parser, reader) -> {
        HtmlObject object;
        List<HtmlObject> content = new ArrayList<>();

        while ((object = parser.parseIfPresent(reader)) != null) {
            content.add(object);
        }

        return new HtmlPage(content);
    };

    private final @NotNull List<HtmlObject> content;

    public HtmlPage(@NotNull List<HtmlObject> content) {
        this.content = content;
    }

    public @NotNull List<@NotNull HtmlObject> content() {
        return content;
    }

    public void write(@NotNull HtmlParserState state, @NotNull Writer writer) throws IOException {
        for (HtmlObject object : content) {
            writer.write(state.getIndent());
            object.write(state, writer);
            writer.append("\n");
        }
    }

    @Override
    public @NotNull HtmlObjectType type() {
        return HtmlObjectType.PAGE;
    }
}
