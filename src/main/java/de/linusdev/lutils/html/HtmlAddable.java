/*
 * Copyright (c) 2025 Linus Andera
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

package de.linusdev.lutils.html;

import de.linusdev.lutils.html.builder.HtmlElementBuilder;
import de.linusdev.lutils.html.impl.HtmlText;
import de.linusdev.lutils.html.impl.StandardHtmlAttribute;
import de.linusdev.lutils.html.impl.StandardHtmlAttributeTypes;
import de.linusdev.lutils.html.impl.element.StandardHtmlElementTypes;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

@SuppressWarnings({"unchecked", "UnusedReturnValue"})
public interface HtmlAddable<SELF> {

    @ApiStatus.Internal
    void _addContent(@NotNull HtmlObject object);

    @ApiStatus.Internal
    void _setAttribute(@NotNull HtmlAttribute attribute);

    @ApiStatus.Internal
    @NotNull HtmlAttributeMap _getAttributeMap();

    default void addToAttribute(@NotNull HtmlAttributeType<?> type, String item) {
        _getAttributeMap().addToAttribute(type, item);
    }

    default void removeFromAttribute(@NotNull HtmlAttributeType<?> type, String item) {
        _getAttributeMap().removeFromAttribute(type, item);
    }

    /**
     * Adds a {@link StandardHtmlElementTypes#LINK link} element like this:
     * {@code <link rel="stylesheet" href="path">}. "path" is replaced with
     * given {@code path}.
     */
    default SELF addCssLink(@NotNull String path) {
        _addContent(StandardHtmlElementTypes.LINK.builder()
                .setAttribute(StandardHtmlAttributeTypes.REL, "stylesheet")
                .setAttribute(StandardHtmlAttributeTypes.HREF, path).build()
        );

        return (SELF) this;
    }

    /**
     * Add given string as {@link HtmlText} to the content.
     */
    default SELF addText(@NotNull String text) {
        _addContent(new HtmlText(text));
        return (SELF) this;
    }

    default SELF addContent(@NotNull HtmlObject object) {
        _addContent(object);
        return (SELF) this;
    }

    default  <B extends HtmlElementBuilder> SELF addElement(@NotNull HtmlElementType<B> type, @NotNull Consumer<B> adjuster) {
        B builder = type.builder();
        adjuster.accept(builder);
        _addContent(builder.build());
        return (SELF) this;
    }

    default SELF setAttribute(@NotNull HtmlAttribute attribute) {
        _setAttribute(attribute);
        return (SELF) this;
    }

    default SELF setAttribute(@NotNull HtmlAttributeType<?> type, @Nullable String value) {
        _setAttribute(new StandardHtmlAttribute(type, value));
        return (SELF) this;
    }
}