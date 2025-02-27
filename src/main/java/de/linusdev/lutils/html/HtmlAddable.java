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

import de.linusdev.lutils.html.impl.HtmlText;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Object with html content, where new elements can be added.
 */
public interface HtmlAddable extends _HtmlAddable<HtmlAddable> {

    /**
     * Add given {@code object} to the content.
     */
    void addContent(@NotNull HtmlObject object);

    /**
     * Add given string as {@link HtmlText} to the content.
     */
    default void addText(@NotNull String text) {
        addContent(new HtmlText(text));
    }

    @Override
    @ApiStatus.Internal
    default void _addContent(@NotNull HtmlObject object) {
        addContent(object);
    }
}
