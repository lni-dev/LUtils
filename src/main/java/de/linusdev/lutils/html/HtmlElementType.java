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

package de.linusdev.lutils.html;

import de.linusdev.lutils.html.builder.HtmlElementBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A html element type/tag.
 * @param <B> the {@link HtmlElementBuilder} class.
 */
public interface HtmlElementType<B extends HtmlElementBuilder> {

    /**
     * Checks whether the given {@link HtmlElementType}s are equal, meaning they have the same {@link #name()}.
     */
    static boolean equals(@NotNull HtmlElementType<?> that, @Nullable HtmlElementType<?> other) {
        if(that == other) return true;
        if(other == null) return false;
        return that.name().equals(other.name());
    }

    /**
     * Creates a string of the following form {@code tagName/className}.
     */
    static @NotNull String toString(@NotNull HtmlElementType<?> tag) {
        return tag.name() + "/" + tag.getClass().getSimpleName();
    }

    /**
     * tag name
     */
    @NotNull String name();

    /**
     * A new builder for an element of this type.
     */
    @NotNull B builder();

    /**
     * A parser for an element of this type.
     */
    @NotNull HtmlObjectParser<? extends HtmlElement> parser();

}
