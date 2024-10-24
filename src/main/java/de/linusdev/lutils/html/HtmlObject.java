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

import org.jetbrains.annotations.NotNull;

public interface HtmlObject extends HtmlWritable {

    /**
     * The type of this html object.
     */
    @NotNull HtmlObjectType type();

    /**
     * Can be safely called only if {@link #type()} is {@link HtmlObjectType#ELEMENT}.
     */
    default @NotNull HtmlElement asHtmlElement() {
        return (HtmlElement) this;
    }

    @NotNull HtmlObject copy();

}
