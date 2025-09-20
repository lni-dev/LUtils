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

package de.linusdev.lutils.pack.map;

import de.linusdev.lutils.id.Identifier;
import de.linusdev.lutils.pack.resource.Resource;
import de.linusdev.lutils.pack.resource.ResourceCollection;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.function.Supplier;

/**
 * Basic {@link ResourceCollection} implementation.
 * @param <R> resource type
 */
public class ResourceMap<R extends Resource> implements ResourceCollection<R> {

    private final @NotNull HashMap<String, R> values = new HashMap<>();

    public void put(R object) {
        values.put(Identifier.toString(object.getIdentifier()), object);
    }

    public R computeIfAbsent(Identifier id, Supplier<R> computer) {
        return values.computeIfAbsent(Identifier.toString(id), key -> computer.get());
    }

    public R get(@NotNull String id) {
        return values.get(id);
    }

    public R get(@NotNull Identifier id) {
        return values.get(Identifier.toString(id));
    }

    public @NotNull Collection<R> collection() {
        return values.values();
    }

    public int size() {
        return values.size();
    }

    @Override
    public void clear() {
        values.clear();
    }

    @Override
    public @NotNull Iterator<R> iterator() {
        return values.values().iterator();
    }
}
