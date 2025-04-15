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

package de.linusdev.lutils.html.lhtml;

import de.linusdev.lutils.html.HtmlObject;
import de.linusdev.lutils.html.HtmlObjectType;
import de.linusdev.lutils.html.HtmlUtils;
import de.linusdev.lutils.html.parser.HtmlWritingState;
import de.linusdev.lutils.other.str.ConstructableString;
import de.linusdev.lutils.other.str.PartsString;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

public class LHtmlPlaceholderText implements HtmlObject {

    private final @NotNull ConstructableString text;

    /**
     * Replace-values map.
     */
    private Map<String, String> replaceValues;

    public LHtmlPlaceholderText(
            @NotNull ConstructableString text
    ) {
        this.text = text;
    }

    /**
     * Must be called by the parent lhtml-element to the set replace-values map
     * which will be used, when {@link #text} is {@link PartsString#construct(PartsString.Resolver) constructed}.
     * @param replaceValues the parent's replace-values map.
     */
    @ApiStatus.Internal
    public void setReplaceValues(@NotNull Map<String, String> replaceValues) {
        this.replaceValues = replaceValues;
    }


    @Override
    public @NotNull HtmlObjectType type() {
        return HtmlObjectType.TEXT;
    }

    @Override
    public @NotNull HtmlObject copy() {
        return new LHtmlPlaceholderText(text);
    }

    @Override
    public void write(@NotNull HtmlWritingState state, @NotNull Writer writer) throws IOException {
        assert isReplaceValuesNotNull();
        String processed = HtmlUtils.escape(text.construct(replaceValues::get), false);
        processed = processed.replaceAll("\n", "\n" + state.getIndent());
        writer.write(processed);
    }

    private boolean isReplaceValuesNotNull() {
        return replaceValues != null;
    }
}
