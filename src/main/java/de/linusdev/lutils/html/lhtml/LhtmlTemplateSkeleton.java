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
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public class LhtmlTemplateSkeleton {

    protected final @NotNull String id;
    protected final @NotNull HashMap<String, LhtmlTemplateSkeleton> templates;

    protected final @NotNull EditableHtmlElement actual;

    public LhtmlTemplateSkeleton(
            @NotNull String id,
            @NotNull EditableHtmlElement actual,
            @NotNull HashMap<String, LhtmlTemplateSkeleton> templates
    ) {
        this.id = id;
        this.templates = templates;
        this.actual = actual;
    }

    public @NotNull LhtmlTemplateElement copy() {
        EditableHtmlElement copy = actual.copy();
        Map<String, LhtmlPlaceholder> placeholders = new HashMap<>();
        Map<String, String> replaceValues = new HashMap<>();

        Consumer<HtmlObject> consumer = object -> {
            if(object.type() == HtmlObjectType.ELEMENT) {
                object.asHtmlElement().iterateAttributes(attribute -> {
                    if(attribute instanceof LhtmlPlaceholderAttribute placeholderAttr) {
                        placeholderAttr.setValues(replaceValues);
                    }
                });

                if(object instanceof LhtmlPlaceholderElement placeholderEle) {
                    LhtmlPlaceholder holder = placeholders.computeIfAbsent(placeholderEle.getId(), s -> new LhtmlPlaceholder());
                    holder.addPlaceholderElement(placeholderEle);
                }
            }
        };

        copy.iterateContentRecursive(consumer);

        return new LhtmlTemplateElement(id, copy, placeholders, templates, replaceValues);
    }

    public @NotNull String getId() {
        return id;
    }

    public @NotNull LhtmlTemplateElement getTemplate(@NotNull String id) {
        return Objects.requireNonNull(templates.get(id)).copy();
    }

}
