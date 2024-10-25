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

import de.linusdev.lutils.html.HtmlAddable;
import de.linusdev.lutils.html.HtmlObject;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class LhtmlPlaceholder implements HtmlAddable {

    protected final @NotNull List<LhtmlPlaceholderElement> elements;

    public LhtmlPlaceholder() {
        this.elements = new ArrayList<>(1);
    }

    void addPlaceholderElement(@NotNull LhtmlPlaceholderElement element) {
        this.elements.add(element);
    }

    @Override
    public void addContent(@NotNull HtmlObject object) {
        for (LhtmlPlaceholderElement element : elements) {
            element.addContent(object);
        }
    }
}
