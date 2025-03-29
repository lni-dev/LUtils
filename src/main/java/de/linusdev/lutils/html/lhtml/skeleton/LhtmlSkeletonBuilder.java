/*
 * Copyright (c) 2024-2025 Linus Andera
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

package de.linusdev.lutils.html.lhtml.skeleton;

import de.linusdev.lutils.html.EditableHtmlElement;
import de.linusdev.lutils.html.HtmlElement;
import de.linusdev.lutils.html.impl.HtmlPage;
import de.linusdev.lutils.html.lhtml.LhtmlHead;
import de.linusdev.lutils.html.lhtml.LhtmlInjector;
import de.linusdev.lutils.html.lhtml.LhtmlPlaceholder;
import de.linusdev.lutils.html.lhtml.LhtmlPlaceholderElement;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

/**
 * Builder for {@link LhtmlPageSkeleton} and {@link LhtmlTemplateSkeleton} used by {@link LhtmlInjector}.
 */
@ApiStatus.Internal
public class LhtmlSkeletonBuilder {

    private final @NotNull HashMap<String, LhtmlPlaceholder> placeholders = new HashMap<>();
    private final @NotNull HashMap<String, LhtmlTemplateSkeleton> templates = new HashMap<>();
    private @Nullable LhtmlHead head = null;
    private @Nullable HtmlElement body = null;

    public void addPlaceholder(@NotNull LhtmlPlaceholderElement element) {
        LhtmlPlaceholder holder = placeholders.computeIfAbsent(element.getId(), LhtmlPlaceholder::new);
        holder.addPlaceholderElement(element);
    }

    public void addTemplate(@NotNull LhtmlTemplateSkeleton template) {
        if(templates.put(template.getId(), template) != null) {
            throw new IllegalStateException("A template with the id '" + template.getId() + " already exists.");
        }
    }

    public void setHead(@NotNull LhtmlHead head) {
        this.head = head;
    }

    public void setBody(@NotNull HtmlElement body) {
        this.body = body;
    }

    public @NotNull LhtmlTemplateSkeleton buildTemplate(@NotNull String id, @NotNull EditableHtmlElement element) {
        return new LhtmlTemplateSkeleton(id, element, templates);
    }

    public @NotNull LhtmlPageSkeleton buildPage(@NotNull HtmlPage actual) {
        // Check if head and body was found.
        if(head == null)
            throw new IllegalArgumentException("LhtmlPage is missing a head element.");

        if(body == null)
            throw new IllegalArgumentException("LhtmlPage is missing a body element.");

        return new LhtmlPageSkeleton(actual, templates);
    }

}
