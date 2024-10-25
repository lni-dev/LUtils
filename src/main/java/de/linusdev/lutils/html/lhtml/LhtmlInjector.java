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
import de.linusdev.lutils.html.impl.element.StandardHtmlElementTypes;
import de.linusdev.lutils.html.parser.HtmlParserInjector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Stack;

public class LhtmlInjector implements HtmlParserInjector {

    public static final @NotNull String LHTML_ATTR_TEMPLATE_NAME = "lhtml-template";
    public static final @NotNull String LHTML_ATTR_PLACEHOLDER_NAME = "lhtml-placeholder";

    private @Nullable LhtmlHead head = null;
    private @Nullable HtmlElement body = null;

    private final Stack<Builder> builders = new Stack<>();

    public LhtmlInjector() {
        builders.add(new Builder());
    }

    @Override
    public int onStartParsingContent(@NotNull HtmlElementType<?> tag, @NotNull Map<String, HtmlAttribute> attributes) {
        HtmlAttribute lhtmlTemplateAttr = attributes.get(LHTML_ATTR_TEMPLATE_NAME);
        if(lhtmlTemplateAttr != null) {
            builders.push(new Builder());
        }

        return 0;
    }

    @Override
    public @Nullable HtmlObject onObjectParsed(@NotNull HtmlObject parsed) {
        if(parsed.type() != HtmlObjectType.ELEMENT)
            return parsed;
        HtmlElement element = parsed.asHtmlElement();

        if(element.tag() == LhtmlHead.TYPE) {
            head = (LhtmlHead) element;
            return element;
        }

        if(HtmlElementType.equals(StandardHtmlElementTypes.BODY, element.tag())) {
            body = element;
            return element;
        }

        HtmlAttribute lhtmlPlaceHolderAttr = element.attributes().get(LHTML_ATTR_PLACEHOLDER_NAME);

        if(lhtmlPlaceHolderAttr != null) {
            String id = lhtmlPlaceHolderAttr.value();

            if(id == null)
                throw new IllegalStateException("Placeholder id missing");

            if(!(element instanceof EditableHtmlElement editable))
                throw new IllegalStateException("Parsed element is not editable!");

            LhtmlPlaceholderElement placeholder = new LhtmlPlaceholderElement(id, editable);
            Builder builder = builders.peek();
            builder.addPlaceholder(placeholder);

            return placeholder;
        }

        HtmlAttribute lhtmlTemplateAttr = element.attributes().get(LHTML_ATTR_TEMPLATE_NAME);
        if(lhtmlTemplateAttr != null) {
            String id = lhtmlTemplateAttr.value();
            if(id == null)
                throw new IllegalArgumentException("Attribute '" + LHTML_ATTR_TEMPLATE_NAME + "' must have a value.");

            if(!(element instanceof EditableHtmlElement editable))
                throw new IllegalStateException("Parsed element is not editable!");

            Builder builder = builders.pop();
            builders.peek().addTemplate(builder.buildTemplate(id, editable));
            return null;
        }

        return parsed;
    }

    @Override
    public void onEndParsingContent(int id) {}

    public Builder getBuilder() {
        return builders.pop();
    }

    public @Nullable LhtmlHead getHead() {
        return head;
    }

    public @Nullable HtmlElement getBody() {
        return body;
    }
}
