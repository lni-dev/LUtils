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

package de.linusdev.lutils.html.parser;

import de.linusdev.lutils.html.HtmlAttributeType;
import de.linusdev.lutils.html.HtmlElementType;
import de.linusdev.lutils.html.HtmlObjectParser;
import de.linusdev.lutils.html.impl.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.function.Function;

public class Registry {
    private final @NotNull HashMap<String, HtmlElementType<?>> elements;
    private final @NotNull HashMap<String, HtmlAttributeType> attributes;

    private final @NotNull HtmlObjectParser<?> docTypeParser;
    private final @NotNull HtmlObjectParser<?> textParser;
    private final @NotNull HtmlObjectParser<?> commentParser;
    private final @NotNull Function<String, HtmlElementType<?>> defaultElementType;
    private final @NotNull Function<String, HtmlAttributeType> defaultAttributeType;

    private static @Nullable Registry DEFAULT = null;

    public static @NotNull Registry getDefault() {
        if(DEFAULT != null) return DEFAULT;

        HashMap<String, HtmlElementType<?>> elements = new HashMap<>();
        for (@NotNull HtmlElementType<?> value : StandardHtmlElementTypes.VALUES) {
            elements.put(value.name(), value);
        }

        HashMap<String, HtmlAttributeType> attributes = new HashMap<>();
        for (@NotNull HtmlAttributeType value : StandardHtmlAttributeTypes.VALUES) {
            attributes.put(value.name(), value);
        }

        DEFAULT = new Registry(elements, attributes,
                HtmlDocType.PARSER, HtmlText.PARSER, HtmlComment.PARSER,
                StandardHtmlElement.Type::newInline,
                s -> () -> s
        );

        return DEFAULT;
    }

    public Registry(
            @NotNull HashMap<String, HtmlElementType<?>> elements,
            @NotNull HashMap<String, HtmlAttributeType> attributes,
            @NotNull HtmlObjectParser<?> docTypeParser,
            @NotNull HtmlObjectParser<?> textParser,
            @NotNull HtmlObjectParser<?> commentParser,
            @NotNull Function<String, HtmlElementType<?>> defaultElementType,
            @NotNull Function<String, HtmlAttributeType> defaultAttributeType
    ) {
        this.elements = elements;
        this.attributes = attributes;
        this.docTypeParser = docTypeParser;
        this.textParser = textParser;
        this.commentParser = commentParser;
        this.defaultElementType = defaultElementType;
        this.defaultAttributeType = defaultAttributeType;
    }

    public @NotNull HtmlElementType<?> getElementTypeByName(@NotNull String name) {
        HtmlElementType<?> type = elements.get(name);
        if(type != null)
            return type;

        return defaultElementType.apply(name);
    }

    public @NotNull HtmlAttributeType getAttributeTypeByName(@NotNull String name) {
        HtmlAttributeType type = attributes.get(name);
        if(type != null)
            return type;

        return defaultAttributeType.apply(name);
    }

    public @NotNull HtmlObjectParser<?> getDocTypeParser() {
        return docTypeParser;
    }

    public @NotNull HtmlObjectParser<?> getTextParser() {
        return textParser;
    }

    public @NotNull HtmlObjectParser<?> getCommentParser() {
        return commentParser;
    }
}
