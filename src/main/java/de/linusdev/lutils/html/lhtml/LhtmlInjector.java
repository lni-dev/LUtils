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
import de.linusdev.lutils.html.impl.StandardHtmlAttribute;
import de.linusdev.lutils.html.impl.element.StandardHtmlElementTypes;
import de.linusdev.lutils.html.parser.HtmlParserInjector;
import de.linusdev.lutils.other.str.ConstructableString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LhtmlInjector implements HtmlParserInjector {

    protected static @Nullable ConstructableString getConstructableStringOfValue(@Nullable String value) {
        if(value == null || value.isEmpty())
            return null;

        Pattern pattern = Pattern.compile("\\$\\{(?<key>[a-zA-Z0-9-_]+)}");
        Matcher matcher = pattern.matcher(value);

        if(!matcher.find())
            return null;

        ConstructableString.Builder builder = new ConstructableString.Builder();
        int start = 0;
        do {
            String constant = value.substring(start, matcher.start());
            if(!constant.isEmpty())
                builder.addConstant(constant);
            builder.addPlaceholder(matcher.group("key"));

            start = matcher.end();

        } while (matcher.find());

        if (start != value.length())
            builder.addConstant(value.substring(start));

        return builder.build();
    }

    public static final @NotNull String LHTML_ATTR_TEMPLATE_NAME = "lhtml-template";
    public static final @NotNull String LHTML_ATTR_PLACEHOLDER_NAME = "lhtml-placeholder";

    private final Stack<Builder> builders = new Stack<>();

    public LhtmlInjector() {
        builders.add(new Builder());
    }

    @Override
    public @Nullable HtmlAttribute onAttributeParsed(@NotNull HtmlAttributeType type, @Nullable String value) {
        ConstructableString str = getConstructableStringOfValue(value);
        if(str == null)
            return new StandardHtmlAttribute(type, value);

        return new LhtmlPlaceholderAttribute(type, str);
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
            builders.peek().setHead((LhtmlHead) element);
            return element;
        }

        if(HtmlElementType.equals(StandardHtmlElementTypes.BODY, element.tag())) {
            builders.peek().setBody(element);
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
}
