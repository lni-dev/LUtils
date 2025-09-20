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

package de.linusdev.lutils.pack.resource;

import de.linusdev.lutils.id.Identifier;
import de.linusdev.lutils.optional.Container;
import de.linusdev.lutils.optional.impl.BasicContainer;
import de.linusdev.lutils.pack.Group;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Resource collection used in {@link Group}.
 * @param <R> the type of the contained resources.
 */
public interface ResourceCollection<R extends Resource> extends Iterable<R>{

    /**
     * Get a resource with given id.
     * @param id the id of the resource
     * @return the resource or {@code null} if no resource is available for given {@code id}.
     */
    @Nullable R get(@NotNull Identifier id);

    /**
     * {@link #get(Identifier)} wrapped by a {@link Container}. The container's value exists, if {@link #get(Identifier) get()}
     * returns {@code null}.
     */
    default @NotNull Container<R> grab(@NotNull Identifier id) {
        R resource = get(id);
        return new BasicContainer<>(id, resource != null, resource);
    }

    /**
     * Checks if a resource with given {@code id} exists.
     */
    default boolean exists(@NotNull Identifier id) {
        return get(id) != null;
    }

    /**
     * Will clear this resource collection. Used if resources are reloaded.
     */
    @ApiStatus.Internal
    void clear();

}
