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

package de.linusdev.lutils.html.impl;

import de.linusdev.lutils.html.HtmlAttribute;
import de.linusdev.lutils.html.HtmlAttributeType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record StandardHtmlAttribute(
        @NotNull HtmlAttributeType<?> type,
        @Nullable String value
) implements HtmlAttribute {

    @Override
    public @NotNull HtmlAttribute setValue(@Nullable String value) {
        return new StandardHtmlAttribute(type, value);
    }

    @Override
    public @NotNull HtmlAttribute addToValue(@NotNull String item) {
        return new StandardHtmlAttribute(type, type.add(value, item));
    }

    @Override
    public @NotNull HtmlAttribute removeFromValue(@NotNull String item) {
        return new StandardHtmlAttribute(type, type.remove(value, item));
    }

    @Override
    public @NotNull HtmlAttribute copy() {
        return this; // fine because it is final
    }
}
