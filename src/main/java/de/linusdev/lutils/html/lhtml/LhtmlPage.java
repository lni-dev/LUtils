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
import de.linusdev.lutils.html.impl.HtmlPage;
import de.linusdev.lutils.html.impl.element.StandardHtmlElementTypes;
import de.linusdev.lutils.html.parser.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class LhtmlPage implements HtmlObject, HasHtmlContent, LhtmlElement {

    public static @NotNull LhtmlPage parse(@NotNull HtmlParser parser, @NotNull Reader reader) throws IOException, ParseException {
        LhtmlInjector injector = new LhtmlInjector();
        HtmlParserState state = new HtmlParserState(injector, parser);
        HtmlReader htmlReader = new HtmlReader(reader);

        HtmlPage page = HtmlPage.PARSER.parse(state, htmlReader);

        return injector.getBuilder().buildPage(page);
    }

    protected final @NotNull HtmlPage actual;
    protected final @NotNull Map<String, LhtmlPlaceholder> placeholders;
    protected final @NotNull Map<String, LhtmlTemplateElement> templates;
    protected final @NotNull Map<String, String> replaceValues;
    protected final @NotNull LhtmlHead head;
    protected final @NotNull HtmlElement body;

    public LhtmlPage(
            @NotNull HtmlPage actual,
            @NotNull Map<String, LhtmlPlaceholder> placeholders,
            @NotNull Map<String, LhtmlTemplateElement> templates,
            @NotNull Map<String, String> replaceValues,
            @NotNull LhtmlHead head,
            @NotNull HtmlElement body
    ) {
        this.actual = actual;
        this.placeholders = placeholders;
        this.templates = templates;
        this.replaceValues = replaceValues;
        this.head = head;
        this.body = body;
    }


    public @NotNull HtmlAddable getPlaceholder(@NotNull String id) {
        return Objects.requireNonNull(placeholders.get(id), "No template found with id '" + id + "'.");
    }

    public @NotNull LhtmlTemplateElement getTemplate(@NotNull String id) {
        return Objects.requireNonNull(templates.get(id));
    }

    @Override
    public @NotNull HtmlObjectType type() {
        return HtmlObjectType.PAGE;
    }

    public @NotNull LhtmlHead getHead() {
        return head;
    }

    public @NotNull HtmlElement getBody() {
        return body;
    }

    @Override
    public @NotNull LhtmlPage copy() {
        HtmlPage copy = actual.copy();
        Map<String, LhtmlPlaceholder> placeholders = new HashMap<>(this.placeholders.size());
        Map<String, String> replaceValues = new HashMap<>();
        AtomicReference<LhtmlHead> head = new AtomicReference<>();
        AtomicReference<HtmlElement> body = new AtomicReference<>();

        Consumer<HtmlObject> consumer = object -> {
            if(object.type() == HtmlObjectType.ELEMENT) {
                HtmlElement element = object.asHtmlElement();

                element.iterateAttributes(attribute -> {
                    if(attribute instanceof LhtmlPlaceholderAttribute placeholderAttr) {
                        placeholderAttr.setValues(replaceValues);
                    }
                });

                if(element instanceof LhtmlPlaceholderElement placeholderEle) {
                    LhtmlPlaceholder holder = placeholders.computeIfAbsent(placeholderEle.getId(), s -> new LhtmlPlaceholder());
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
    public void write(@NotNull HtmlWritingState state, @NotNull Writer writer) throws IOException {
        actual.write(state, writer);
    }

    @Override
    public @NotNull List<@NotNull HtmlObject> content() {
        return actual.content();
    }
}
