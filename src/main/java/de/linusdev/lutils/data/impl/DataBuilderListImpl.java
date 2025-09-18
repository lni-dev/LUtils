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
import de.linusdev.lutils.data.DataBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@SuppressWarnings("ClassCanBeRecord")
public class DataBuilderListImpl implements DataBuilder {

    protected final @NotNull List<Entry<String, Object>> entries;

    public DataBuilderListImpl(@NotNull List<Entry<String, Object>> entries) {
        this.entries = entries;
    }

    @Override
    public @NotNull DataBuilder add(@NotNull String key, @Nullable Object value) {
        entries.add(new EntryImpl<>(key, value));
        return this;
    }

    @Override
    public @NotNull DataBuilder put(@NotNull String key, @Nullable Object value) {
        remove(key);
        return add(key, value);
    }

    @Override
    public Object get(@NotNull String key) {
        for (Entry<String, Object> entry : entries) {
            if(key.equals(entry.getKey()))
                return entry.getValue();
        }
        return null;
    }

    @Override
    public @Nullable DataBuilder remove(@NotNull String key) {
        for (int i = 0; i < entries.size(); i++) {
            Entry<String, Object> entry = entries.get(i);
            if(key.equals(entry.getKey())) {
                entries.remove(i);
                return this;
            }
        }

        return this;
    }

    @Override
    public int size() {
        return entries.size();
    }

    @Override
    public @NotNull BiIterator<String, Object> iterator() {
        return BiIterator.of(entries);
    }
}
