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

import de.linusdev.lutils.html.EditableHtmlElement;
import de.linusdev.lutils.html.HtmlAttribute;
import de.linusdev.lutils.html.HtmlObject;
import de.linusdev.lutils.html.HtmlObjectType;
import de.linusdev.lutils.html.impl.element.StandardHtmlElement;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;

public class LhtmlTemplate extends StandardHtmlElement implements LhtmlElement {

    protected final @NotNull Map<String, LhtmlPlaceholder> placeholders;

    public LhtmlTemplate(
            @NotNull Type tag,
            @NotNull List<@NotNull HtmlObject> content,
            @NotNull Map<String, HtmlAttribute> attributes,
            @NotNull Map<String, LhtmlPlaceholder> placeholders
    ) {
        super(tag, content, attributes);
        this.placeholders = placeholders;
    }


    @Override
    public @NotNull LhtmlTemplate copy() {
        List<@NotNull HtmlObject> content = new ArrayList<>(this.content.size());
        Map<String, HtmlAttribute> attributes = new HashMap<>(this.attributes.size());
        Map<String, LhtmlPlaceholder> placeHolders = new HashMap<>(this.placeholders.size());


        Consumer<HtmlObject> consumer = object -> {
            System.out.println("type: " + object.type());
            System.out.println(object.writeToString());
            if(object instanceof LhtmlPlaceholder placeholder) {
                System.out.println("is placeholder!");
                placeHolders.put(placeholder.getId(), placeholder);
            }
        };

        for (@NotNull HtmlObject object : this.content) {
            HtmlObject copy = object.copy();
            consumer.accept(copy);

            if(copy.type() == HtmlObjectType.ELEMENT) {
                copy.asHtmlElement().iterateContentRecursive(consumer);
                content.add(copy);
            } else {
                content.add(copy);
            }
        }
        this.attributes.forEach((key, attr) -> attributes.put(key, attr.clone()));

        return new LhtmlTemplate(tag, content, attributes, placeHolders);
    }

    @Override
    public @NotNull EditableHtmlElement getPlaceholder(@NotNull String id) {
        return Objects.requireNonNull(placeholders.get(id), "No template found with id '" + id + "'.");
    }
}
