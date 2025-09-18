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

package de.linusdev.lutils.data.impl;

import de.linusdev.lutils.collections.BiIterator;
import de.linusdev.lutils.collections.Entry;
import de.linusdev.lutils.collections.EntryImpl;
import de.linusdev.lutils.data.Data;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.Map;

@SuppressWarnings("ClassCanBeRecord")
public class DataMapImpl implements Data {

    private final @NotNull Map<String, Object> entries;

    public DataMapImpl(@NotNull Map<String, Object> entries) {
        this.entries = entries;
    }

    @Override
    public @NotNull Data add(@NotNull String key, @Nullable Object value) {
        entries.put(key, value);
        return this;
    }

    @Override
    public @NotNull Data put(@NotNull String key, @Nullable Object value) {
        return add(key, value);
    }

    @Override
    public Object get(@NotNull String key) {
        return entries.get(key);
    }

    @Override
    public @Nullable Data remove(@NotNull String key) {
        entries.remove(key);
        return this;
    }

    @Override
    public int size() {
        return entries.size();
    }

    @Override
    public @NotNull BiIterator<String, Object> iterator() {
        return new BiIterator<>() {
            final Iterator<Map.Entry<String, Object>> it = entries.entrySet().iterator();

            @Override
            public @NotNull Entry<String, Object> next() {
                var entry = it.next();
                return new EntryImpl<>(entry.getKey(), entry.getValue());
            }

            @Override
            public boolean hasNext() {
                return it.hasNext();
            }
        };
    }
}
