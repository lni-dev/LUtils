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
import de.linusdev.lutils.html.impl.StandardHtmlAttribute;
import de.linusdev.lutils.other.parser.ParseException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Parser state, passed to {@link HtmlObjectParser}, so they can access {@link #injector}.
 */
public class HtmlParserState implements HtmlParserInjector {

    /**
     * @see HtmlParserInjector
     */
    private final @Nullable HtmlParserInjector injector;
    private final @NotNull HtmlParser parser;

    public HtmlParserState(@Nullable HtmlParserInjector injector, @NotNull HtmlParser parser) {
        this.injector = injector;
        this.parser = parser;
    }

    public @NotNull HtmlParser getParser() {
        return parser;
    }

    public @NotNull Registry getRegistry() {
        return parser.getRegistry();
    }

    /**
     * @see ParseException#ParseException(String)
     */
    public @NotNull ParseException fail(@NotNull HtmlReader reader, @NotNull String message) {
        return new ParseException(reader.getTracker(), message);
    }

    /**
     * @see ParseException#ParseException(char)
     */
    public @NotNull ParseException fail(@NotNull HtmlReader reader, char c) {
        return new ParseException(reader.getTracker(), c);
    }

    @Override
    public @Nullable HtmlAttribute onAttributeParsed(@NotNull HtmlAttributeType<?> type, @Nullable String value) {
        if(injector == null) return new StandardHtmlAttribute(type, value);
        return injector.onAttributeParsed(type, value);
    }

    @Override
    public int onStartParsingContent(@NotNull HtmlElementType<?> tag, @NotNull HtmlAttributeMap attributes) {
        if(injector == null) return 0;
        return injector.onStartParsingContent(tag, attributes);
    }

    @Override
    public @Nullable HtmlObject onObjectParsed(@NotNull HtmlObject parsed) {
        if(injector == null) return parsed;
        return injector.onObjectParsed(parsed);
    }

    @Override
    public void onEndParsingContent(int id) {
        if(injector == null) return;
        injector.onEndParsingContent(id);
    }
}
