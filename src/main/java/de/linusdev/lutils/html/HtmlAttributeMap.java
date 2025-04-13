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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class HtmlAttributeMap implements Iterable<HtmlAttribute> {

    private final @NotNull Map<String, HtmlAttribute> attributes;

    public HtmlAttributeMap() {
        this.attributes = new HashMap<>();
    }

    /**
     * Add or replace an attribute.
     * @param attribute to add/replace.
     * @return the old attribute or {@code null}.
     */
    public @Nullable HtmlAttribute put(@NotNull HtmlAttribute attribute) {
        return attributes.put(attribute.type().name(), attribute);
    }

    /**
     * Remove attribute with given {@code type}.
     * @return the old attribute or {@code null} if no attribute was removed.
     */
    public @Nullable HtmlAttribute remove(@NotNull HtmlAttributeType<?> type) {
        return attributes.remove(type.name());
    }

    /**
     * Get attribute with given {@code type}. Returns {@code null} if no such attribute exists.
     */
    public @Nullable HtmlAttribute get(@NotNull HtmlAttributeType<?> type) {
        return attributes.get(type.name());
    }

    /**
     * Get the {@link HtmlAttribute#value() value} of the attribute with given {@code type} or {@code null} if no such attribute exists.
     */
    public <V> V getValue(@NotNull HtmlAttributeType<V> type) {
        HtmlAttribute attribute = attributes.get(type.name());
        if(attribute == null)
            return null;
        return type.convertValue(attribute);
    }

    /**
     * {@link HtmlAttributeType#add(String, String) adds} given {@code item} to attribute with given {@code type}. If no
     * attribute with given {@code type} exists a {@link HtmlAttributeType#of() new attribute will be created}.
     */
    public void addToAttribute(@NotNull HtmlAttributeType<?> type, @NotNull String item) {
        HtmlAttribute attr = get(type);

        if(attr == null)
            attr = type.of();

        put(attr.addToValue(item));
    }

    /**
     * {@link HtmlAttributeType#add(String, String) removes} given {@code item} from attribute with given {@code type}. If no
     * attribute with given {@code type} exists a {@link HtmlAttributeType#of() new attribute will be created}.
     */
    public void removeFromAttribute(@NotNull HtmlAttributeType<?> type, @NotNull String item) {
        HtmlAttribute attr = get(type);

        if(attr == null)
            attr = type.of();

        put(attr.removeFromValue(item));
    }

    @Override
    public @NotNull Iterator<HtmlAttribute> iterator() {
        return attributes.values().iterator();
    }

    /**
     * Attribute count.
     */
    public int size() {
        return attributes.size();
    }
}
