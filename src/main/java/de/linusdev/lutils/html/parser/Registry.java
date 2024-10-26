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
import de.linusdev.lutils.html.impl.HtmlComment;
import de.linusdev.lutils.html.impl.HtmlDocType;
import de.linusdev.lutils.html.impl.HtmlText;
import de.linusdev.lutils.html.impl.StandardHtmlAttributeTypes;
import de.linusdev.lutils.html.impl.element.StandardHtmlElement;
import de.linusdev.lutils.html.impl.element.StandardHtmlElementTypes;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.function.Function;

public class Registry {
    /**
     * All registered element types.
     */
    private final @NotNull HashMap<String, HtmlElementType<?>> elements;
    /**
     * All registered attribute types
     */
    private final @NotNull HashMap<String, HtmlAttributeType> attributes;

    /**
     * Parser for doc-type elements ({@code <!doctype html>})
     */
    private final @NotNull HtmlObjectParser<?> docTypeParser;
    /**
     * Parser for text elements.
     */
    private final @NotNull HtmlObjectParser<?> textParser;
    /**
     * Parser for comment elements ({@code <!-- comment -->})
     */
    private final @NotNull HtmlObjectParser<?> commentParser;
    /**
     * Element type creator for element types not contained in {@link #elements}.
     */
    private final @NotNull Function<String, HtmlElementType<?>> defaultElementType;
    /**
     * Attribute type creator for attribute types not contained in {@link #attributes}.
     */
    private final @NotNull Function<String, HtmlAttributeType> defaultAttributeType;


    /**
     * Returns a new {@link Builder} with all {@link StandardHtmlElementTypes} and all
     * {@link StandardHtmlAttributeTypes} added.
     */
    public static @NotNull Registry.Builder getDefault() {
        Builder builder = new Builder();

        for (@NotNull HtmlElementType<?> eleType : StandardHtmlElementTypes.VALUES)
            builder.putElement(eleType);

        for (@NotNull HtmlAttributeType attrType : StandardHtmlAttributeTypes.VALUES)
            builder.addAttribute(attrType);

        return builder;
    }

    Registry(
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

    /**
     * Get element type from {@link #elements} or {@link #defaultElementType}.
     */
    public @NotNull HtmlElementType<?> getElementTypeByName(@NotNull String name) {
        HtmlElementType<?> type = elements.get(name);
        if(type != null)
            return type;

        return defaultElementType.apply(name);
    }

    /**
     * Get attribute type from {@link #attributes} or {@link #defaultAttributeType}.
     */
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

    @SuppressWarnings("unused")
    public static class Builder {
        private final @NotNull HashMap<String, HtmlElementType<?>> elements = new HashMap<>();
        private final @NotNull HashMap<String, HtmlAttributeType> attributes = new HashMap<>();

        private @NotNull HtmlObjectParser<?> docTypeParser = HtmlDocType.PARSER;
        private @NotNull HtmlObjectParser<?> textParser = HtmlText.PARSER;
        private @NotNull HtmlObjectParser<?> commentParser = HtmlComment.PARSER;
        private @NotNull Function<String, HtmlElementType<?>> defaultElementType = StandardHtmlElement.Type::newInline;
        private @NotNull Function<String, HtmlAttributeType> defaultAttributeType = StandardHtmlAttributeTypes.StringType::new;

        public void putElement(@NotNull HtmlElementType<?> type) {
            elements.put(type.name(), type);
        }

        public void addAttribute(@NotNull HtmlAttributeType type) {
            if(attributes.put(type.name(), type) != null)
                throw new IllegalStateException("Attribute with name '" + type.name() + "' already exists.");
        }

        public void setDocTypeParser(@NotNull HtmlObjectParser<?> docTypeParser) {
            this.docTypeParser = docTypeParser;
        }

        public void setTextParser(@NotNull HtmlObjectParser<?> textParser) {
            this.textParser = textParser;
        }

        public void setCommentParser(@NotNull HtmlObjectParser<?> commentParser) {
            this.commentParser = commentParser;
        }

        public void setDefaultElementType(@NotNull Function<String, HtmlElementType<?>> defaultElementType) {
            this.defaultElementType = defaultElementType;
        }

        public void setDefaultAttributeType(@NotNull Function<String, HtmlAttributeType> defaultAttributeType) {
            this.defaultAttributeType = defaultAttributeType;
        }

        public Registry build() {
            return new Registry(
                    elements, attributes, docTypeParser, textParser, commentParser,
                    defaultElementType, defaultAttributeType
            );
        }
    }
}
