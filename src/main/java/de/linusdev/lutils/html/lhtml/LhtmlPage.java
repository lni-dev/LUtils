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
import de.linusdev.lutils.html.lhtml.skeleton.LhtmlPageSkeleton;
import de.linusdev.lutils.html.lhtml.skeleton.LhtmlTemplateSkeleton;
import de.linusdev.lutils.html.parser.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * lhtml page created by a {@link LhtmlPageSkeleton}.
 */
public class LhtmlPage implements HtmlObject, HasHtmlContent, LhtmlTemplate {

    /**
     * The id of all pages.
     */
    public static final @NotNull String ID = "page";

    public static @NotNull LhtmlPageSkeleton parse(@NotNull HtmlParser parser, @NotNull Reader reader) throws IOException, ParseException {
        LhtmlInjector injector = new LhtmlInjector();
        HtmlParserState state = new HtmlParserState(injector, parser);
        HtmlReader htmlReader = new HtmlReader(reader);

        HtmlPage page = HtmlPage.PARSER.parse(state, htmlReader);

        return injector.getBuilder().buildPage(page);
    }

    protected final @NotNull HtmlPage actual;
    protected final @NotNull Map<String, LhtmlPlaceholder> placeholders;
    protected final @NotNull Map<String, LhtmlTemplateSkeleton> templates;
    protected final @NotNull Map<String, String> replaceValues;
    protected final @NotNull LhtmlHead head;
    protected final @NotNull HtmlElement body;

    public LhtmlPage(
            @NotNull HtmlPage actual,
            @NotNull Map<String, LhtmlPlaceholder> placeholders,
            @NotNull Map<String, LhtmlTemplateSkeleton> templates,
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

    /**
     * The {@link LhtmlHead head} of this page.
     */
    public @NotNull LhtmlHead getHead() {
        return head;
    }

    /**
     * The body of this page.
     */
    public @NotNull HtmlElement getBody() {
        return body;
    }

    /**
     * Adds all content of given page {@code other} to the placeholder with given {@code id}.
     * Also adds all links of the other page's {@link #head} to this {@link #head}.
     * @param id id of the placeholder, where the content shall be added.
     * @param other the page, whose content shall be added.
     */
    public void addPageContentToPlaceholder(@NotNull String id, @NotNull LhtmlPage other) {
        HtmlAddable placeholder = getPlaceholder(id);
        for (@NotNull HtmlObject object : other.getBody().content()) {
            placeholder.addContent(object);
        }

        getHead().addLinks(other.getHead());
    }


    @Override
    public @NotNull HtmlAddable getPlaceholder(@NotNull String id) {
        return Objects.requireNonNull(placeholders.get(id), "No template found with id '" + id + "'.");
    }

    @Override
    public void setValue(@NotNull String key, @NotNull String value) {
        replaceValues.put(key, value);
    }

    public @NotNull LhtmlTemplateElement getTemplate(@NotNull String id) {
        return Objects.requireNonNull(templates.get(id)).copy();
    }

    @Override
    public @NotNull HtmlObjectType type() {
        return HtmlObjectType.PAGE;
    }

    @Override
    public @NotNull LhtmlPage copy() {
        return LhtmlPageSkeleton.createCopy(actual, templates);
    }

    @Override
    public void write(@NotNull HtmlWritingState state, @NotNull Writer writer) throws IOException {
        actual.write(state, writer);
    }

    @Override
    public @NotNull List<@NotNull HtmlObject> content() {
        return actual.content();
    }

    @Override
    public @NotNull String getId() {
        return ID;
    }
}
