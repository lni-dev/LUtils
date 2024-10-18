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

package de.linusdev.lutils.html.builder;

import de.linusdev.lutils.html.HtmlElement;
import de.linusdev.lutils.html.HtmlElementType;
import de.linusdev.lutils.html.impl.HtmlDocType;
import de.linusdev.lutils.html.impl.HtmlPage;
import de.linusdev.lutils.html.impl.StandardHtmlElement;
import de.linusdev.lutils.html.impl.StandardHtmlElementTypes;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

public class Html {

    /**
     * Convenience method to create a {@link HtmlElement} of given {@code type}.
     * @param type element tag/type.
     * @param adjuster consumer to adjust the elements {@link HtmlElementBuilder builder}.
     * @return The built element.
     * @param <B> {@link HtmlElementBuilder} class, should be inferred by {@code type}.
     */
    public static <B extends HtmlElementBuilder> @NotNull HtmlElement buildElement(
            @NotNull HtmlElementType<B> type, @NotNull Consumer<B> adjuster
    ) {
        B builder = type.builder();
        adjuster.accept(builder);
        return builder.build();
    }

    /**
     * Convenience method to create a {@link HtmlPage} with the following elements:
     * <pre>{@code
     * <!doctype html>
     * <html>
     *     <head></head>
     *     <body></body>
     * </html>
     * }</pre>
     * @param headAdjuster consumer to adjust the {@code head} element.
     * @param bodyAdjuster consumer to adjust the {@code body} element.
     * @return the built {@link HtmlPage}.
     */
    public static @NotNull HtmlPage buildPage(
            @NotNull Consumer<StandardHtmlElement.Builder> headAdjuster,
            @NotNull Consumer<StandardHtmlElement.Builder> bodyAdjuster
    ) {
        return new HtmlPage(List.of(
                new HtmlDocType("html"),
                buildElement(StandardHtmlElementTypes.HTML, builder -> {
                    builder.addElement(StandardHtmlElementTypes.HEAD, headAdjuster);
                    builder.addElement(StandardHtmlElementTypes.BODY, bodyAdjuster);
                })
        ));
    }

}
