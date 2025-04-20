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

import de.linusdev.lutils.html.impl.StandardHtmlAttribute;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface HtmlAttributeType<V> {

    /**
     * {@link Object#hashCode() Hashcode} method all classes implementing this interface should use.
     */
    static int hashcode(@NotNull HtmlAttributeType<?> type) {
        return type.name().hashCode();
    }

    /**
     * Name or key of the attribute.
     */
    @NotNull String name();

    /**
     * Create a new attribute of this type with the {@link #defaultValue()}.
     */
    default @NotNull HtmlAttribute of() {
        return new StandardHtmlAttribute(this, defaultValue());
    }

    /**
     * Create a new attribute of this type with given {@code value}.
     */
    default @NotNull HtmlAttribute of(V value) {
        return new StandardHtmlAttribute(this, convertValue(value));
    }

    /**
     * Convert the value of given attribute to {@link V}.
     */
    @Nullable V convertValue(@NotNull HtmlAttribute attribute);

    /**
     * Converts a value {@link V} to a string.
     */
    @Nullable String convertValue(V value);

    /**
     * The default value of attributes of this type as string.
     */
    default @Nullable String defaultValue() {
        return null;
    }

    /**
     * Adds given {@code item} to given attribute value {@code current}. Does not work on every type. Implementation depends on the
     * attribute type.
     */
    default @Nullable String add(@Nullable String current, @NotNull String item) {
        throw new UnsupportedOperationException();
    }

    /**
     * Removes given {@code item} from given attribute value {@code current}. Does not work on every type. Implementation depends on the
     * attribute type.
     */
    default @Nullable String remove(@Nullable String current, @NotNull String item) {
        throw new UnsupportedOperationException();
    }
}
