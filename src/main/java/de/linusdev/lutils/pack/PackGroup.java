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

package de.linusdev.lutils.pack;

import de.linusdev.lutils.data.json.Json;
import de.linusdev.lutils.interfaces.TriConsumer;
import de.linusdev.lutils.pack.map.ResourceMap;
import de.linusdev.lutils.pack.resource.Resource;
import de.linusdev.lutils.pack.resource.ResourceCollection;
import de.linusdev.lutils.pack.validation.ResourceBoundValidationResultBuilder;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;

/**
 * A Group which is filled by {@link AbstractPack AbstractPacks}.
 * @param <G> The resource collection type of this group.
 * @param <I> The resource type of this group.
 * @see #newResourceMapGroup(String, BiFunction, TriConsumer)
 * @see Group
 */
public abstract class PackGroup<G extends ResourceCollection<I>, I extends Resource> extends Group<G, I> {

    /**
     * Create a new group with a {@link ResourceMap} as {@link ResourceCollection}.
     * @param name the group name
     * @param converter the converter to convert the resource from {@link Json} to {@link I}.
     * @param validator validate the resource
     *                  (See {@link Group#validateItem(ResourceBoundValidationResultBuilder, Resources, Resource) validateItem()}).
     * @return A new group as described above.
     * @param <I> the resource type.
     */
    public static <I extends Resource> @NotNull PackGroup<ResourceMap<I>, I> newResourceMapGroup(
            @NotNull String name,
            @NotNull BiFunction<AbstractPack, Json, I> converter,
            @NotNull TriConsumer<ResourceBoundValidationResultBuilder, Resources, I> validator
    ) {
        return new PackGroup<>(name) {

            @Override
            public @NotNull ResourceMap<I> createResourceCollection() {
                return new ResourceMap<>();
            }

            @Override
            public void validateItem(@NotNull ResourceBoundValidationResultBuilder result, @NotNull Resources resources, @NotNull I item) {
                validator.consume(result, resources, item);
            }

            @Override
            public void addToResourceCollection(@NotNull ResourceMap<I> collection, @NotNull Json data, @NotNull AbstractPack source) {
                collection.put(converter.apply(source, data));
            }
        };
    }

    protected PackGroup(@NotNull String name) {
        super(name);
    }

    /**
     * Add the resource represented by {@code data} to the resource collection of this group.
     * @param collection the resource collection of this group
     * @param data the resource parsed from json
     * @param source the {@link AbstractPack} the resource is from
     */
    public abstract void addToResourceCollection(@NotNull G collection, @NotNull Json data, @NotNull AbstractPack source);

    @ApiStatus.Internal
    void _addToResourceCollection(@NotNull ResourceCollection<?> collection, @NotNull Json data, @NotNull AbstractPack source) {
        //noinspection unchecked
        addToResourceCollection((G) collection, data, source);
    }
}
