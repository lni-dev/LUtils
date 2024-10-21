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
import de.linusdev.lutils.html.impl.StandardHtmlElement;
import org.jetbrains.annotations.NotNull;

import java.util.*;

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

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public @NotNull LhtmlTemplate clone() {
        List<@NotNull HtmlObject> content = new ArrayList<>(this.content.size());
        Map<String, HtmlAttribute> attributes = new HashMap<>(this.attributes.size());
        Map<String, LhtmlPlaceholder> placeHolders = new HashMap<>(this.placeholders.size());

        this.content.forEach(object -> {
            if(object instanceof LhtmlPlaceholder placeholder) {
                LhtmlPlaceholder clone = placeholder.clone();
                content.add(clone);
                placeHolders.put(clone.getId(), clone);
            } else {
                content.add(object.clone());
            }
        });
        this.attributes.forEach((key, attr) -> attributes.put(key, attr.clone()));

        return new LhtmlTemplate(tag, content, attributes, placeHolders);
    }

    @Override
    public @NotNull EditableHtmlElement getPlaceholder(@NotNull String id) {
        return Objects.requireNonNull(placeholders.get(id));
    }
}
