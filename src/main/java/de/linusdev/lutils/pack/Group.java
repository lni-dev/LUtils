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

import de.linusdev.lutils.pack.item.Resource;
import de.linusdev.lutils.pack.item.ResourceCollection;
import de.linusdev.lutils.pack.validation.ResourceBoundValidationResultBuilder;
import de.linusdev.lutils.pack.validation.ValidationResultBuilder;
import org.jetbrains.annotations.NotNull;

/**
 * An instance of this class represents a group containing resources of type {@link I}.
 * Before the resources are loaded/created a custom resource collection is created for each group using {@link #createResourceCollection()}.
 * All resources of a group will be added to its {@link ResourceCollection}.
 * @param <C> The {@link ResourceCollection} type of this group.
 * @param <I> The {@link Resource} type of this group.
 */
public abstract class Group<C extends ResourceCollection<I>, I extends Resource> {

    /**
     * The name of this group
     */
    private final @NotNull String name;

    /**
     *
     * @param name see {@link #name}
     */
    protected Group(@NotNull String name) {
        this.name = name;
    }

    /**
     * Creates a new resource collection for this group.
     * @return a new resource collection for this group.
     */
    public abstract @NotNull C createResourceCollection();

    /**
     * This method should validate given {@code item} which is part of a resource collection of type {@link C} of this group.
     * {@code resources} can be used to query all groups resource collections in case of dependencies. Validation errors
     * should be reported to {@code validation}.
     * <br><br>
     * This function is called in {@link #validate(ValidationResultBuilder, Resources) validate} for every item in the resource
     * collection of this group.
     * @param validation use this to report validation errors
     * @param resources to query resource collections in case of dependencies
     * @param item the item of type {@link I} to validate.
     */
    public abstract void validateItem(
            @NotNull ResourceBoundValidationResultBuilder validation,
            @NotNull Resources resources,
            @NotNull I item
    );

    /**
     * Validate the {@link ResourceCollection} belonging to this group instance.
     * <br><br>
     * The default implementation will call {@link #validateItem(ResourceBoundValidationResultBuilder, Resources, Resource) validateItem}
     * for every item of the {@link ResourceCollection} belonging to this group instance.
     * @param validation use this to report validation errors
     * @param resources to query the resource collection of this group or of dependencies.
     */
    public void validate(@NotNull ValidationResultBuilder validation, @NotNull Resources resources) {
        C collection = resources.get(this);
        for (I resource : collection) {
            validateItem(validation.withFixedErrorSource(resource), resources, resource);
        }
    }

    /**
     * @see #name
     */
    public @NotNull String name() {
        return name;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return name;
    }

}
