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
import de.linusdev.lutils.html.impl.StandardHtmlElement;
import de.linusdev.lutils.html.parser.HtmlParserInjector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class LhtmlInjector implements HtmlParserInjector {

    public static final @NotNull String LHTML_ATTR_TEMPLATE_NAME = "lhtml-template";
    public static final @NotNull String LHTML_ATTR_PLACEHOLDER_NAME = "lhtml-placeholder";

    private static final int DEFAULT_ID = 0;
    private static final int TEMPLATE_ID = 1;

    private final @NotNull HashMap<String, LhtmlPlaceholder> placeholders = new HashMap<>();
    private final @NotNull HashMap<String, LhtmlTemplate> templates = new HashMap<>();

    private boolean inTemplate = false;
    private HashMap<String, LhtmlPlaceholder> templatePlaceholders;

    public @NotNull HashMap<String, LhtmlPlaceholder> getPlaceholders() {
        return placeholders;
    }

    public @NotNull HashMap<String, LhtmlTemplate> getTemplates() {
        return templates;
    }

    @Override
    public int onStartParsingContent(@NotNull HtmlElementType<?> tag, @NotNull Map<String, HtmlAttribute> attributes) {
        HtmlAttribute lhtmlTemplateAttr = attributes.get(LHTML_ATTR_TEMPLATE_NAME);
        if(lhtmlTemplateAttr != null) {
            if(inTemplate)
                throw new IllegalStateException("Cannot have a template in a template!");
            inTemplate = true;
            templatePlaceholders = new HashMap<>();
            return TEMPLATE_ID;
        }

        return DEFAULT_ID;
    }

    @Override
    public @Nullable HtmlObject onObjectParsed(@NotNull HtmlObject parsed) {
        if(parsed.type() != HtmlObjectType.ELEMENT)
            return parsed;
        HtmlElement element = parsed.asHtmlElement();
        HtmlAttribute lhtmlPlaceHolderAttr = element.attributes().get(LHTML_ATTR_PLACEHOLDER_NAME);

        if(lhtmlPlaceHolderAttr != null) {
            String id = lhtmlPlaceHolderAttr.value();

            if(id == null)
                throw new IllegalStateException("Placeholder id missing");

            if(!(element instanceof EditableHtmlElement editable))
                throw new IllegalStateException("Parsed element is not editable!");

            LhtmlPlaceholder placeholder = new LhtmlPlaceholder(id, editable);
            if(inTemplate) {
                templatePlaceholders.put(lhtmlPlaceHolderAttr.value(), placeholder);
            } else {
                placeholders.put(lhtmlPlaceHolderAttr.value(), placeholder);
            }

            return parsed;
        }

        HtmlAttribute lhtmlTemplateAttr = element.attributes().get(LHTML_ATTR_TEMPLATE_NAME);
        if(lhtmlTemplateAttr != null) {
            HtmlElementType<?> tag = element.tag();
            if(!(tag instanceof StandardHtmlElement.Type))
                tag = StandardHtmlElement.Type.newNormal(tag.name());
            LhtmlTemplate template = new LhtmlTemplate((StandardHtmlElement.Type) tag, element.content(), element.attributes(), templatePlaceholders);
            templates.put(lhtmlTemplateAttr.value(), template);
            return null;
        }

        return parsed;
    }

    @Override
    public void onEndParsingContent(int id) {
        if(id == TEMPLATE_ID)
            inTemplate = false;
    }
}
