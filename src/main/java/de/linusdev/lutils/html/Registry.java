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

package de.linusdev.lutils.html;

import de.linusdev.lutils.html.impl.HtmlDocType;
import de.linusdev.lutils.html.impl.HtmlText;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.function.Function;

public class Registry {
    private final @NotNull HashMap<String, HtmlElementType<?>> elements;
    private final @NotNull HashMap<String, HtmlAttributeType> attributes;

    private final @NotNull HtmlObjectParser<?> docTypeParser = HtmlDocType.PARSER;
    private final @NotNull HtmlObjectParser<?> textParser = HtmlText.PARSER;
    private final @NotNull Function<String, HtmlElementType<?>> defaultElementType = s -> new StandardHtmlElementTypes.Type(s, true);
    private final @NotNull Function<String, HtmlAttributeType> defaultAttributeType = s -> () -> s;

    public Registry() {
        this.elements = new HashMap<>();
        for (@NotNull HtmlElementType<?> value : StandardHtmlElementTypes.VALUES) {
            elements.put(value.name(), value);
        }

        this.attributes = new HashMap<>();
        for (@NotNull HtmlAttributeType value : StandardHtmlAttributeTypes.VALUES) {
            attributes.put(value.name(), value);
        }
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
}
