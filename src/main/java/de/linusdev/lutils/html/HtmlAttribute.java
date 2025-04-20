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

import de.linusdev.lutils.html.parser.HtmlWritingState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.Writer;

public interface HtmlAttribute extends HtmlWritable{

    /**
     * The type of the attribute
     */
    @NotNull HtmlAttributeType<?> type();

    /**
     * Attribute value or {@code null} if this attribute has no value.
     */
    @Nullable String value();

    /**
     * Set the value of this attribute.
     * @param value the new value
     * @return might return the same attribute with the changed value or a new attribute with the changed value.
     */
    @NotNull HtmlAttribute setValue(@Nullable String value);

    /**
     * Add to the value of this attribute. Not supported by all attribute types. Specific implementation
     * depends on the {@link HtmlAttributeType type} of this attribute.
     * @param item the item to add
     * @return might return the same attribute with the changed value or a new attribute with the changed value.
     */
    default @NotNull HtmlAttribute addToValue(@NotNull String item) {
        return setValue(type().add(value(), item));
    }

    /**
     * Add from the value of this attribute. Not supported by all attribute types. Specific implementation
     * depends on the {@link HtmlAttributeType type} of this attribute.
     * @param item the item to remove
     * @return might return the same attribute with the changed value or a new attribute with the changed value.
     */
    default @NotNull HtmlAttribute removeFromValue(@NotNull String item) {
        return setValue(type().remove(value(), item));
    }

    /**
     * Create a copy of this attribute. May return {@code this} if it is a constant attribute.
     */
    @NotNull HtmlAttribute copy();

    @Override
    default void write(@NotNull HtmlWritingState state, @NotNull Writer writer) throws IOException {
        writer.append(type().name());

        String value = value();
        if(value != null) {
            writer.append("=\"").append(HtmlUtils.escape(value, true)).append("\"");
        }
    }
}
