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

package de.linusdev.lutils.html.lhtml;

import de.linusdev.lutils.html.*;
import de.linusdev.lutils.html.impl.HtmlText;
import de.linusdev.lutils.html.impl.StandardHtmlAttribute;
import de.linusdev.lutils.html.impl.element.StandardHtmlElementTypes;
import de.linusdev.lutils.html.lhtml.skeleton.LhtmlPageSkeleton;
import de.linusdev.lutils.html.lhtml.skeleton.LhtmlSkeletonBuilder;
import de.linusdev.lutils.html.parser.HtmlParser;
import de.linusdev.lutils.html.parser.HtmlParserInjector;
import de.linusdev.lutils.other.str.ConstructableString;
import de.linusdev.lutils.other.str.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Reader;
import java.util.Stack;

/**
 * {@link HtmlParserInjector} to use while parsing to parse a {@link LhtmlPageSkeleton}. Must be used in combination with
 * a {@link Lhtml#getRegistry() lhtml compatible registry}.
 * @see LhtmlPage#parse(HtmlParser, Reader)
 */
public class LhtmlInjector implements HtmlParserInjector {

    private final Stack<LhtmlSkeletonBuilder> builders = new Stack<>();

    public LhtmlInjector() {
        builders.add(new LhtmlSkeletonBuilder());
    }

    @Override
    public @Nullable HtmlAttribute onAttributeParsed(@NotNull HtmlAttributeType<?> type, @Nullable String value) {
        ConstructableString str = StringUtils.computePossibleConstructableStringOfText(value);
        if(str == null)
            return new StandardHtmlAttribute(type, value);

        return new LhtmlPlaceholderAttribute(type, str);
    }

    @Override
    public int onStartParsingContent(@NotNull HtmlElementType<?> tag, @NotNull HtmlAttributeMap attributes) {
        HtmlAttribute lhtmlTemplateAttr = attributes.get(Lhtml.ATTR_TEMPLATE);
        if(lhtmlTemplateAttr != null) {
            builders.push(new LhtmlSkeletonBuilder());
        }

        return 0;
    }

    @Override
    public @Nullable HtmlObject onObjectParsed(@NotNull HtmlObject parsed) {
        if(parsed.type() == HtmlObjectType.TEXT) {
            HtmlText text = (HtmlText) parsed;

            ConstructableString str = StringUtils.computePossibleConstructableStringOfText(text.getText());
            if(str == null)
                return parsed;

            return new LHtmlPlaceholderText(str);
        }

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

        String placeholderId = element.attributes().getValue(Lhtml.ATTR_PLACEHOLDER);

        if(placeholderId != null) {

            if(!(element instanceof EditableHtmlElement editable))
                throw new IllegalStateException("Parsed element is not editable!");

            LhtmlPlaceholderElement placeholder = new LhtmlPlaceholderElement(placeholderId, editable);
            LhtmlSkeletonBuilder builder = builders.peek();
            builder.addPlaceholder(placeholder);

            return placeholder;
        }

        String templateId = element.attributes().getValue(Lhtml.ATTR_TEMPLATE);
        if(templateId != null) {
            if(!(element instanceof EditableHtmlElement editable))
                throw new IllegalStateException("Parsed element is not editable!");

            LhtmlSkeletonBuilder builder = builders.pop();
            builders.peek().addTemplate(builder.buildTemplate(templateId, editable));
            return null;
        }

        return parsed;
    }

    @Override
    public void onEndParsingContent(int id) {}

    /**
     * Get the last {@link LhtmlSkeletonBuilder}, which can be used to build a {@link LhtmlPageSkeleton}.
     */
    public LhtmlSkeletonBuilder getBuilder() {
        LhtmlSkeletonBuilder builder = builders.pop();
        assert builders.isEmpty();
        return builder;
    }
}
