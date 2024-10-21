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
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public interface HtmlElement extends HtmlObject {

    /**
     * The tag of this html element.
     */
    @NotNull HtmlElementType<?> tag();

    /**
     * Content of this html element, including text and comments.
     */
    @NotNull List<@NotNull HtmlObject> content();

    /**
     * Child elements of this html element.
     */
    default @NotNull Iterator<@NotNull HtmlElement> children() {
        return new Iterator<>() {

            final Iterator<@NotNull HtmlObject> it = content().iterator();
            @Nullable HtmlElement next = null;

            @Override
            public boolean hasNext() {
                if (next == null && !it.hasNext())
                    return false;
                if (next != null)
                    return true;
                getNext();
                return hasNext();
            }

            @Override
            public HtmlElement next() {
                if (next == null) {
                    getNext();
                    return next();
                }

                HtmlElement buf = next;
                next = null;
                return buf;
            }

            private void getNext() {
                if (next != null) return;
                HtmlObject obj = it.next();
                while (obj.type() != HtmlObjectType.ELEMENT && it.hasNext()) {
                    obj = it.next();
                }

                if (obj.type() != HtmlObjectType.ELEMENT)
                    return;

                next = obj.asHtmlElement();
            }
        };
    }

    /**
     * Attributes of this html element.
     */
    @NotNull Map<String, HtmlAttribute> attributes();

    @Override
    @NotNull HtmlElement clone();
}
