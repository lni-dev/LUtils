/*
 * Copyright (c) 2025 Linus Andera
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

import de.linusdev.lutils.html.impl.StandardHtmlAttributeTypes;
import de.linusdev.lutils.html.impl.element.StandardHtmlElementTypes;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unchecked")
public interface _HtmlAddable<SELF> {

    @ApiStatus.Internal
    void _addContent(@NotNull HtmlObject object);

    /**
     * Adds a {@link StandardHtmlElementTypes#LINK link} element like this:
     * {@code <link rel="stylesheet" href="path">}. "path" is replaced with
     * given {@code path}.
     */
    default SELF addCssLink(@NotNull String path) {
        _addContent(StandardHtmlElementTypes.LINK.builder()
                .addAttribute(StandardHtmlAttributeTypes.REL, "stylesheet")
                .addAttribute(StandardHtmlAttributeTypes.HREF, path).build()
        );

        return (SELF) this;
    }
}