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

package de.linusdev.lutils.html.lhtml;


import de.linusdev.lutils.html.*;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * A meta placeholder. A placeholder's html can be changed using the methods of {@link HtmlAddable}.
 * The change will be reflected in all {@link LhtmlPlaceholderElement}s in the parent with the same {@link #getId() id}.
 */
public class LhtmlPlaceholder implements HtmlAddable<LhtmlPlaceholder>, LhtmlIdentifiable {

    protected final @NotNull List<LhtmlPlaceholderElement> elements;
    private final @NotNull String id;

    public LhtmlPlaceholder(@NotNull String id) {
        this.id = id;
        this.elements = new ArrayList<>(1);
    }

    /**
     * Add a placeholder element with the same {@link #getId() id}. Called when constructing a {@link LhtmlPlaceholder}.
     */
    @ApiStatus.Internal
    public void addPlaceholderElement(@NotNull LhtmlPlaceholderElement element) {
        assert getId().equals(element.getId());
        this.elements.add(element);
    }

    @Override
    public void _addContent(@NotNull HtmlObject object) {
        for (LhtmlPlaceholderElement element : elements) {
            element.addContent(object);
        }
    }

    @Override
    public void _setAttribute(@NotNull HtmlAttribute attribute) {
        for (LhtmlPlaceholderElement element : elements) {
            element.setAttribute(attribute);
        }
    }

    @Override
    public @NotNull HtmlAttributeMap _getAttributeMap() {
        throw new Error("This does not work here. overwrite calling methods instead");
    }

    public void addToAttribute(@NotNull HtmlAttributeType<?> type, String item) {
        for (LhtmlPlaceholderElement element : elements) {
            element.addToAttribute(type, item);
        }
    }

    @Override
    public void removeFromAttribute(@NotNull HtmlAttributeType<?> type, String item) {
        for (LhtmlPlaceholderElement element : elements) {
            element.removeFromAttribute(type, item);
        }
    }

    @Override
    public @NotNull String getId() {
        return id;
    }
}
