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

package de.linusdev.lutils.html.parser;

import de.linusdev.lutils.html.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Injector called by {@link HtmlObjectParser}, when parsing html elements.
 */
public interface HtmlParserInjector {

    @Nullable HtmlAttribute onAttributeParsed(@NotNull HtmlAttributeType<?> type, @Nullable String value);

    /**
     * Called when content of a parent element is parsed. Called even if the parent element is a void element.
     * @param tag Parent element tag
     * @param attributes Parent element attribute
     * @return id given to parent element
     */
    int onStartParsingContent(
            @NotNull HtmlElementType<?> tag,
            @NotNull HtmlAttributeMap attributes
    );

    /**
     * A child element was parsed.
     * @param parsed the parsed object
     * @return {@code parsed} or a replacement or {@code null} if this element should be ignored.
     */
    @Nullable HtmlObject onObjectParsed(@NotNull HtmlObject parsed);

    /**
     * Finished parsing content of the parent with given {@code id}. Called even if the parent element is a void element.
     * @param id id returned by {@link #onStartParsingContent(HtmlElementType, HtmlAttributeMap)}.
     */
    void onEndParsingContent(int id);
}
