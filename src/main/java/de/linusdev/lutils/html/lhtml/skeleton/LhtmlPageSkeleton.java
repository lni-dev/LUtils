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

import de.linusdev.lutils.html.HtmlElement;
import de.linusdev.lutils.html.HtmlElementType;
import de.linusdev.lutils.html.HtmlObject;
import de.linusdev.lutils.html.HtmlObjectType;
import de.linusdev.lutils.html.impl.HtmlPage;
import de.linusdev.lutils.html.impl.element.StandardHtmlElementTypes;
import de.linusdev.lutils.html.lhtml.*;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

/**
 * Skeleton for {@link LhtmlPage}.
 */
public class LhtmlPageSkeleton implements LhtmlSkeleton{

    protected final @NotNull HtmlPage actual;
    protected final @NotNull Map<String, LhtmlTemplateSkeleton> templates;

    public LhtmlPageSkeleton(
            @NotNull HtmlPage actual,
            @NotNull Map<String, LhtmlTemplateSkeleton> templates
    ) {
        this.actual = actual;
        this.templates = templates;
    }

    public @NotNull LhtmlTemplateElement getTemplate(@NotNull String id) {
        return Objects.requireNonNull(templates.get(id)).copy();
    }

    public @NotNull LhtmlPage copy() {
        HtmlPage copy = actual.copy();
        Map<String, LhtmlPlaceholder> placeholders = new HashMap<>();
        Map<String, String> replaceValues = new HashMap<>();
        AtomicReference<LhtmlHead> head = new AtomicReference<>();
        AtomicReference<HtmlElement> body = new AtomicReference<>();

        Consumer<HtmlObject> consumer = object -> {
            if(object.type() == HtmlObjectType.ELEMENT) {
                HtmlElement element = object.asHtmlElement();

                element.iterateAttributes(attribute -> {
                    if(attribute instanceof LhtmlPlaceholderAttribute placeholderAttr) {
                        placeholderAttr.setReplaceValues(replaceValues);
                    }
                });

                if(element instanceof LhtmlPlaceholderElement placeholderEle) {
                    LhtmlPlaceholder holder = placeholders.computeIfAbsent(placeholderEle.getId(), LhtmlPlaceholder::new);
                    holder.addPlaceholderElement(placeholderEle);
                }

                if(HtmlElementType.equals(StandardHtmlElementTypes.BODY, element.tag())) {
                    body.set(element);
                } else if(element instanceof LhtmlHead lhtmlHead) {
                    head.set(lhtmlHead);
                }
            }
        };

        copy.iterateContentRecursive(consumer);

        return new LhtmlPage(copy, placeholders, templates, replaceValues, head.get(), body.get());
    }

    @Override
    public @NotNull String getId() {
        return LhtmlPage.ID;
    }
}
