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

import de.linusdev.lutils.html.impl.StandardHtmlAttributeTypes;
import de.linusdev.lutils.html.lhtml.skeleton.LhtmlPageSkeleton;
import de.linusdev.lutils.html.parser.HtmlParser;
import de.linusdev.lutils.html.parser.ParseException;
import de.linusdev.lutils.html.parser.Registry;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Reader;

public class Lhtml {

    public static final @NotNull StandardHtmlAttributeTypes.StringType ATTR_TEMPLATE = new StandardHtmlAttributeTypes.StringType("lhtml-template");
    public static final @NotNull StandardHtmlAttributeTypes.StringType ATTR_PLACEHOLDER = new StandardHtmlAttributeTypes.StringType("lhtml-placeholder");

    public static final @NotNull HtmlParser PARSER = new HtmlParser(getRegistry().build());

    /**
     * {@link Registry.Builder} required when parsing any {@link LhtmlTemplate}.
     */
    public static @NotNull Registry.Builder getRegistry() {
        Registry.Builder builder = Registry.getDefault();
        builder.putElement(LhtmlHead.TYPE);
        builder.addAttribute(ATTR_TEMPLATE);
        builder.addAttribute(ATTR_PLACEHOLDER);

        return builder;
    }

    /**
     * Convenience method to parse a lhtml page.
     */
    public static @NotNull LhtmlPageSkeleton parsePage(@NotNull Reader reader) throws IOException, ParseException {
        return LhtmlPage.parse(PARSER, reader);
    }

}
