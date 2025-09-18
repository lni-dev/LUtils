/*
 * Copyright (c) 2025 Linus Andera
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

package de.linusdev.lutils.data.json;

import de.linusdev.lutils.collections.BiIterator;
import de.linusdev.lutils.collections.Entry;
import de.linusdev.lutils.collections.EntryImpl;
import de.linusdev.lutils.optional.Container;
import de.linusdev.lutils.optional.impl.BasicContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.Map;

public class JsonMapImpl implements Json, JsonBuilder {

    protected @NotNull Map<String, Object> entries;

    public JsonMapImpl(@NotNull Map<String, Object> entries) {
        this.entries = entries;
    }

    protected Object internalGet(@NotNull String key) {
        return entries.get(key);
    }

    @Override
    public Object _get(@NotNull String key) {
        return entries.get(key);
    }

    @Override
    public Object get(@NotNull String key) {
        Object value = internalGet(key);
        if(value == NULL)
            return null;
        return value;
    }

    @Override
    public @NotNull Container<Object> grab(@NotNull String key) {
        Object value = internalGet(key);
        return new BasicContainer<>(
                key,
                value != null,
                value == NULL ? null : value
        );
    }

    @Override
    public @NotNull JsonBuilder add(@NotNull String key, @Nullable Object value) {
        entries.put(key, value == null ? NULL : value);
        return this;
    }

    @Override
    public @NotNull Json build() {
        return this;
    }

    @Override
    public @NotNull BiIterator<String, Object> iterator() {
        return new BiIterator<>() {
            final Iterator<Map.Entry<String, Object>> it = entries.entrySet().iterator();

            @Override
            public @NotNull Entry<String, Object> next() {
                Map.Entry<String, Object> entry = it.next();
                return new EntryImpl<>(entry.getKey(), entry.getValue());
            }

            @Override
            public boolean hasNext() {
                return it.hasNext();
            }
        };
    }

    @Override
    public int size() {
        return entries.size();
    }

    @Override
    public String toString() {
        return Json.toString(this);
    }
}
