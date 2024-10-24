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

import de.linusdev.lutils.html.HtmlAttributeType;
import de.linusdev.lutils.html.HtmlElementType;
import de.linusdev.lutils.html.impl.HtmlComment;
import de.linusdev.lutils.html.impl.HtmlDocType;
import de.linusdev.lutils.html.impl.HtmlText;
import de.linusdev.lutils.html.impl.StandardHtmlAttributeTypes;
import de.linusdev.lutils.html.impl.element.StandardHtmlElement;
import de.linusdev.lutils.html.impl.element.StandardHtmlElementTypes;
import de.linusdev.lutils.html.parser.Registry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class Lhtml {

    private static @Nullable Registry REGISTRY = null;

    public static @NotNull Registry getRegistry() {
        if(REGISTRY != null) return REGISTRY;

        HashMap<String, HtmlElementType<?>> elements = new HashMap<>();
        for (@NotNull HtmlElementType<?> value : StandardHtmlElementTypes.VALUES) {
            elements.put(value.name(), value);
        }

        elements.put(LhtmlHead.TYPE.name(), LhtmlHead.TYPE);

        HashMap<String, HtmlAttributeType> attributes = new HashMap<>();
        for (@NotNull HtmlAttributeType value : StandardHtmlAttributeTypes.VALUES) {
            attributes.put(value.name(), value);
        }

        REGISTRY = new Registry(elements, attributes,
                HtmlDocType.PARSER, HtmlText.PARSER, HtmlComment.PARSER,
                StandardHtmlElement.Type::newInline,
                s -> () -> s
        );

        return REGISTRY;
    }



}
