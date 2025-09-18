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
import de.linusdev.lutils.optional.Container;
import de.linusdev.lutils.optional.impl.BasicContainer;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class JsonListImpl implements Json {

    private final @NotNull List<Entry<String, Object>> entries;

    public JsonListImpl(@NotNull List<Entry<String, Object>> entries) {
        this.entries = entries;
    }

    @Override
    public Object _get(@NotNull String key) {
        for (Entry<String, Object> entry : entries) {
            if(key.equals(entry.key()))
                return entry.value() == null ? Json.NULL : entry.value();
        }
        return null;
    }

    @Override
    public Object get(@NotNull String key) {
        for (Entry<String, Object> entry : entries) {
            if(key.equals(entry.key()))
                return entry.value();
        }
        return null;
    }

    @Override
    public @NotNull Container<Object> grab(@NotNull String key) {
        for (Entry<String, Object> entry : entries) {
            if(key.equals(entry.key())) {
                if(entry.value() == null)
                    return new BasicContainer<>(key, true, null);
                else
                    return new BasicContainer<>(key, true, entry.value());
            }
        }

        return new BasicContainer<>(key, false, null);
    }

    @Override
    public @NotNull BiIterator<String, Object> iterator() {
        return BiIterator.of(this.entries);
    }

    @Override
    public String toString() {
        return Json.toString(this);
    }

    @Override
    public int size() {
        return entries.size();
    }
}
