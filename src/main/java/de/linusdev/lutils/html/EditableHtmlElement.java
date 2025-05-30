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

package de.linusdev.lutils.html;

import org.jetbrains.annotations.NotNull;

/**
 * A html element, whose {@link #content()} and {@link #attributes()} can be edited.
 */
public interface EditableHtmlElement extends HtmlElement, HtmlAddable<EditableHtmlElement> {

    @Override
    default void _addContent(@NotNull HtmlObject object) {
        content().add(object);
    }

    @Override
    default void _setAttribute(@NotNull HtmlAttribute attribute) {
        attributes().put(attribute);
    }

    @Override
    default @NotNull HtmlAttributeMap _getAttributeMap() {
        return attributes();
    }

    @Override
    @NotNull EditableHtmlElement copy();


}
