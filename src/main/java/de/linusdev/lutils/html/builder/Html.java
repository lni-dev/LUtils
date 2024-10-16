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
import org.jetbrains.annotations.NotNull;

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
            @NotNull HtmlElementType<B> type, Consumer<B> adjuster
    ) {
        B builder = type.builder();
        adjuster.accept(builder);
        return builder.build();
    }

}
