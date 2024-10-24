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

import de.linusdev.lutils.html.*;
import de.linusdev.lutils.html.parser.HtmlWritingState;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Map;

public class LhtmlPlaceholder implements EditableHtmlElement {

    private final @NotNull String id;
    private final @NotNull EditableHtmlElement actual;

    public LhtmlPlaceholder(@NotNull String id, @NotNull EditableHtmlElement actual) {
        this.id = id;
        this.actual = actual;
    }

    @Override
    public @NotNull HtmlElementType<?> tag() {
        return actual.tag();
    }

    @Override
    public @NotNull List<@NotNull HtmlObject> content() {
        return actual.content();
    }

    @Override
    public @NotNull Map<String, HtmlAttribute> attributes() {
        return actual.attributes();
    }

    @Override
    public @NotNull HtmlObjectType type() {
        return HtmlObjectType.ELEMENT;
    }

    @Override
    public @NotNull LhtmlPlaceholder copy() {
        return new LhtmlPlaceholder(id, actual.copy());
    }

    public @NotNull String getId() {
        return id;
    }

    @Override
    public void write(@NotNull HtmlWritingState state, @NotNull Writer writer) throws IOException {
        actual.write(state, writer);
    }
}
