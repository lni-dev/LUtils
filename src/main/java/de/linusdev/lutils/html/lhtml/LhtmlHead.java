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
import de.linusdev.lutils.html.impl.StandardHtmlAttributeTypes;
import de.linusdev.lutils.html.impl.element.StandardHtmlElement;
import de.linusdev.lutils.html.impl.element.StandardHtmlElementTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;

/**
 * Head element for {@link LhtmlPage}s.
 * @see #TYPE
 * @see Builder
 */
public class LhtmlHead extends StandardHtmlElement {

    /**
     * {@link HtmlElementType}.
     */
    public static final @NotNull CustomType<LhtmlHead.Builder> TYPE = StandardHtmlElement.Type.newCustom(Builder::new, "head");

    protected final @NotNull HashMap<String, HtmlElement> links;

    protected LhtmlHead(
            @NotNull Type tag,
            @NotNull List<@NotNull HtmlObject> content,
            @NotNull HtmlAttributeMap attributes,
            @NotNull HashMap<String, HtmlElement> links
    ) {
        super(tag, content, attributes);
        this.links = links;
    }

    /**
     * Add a link to this head. The link must have the same element type name as the element type
     * {@link StandardHtmlElementTypes#LINK link}
     * and must have a {@link StandardHtmlAttributeTypes#HREF href} attribute. If a link with the same
     * {@link StandardHtmlAttributeTypes#HREF href} already exists, it will not be added again.
     * @param element the link
     */
    public void addLink(@NotNull HtmlElement element) {

        if(!HtmlElementType.equals(StandardHtmlElementTypes.LINK, element.tag()))
            throw new IllegalArgumentException("Given element is not a link, but it is '" + element.tag().name() + "'.");

        HtmlAttribute hrefAttr = element.attributes().get(StandardHtmlAttributeTypes.HREF);

        if(hrefAttr == null)
            throw new IllegalArgumentException("Given link has no href attribute.");

        if(links.get(hrefAttr.value()) != null)
            return; // link is already present

        addContent(element);
        links.put(hrefAttr.value(), element);
    }

    /**
     * {@link #addLink(HtmlElement) Adds} all links of given {@link LhtmlHead} to this.
     */
    @SuppressWarnings("unused")
    public void addLinks(@NotNull LhtmlHead other) {
        for (HtmlElement link : other.links.values()) {
            addLink(link);
        }
    }

    /**
     * Builder to build {@link LhtmlHead}s.
     */
    public static class Builder extends StandardHtmlElement.Builder {

        protected final @NotNull HashMap<String, HtmlElement> links;

        public Builder(@NotNull Type tag) {
            super(tag);
            links = new HashMap<>();
        }

        @Override
        protected @Nullable HtmlObject onContentAdd(@NotNull HtmlObject object) {
            if(object.type() != HtmlObjectType.ELEMENT)
                return super.onContentAdd(object);

            HtmlElement element = object.asHtmlElement();

            if(HtmlElementType.equals(StandardHtmlElementTypes.LINK, element.tag())) {
                HtmlAttribute hrefAttr = element.attributes().get(StandardHtmlAttributeTypes.HREF);

                if(hrefAttr == null)
                    return super.onContentAdd(object);

                links.put(hrefAttr.value(), element);
            }

            return super.onContentAdd(object);
        }

        @Override
        public @NotNull LhtmlHead build() {
            return new LhtmlHead(tag, content, attributes, links);
        }
    }

}
