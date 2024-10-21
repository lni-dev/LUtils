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

package de.linusdev.lutils.html.lhtml;

import de.linusdev.lutils.html.EditableHtmlElement;
import de.linusdev.lutils.html.HtmlObject;
import de.linusdev.lutils.html.HtmlObjectType;
import de.linusdev.lutils.html.parser.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class LhtmlPage implements HtmlObject, LhtmlElement {

    public static @NotNull LhtmlPage parse(@NotNull HtmlParser parser, @NotNull Reader reader) throws IOException, ParseException {
        LhtmlInjector injector = new LhtmlInjector();
        HtmlParserState state = new HtmlParserState(injector, parser);
        HtmlReader htmlReader = new HtmlReader(reader);

        HtmlObject object;
        List<HtmlObject> content = new ArrayList<>();

        while ((object = state.getParser().parseIfPresent(state, htmlReader)) != null) {
            content.add(object);
        }

        return new LhtmlPage(content, injector.getPlaceholders(), injector.getTemplates());
    }

    private final @NotNull List<HtmlObject> content;
    private final @NotNull HashMap<String, LhtmlPlaceholder> placeholders;
    private final @NotNull HashMap<String, LhtmlTemplate> templates;

    public LhtmlPage(
            @NotNull List<HtmlObject> content,
            @NotNull HashMap<String, LhtmlPlaceholder> placeholders,
            @NotNull HashMap<String, LhtmlTemplate> templates
    ) {
        this.content = content;
        this.placeholders = placeholders;
        this.templates = templates;
    }

    public @NotNull EditableHtmlElement getPlaceholder(@NotNull String id) {
        return Objects.requireNonNull(placeholders.get(id));
    }

    public @NotNull LhtmlTemplate getTemplate(@NotNull String id) {
        return Objects.requireNonNull(templates.get(id));
    }

    @Override
    public @NotNull HtmlObjectType type() {
        return HtmlObjectType.PAGE;
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public @NotNull LhtmlPage clone() {
        List<@NotNull HtmlObject> content = new ArrayList<>(this.content.size());
        HashMap<String, LhtmlPlaceholder> placeholders = new HashMap<>(this.placeholders.size());

        this.content.forEach(object -> {
            if(object instanceof LhtmlPlaceholder placeholder) {
                LhtmlPlaceholder clone = placeholder.clone();
                content.add(clone);
                placeholders.put(clone.getId(), clone);
            } else {
                content.add(object.clone());
            }
        });

        return new LhtmlPage(content, placeholders, templates);
    }

    @Override
    public void write(@NotNull HtmlWritingState state, @NotNull Writer writer) throws IOException {
        boolean first = true;
        for (HtmlObject object : content) {
            if(first) first = false;
            else writer.append("\n");

            writer.write(state.getIndent());
            object.write(state, writer);
        }
    }
}
