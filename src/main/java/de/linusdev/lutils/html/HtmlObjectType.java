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

import de.linusdev.lutils.html.impl.HtmlComment;
import de.linusdev.lutils.html.impl.HtmlDocType;

public enum HtmlObjectType {
    /**
     * Html element, e.g.: {@code <div></div>}
     */
    ELEMENT,
    /**
     * Html comment, e.g.: {@link HtmlComment}
     */
    COMMENT,
    /**
     * Html doc type, e.g.: {@link HtmlDocType}
     */
    DOC_TYPE,

    /**
     * Simple text
     */
    TEXT,
    /**
     * Unused currently
     */
    CUSTOM,

    /**
     * Not a real html object. Instead, a collection of html objects.
     */
    PAGE,
}
