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

import de.linusdev.lutils.html.HtmlAttribute;
import de.linusdev.lutils.html.HtmlAttributeType;
import de.linusdev.lutils.other.str.ConstructableString;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class LhtmlPlaceholderAttribute implements HtmlAttribute {

    private final @NotNull HtmlAttributeType<?> type;
    /**
     * The value, which can be constructed.
     */
    private final @NotNull ConstructableString value;
    /**
     * Replace-values map.
     */
    private Map<String, String> replaceValues;

    public LhtmlPlaceholderAttribute(
            @NotNull HtmlAttributeType<?> type,
            @NotNull ConstructableString value
    ) {
        this.type = type;
        this.value = value;
    }

    /**
     * Must be called by the parent lhtml-element to the set replace-values map
     * which will be used, when {@link #value} is {@link ConstructableString#construct(Map) constructed}.
     * @param replaceValues the parent's replace-values map.
     */
    @ApiStatus.Internal
    public void setReplaceValues(@NotNull Map<String, String> replaceValues) {
        this.replaceValues = replaceValues;
    }

    @Override
    public @NotNull HtmlAttributeType<?> type() {
        return type;
    }

    @Override
    public @Nullable String value() {
        assert isReplaceValuesNotNull();
        return value.construct(replaceValues);
    }

    private boolean isReplaceValuesNotNull() {
        return replaceValues != null;
    }

    @Override
    public @NotNull LhtmlPlaceholderAttribute copy() {
        return new LhtmlPlaceholderAttribute(type, value);
    }
}
