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

import de.linusdev.lutils.html.*;
import de.linusdev.lutils.html.lhtml.skeleton.LhtmlTemplateSkeleton;
import de.linusdev.lutils.html.parser.HtmlWritingState;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * A normal template element.
 */
public class LhtmlTemplateElement implements EditableHtmlElement, LhtmlTemplate {

    protected final @NotNull String id;
    protected final @NotNull Map<String, LhtmlPlaceholder> placeholders;
    protected final @NotNull HashMap<String, LhtmlTemplateSkeleton> templates;
    protected final @NotNull Map<String, String> replaceValues;

    protected final @NotNull EditableHtmlElement actual;

    public LhtmlTemplateElement(
            @NotNull String id,
            @NotNull EditableHtmlElement actual,
            @NotNull Map<String, LhtmlPlaceholder> placeholders,
            @NotNull HashMap<String, LhtmlTemplateSkeleton> templates,
            @NotNull Map<String, String> replaceValues
    ) {
        this.id = id;
        this.placeholders = placeholders;
        this.actual = actual;
        this.templates = templates;
        this.replaceValues = replaceValues;
    }


    @Override
    public @NotNull HtmlElementType<?> tag() {
        return actual.tag();
    }

    @Override
    public @NotNull List<@NotNull HtmlObject> content() {
        return actual.content();
    }

    @Override
    public @NotNull HtmlAttributeMap attributes() {
        return actual.attributes();
    }

    @Override
    public @NotNull HtmlObjectType type() {
        return HtmlObjectType.ELEMENT;
    }

    @Override
    public @NotNull LhtmlTemplateElement copy() {
        EditableHtmlElement copy = actual.copy();
        Map<String, LhtmlPlaceholder> placeholders = new HashMap<>(this.placeholders.size());
        Map<String, String> replaceValues = new HashMap<>();

        Consumer<HtmlObject> consumer = object -> {
            if(object.type() == HtmlObjectType.ELEMENT) {
                object.asHtmlElement().iterateAttributes(attribute -> {
                    if(attribute instanceof LhtmlPlaceholderAttribute placeholderAttr) {
                        placeholderAttr.setReplaceValues(replaceValues);
                    }
                });

                if(object instanceof LhtmlPlaceholderElement placeholderEle) {
                    LhtmlPlaceholder holder = placeholders.computeIfAbsent(placeholderEle.getId(), LhtmlPlaceholder::new);
                    holder.addPlaceholderElement(placeholderEle);
                }
            }
        };

        copy.iterateContentRecursive(consumer);

        return new LhtmlTemplateElement(id, copy, placeholders, templates, replaceValues);
    }

    @Override
    public @NotNull String getId() {
        return id;
    }

    @Override
    public @NotNull HtmlAddable getPlaceholder(@NotNull String id) {
        return Objects.requireNonNull(placeholders.get(id), "No template found with id '" + id + "'.");
    }

    @Override
    public @NotNull LhtmlTemplateElement getTemplate(@NotNull String id) {
        return Objects.requireNonNull(templates.get(id)).copy();
    }

    @Override
    public void setValue(@NotNull String key, @NotNull String value) {
        replaceValues.put(key, value);
    }

    @Override
    public void write(@NotNull HtmlWritingState state, @NotNull Writer writer) throws IOException {
        actual.write(state, writer);
    }
}
