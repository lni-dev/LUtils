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

package de.linusdev.lutils.optional.impl;

import de.linusdev.lutils.optional.ListContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BasicListContainer<V> extends ListContainer<V> {
    public BasicListContainer(@Nullable Object key, boolean exists, @Nullable List<V> list) {
        super(key, exists, list);
    }

    @Override
    protected @NotNull <N> ListContainer<N> createNew(@Nullable List<N> newValue) {
        return new BasicListContainer<>(key, exists, newValue);
    }
}
