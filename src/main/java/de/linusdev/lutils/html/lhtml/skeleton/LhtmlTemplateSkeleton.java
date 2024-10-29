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

package de.linusdev.lutils.html.lhtml.skeleton;

import de.linusdev.lutils.html.EditableHtmlElement;
import de.linusdev.lutils.html.HtmlAttribute;
import de.linusdev.lutils.html.HtmlObject;
import de.linusdev.lutils.html.HtmlObjectType;
import de.linusdev.lutils.html.lhtml.LhtmlPlaceholder;
import de.linusdev.lutils.html.lhtml.LhtmlPlaceholderAttribute;
import de.linusdev.lutils.html.lhtml.LhtmlPlaceholderElement;
import de.linusdev.lutils.html.lhtml.LhtmlTemplateElement;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * {@link LhtmlSkeleton} for a {@link LhtmlTemplateElement}.
 */
public class LhtmlTemplateSkeleton implements LhtmlSkeleton {

    @ApiStatus.Internal
    public static @NotNull LhtmlTemplateElement createCopy(
            @NotNull String id,
            @NotNull EditableHtmlElement actual,
            Map<String, LhtmlTemplateSkeleton> templates
    ) {
        EditableHtmlElement copy = actual.copy();
        Map<String, LhtmlPlaceholder> placeholders = new HashMap<>();
        Map<String, String> replaceValues = new HashMap<>();

        Consumer<HtmlAttribute> attrConsumer = attribute -> {
            if(attribute instanceof LhtmlPlaceholderAttribute placeholderAttr) {
                placeholderAttr.setReplaceValues(replaceValues);
            }
        };

        Consumer<HtmlObject> consumer = object -> {
            if(object.type() == HtmlObjectType.ELEMENT) {
                object.asHtmlElement().iterateAttributes(attrConsumer);

                if(object instanceof LhtmlPlaceholderElement placeholderEle) {
                    LhtmlPlaceholder holder = placeholders.computeIfAbsent(placeholderEle.getId(), LhtmlPlaceholder::new);
                    holder.addPlaceholderElement(placeholderEle);
                }
            }
        };

        copy.iterateContentRecursive(consumer);
        copy.iterateAttributes(attrConsumer);

        return new LhtmlTemplateElement(id, copy, placeholders, templates, replaceValues);
    }

    protected final @NotNull String id;
    protected final @NotNull Map<String, LhtmlTemplateSkeleton> templates;

    protected final @NotNull EditableHtmlElement actual;

    public LhtmlTemplateSkeleton(
            @NotNull String id,
            @NotNull EditableHtmlElement actual,
            @NotNull Map<String, LhtmlTemplateSkeleton> templates
    ) {
        this.id = id;
        this.templates = templates;
        this.actual = actual;
    }

    @Override
    public @NotNull LhtmlTemplateElement copy() {
        return createCopy(id, actual, templates);
    }

    @Override
    public @NotNull String getId() {
        return id;
    }

    @Override
    public @NotNull LhtmlTemplateElement getTemplate(@NotNull String id) {
        return Objects.requireNonNull(templates.get(id)).copy();
    }

}
