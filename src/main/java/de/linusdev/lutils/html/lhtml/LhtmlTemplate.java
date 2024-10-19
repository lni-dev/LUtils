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
import de.linusdev.lutils.html.HtmlAttribute;
import de.linusdev.lutils.html.HtmlObject;
import de.linusdev.lutils.html.impl.StandardHtmlElement;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class LhtmlTemplate extends StandardHtmlElement implements LhtmlElement {

    private final @NotNull Map<String, EditableHtmlElement> placeHolders;

    public LhtmlTemplate(
            @NotNull Type tag,
            @NotNull List<@NotNull HtmlObject> content,
            @NotNull Map<String, HtmlAttribute> attributes,
            @NotNull Map<String, EditableHtmlElement> placeHolders
    ) {
        super(tag, content, attributes);
        this.placeHolders = placeHolders;
    }

    @Override
    public @NotNull EditableHtmlElement getPlaceholder(@NotNull String id) {
        return Objects.requireNonNull(placeHolders.get(id));
    }
}
