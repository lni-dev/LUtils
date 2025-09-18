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
import de.linusdev.lutils.collections.EntryImpl;
import de.linusdev.lutils.data.ContentOnlyData;
import de.linusdev.lutils.data.Data;
import de.linusdev.lutils.other.iterator.IteratorUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record DataWrapper(@Nullable Object wrappedValue) implements ContentOnlyData {

    @Override
    public @NotNull Data add(@NotNull String key, @Nullable Object value) {
        throw new UnsupportedOperationException("Cannot add to a data wrapper");
    }

    @Override
    public @NotNull Data put(@NotNull String key, @Nullable Object value) {
        throw new UnsupportedOperationException("Cannot put on a data wrapper");
    }

    @Override
    public Object get(@NotNull String key) {
        throw new UnsupportedOperationException("Cannot get on a data wrapper");
    }

    @Override
    public @Nullable Data remove(@NotNull String key) {
        throw new UnsupportedOperationException("Cannot remove from a data wrapper");
    }

    @Override
    public int size() {
        return 1;
    }

    @Override
    public @NotNull BiIterator<String, Object> iterator() {
        return IteratorUtils.iteratorOf(new EntryImpl<>("wrapped-value", wrappedValue));
    }
}
