/*
 * Copyright (c) 2024-2025 Linus Andera
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

import de.linusdev.lutils.html.HasHtmlContent;
import de.linusdev.lutils.html.HtmlObject;
import de.linusdev.lutils.html.HtmlObjectParser;
import de.linusdev.lutils.html.HtmlObjectType;
import de.linusdev.lutils.html.parser.HtmlWritingState;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

/**
 * @see HtmlObjectType#PAGE
 */
@SuppressWarnings("ClassCanBeRecord")
public class HtmlPage implements HtmlObject, HasHtmlContent {

    public static final @NotNull HtmlObjectParser<HtmlPage> PARSER = (state, reader) -> {
        HtmlObject object;
        List<HtmlObject> content = new ArrayList<>();

        while ((object = state.getParser().parseIfPresent(state, reader)) != null) {
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

    public void write(@NotNull HtmlWritingState state, @NotNull Writer writer) throws IOException {
        boolean first = true;
        for (HtmlObject object : content) {
            if(first) first = false;
            else writer.append("\n");

            writer.write(state.getIndent());
            object.write(state, writer);
        }

    }

    @Override
    public @NotNull HtmlObjectType type() {
        return HtmlObjectType.PAGE;
    }

    @Override
    public @NotNull HtmlPage copy() {
        List<HtmlObject> content = new ArrayList<>(this.content.size());
        this.content.forEach(object -> content.add(object.copy()));
        return new HtmlPage(content);
    }
}
