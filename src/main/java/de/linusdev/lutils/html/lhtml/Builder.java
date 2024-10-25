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
import de.linusdev.lutils.html.HtmlElement;
import de.linusdev.lutils.html.HtmlObject;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

public class Builder {

    private final @NotNull HashMap<String, LhtmlPlaceholder> placeholders = new HashMap<>();
    private final @NotNull HashMap<String, LhtmlTemplateElement> templates = new HashMap<>();

    public void addPlaceholder(@NotNull LhtmlPlaceholderElement element) {
        LhtmlPlaceholder holder = placeholders.computeIfAbsent(element.getId(), s -> new LhtmlPlaceholder());
        holder.addPlaceholderElement(element);
    }

    public void addTemplate(@NotNull LhtmlTemplateElement template) {
        if(templates.put(template.getId(), template) != null) {
            throw new IllegalStateException("A template with the id '" + template.getId() + " already exists.");
        }
    }

    public @NotNull LhtmlTemplateElement buildTemplate(@NotNull String id, @NotNull EditableHtmlElement element) {
        return new LhtmlTemplateElement(id, element , placeholders, templates);
    }

    public @NotNull LhtmlPage buildPage(@NotNull List<HtmlObject> content, @NotNull LhtmlHead head, @NotNull HtmlElement body) {
        return new LhtmlPage(content, placeholders, templates, head, body);
    }

}
