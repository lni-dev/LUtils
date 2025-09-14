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

package de.linusdev.lutils.pack.validation;

import de.linusdev.lutils.id.Identifier;
import de.linusdev.lutils.pack.Group;
import de.linusdev.lutils.pack.Pack;
import de.linusdev.lutils.pack.item.Resource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * An interface to a {@link ValidationResultBuilder} where the error source is a fixed resource.
 * Additionally, this class adds a lot of convenience methods to ease validation.
 */
@SuppressWarnings("ClassCanBeRecord")
public class ResourceBoundValidationResultBuilder {

    protected final @NotNull ValidationResultBuilder parent;
    protected final @NotNull Resource source;

    public ResourceBoundValidationResultBuilder(@NotNull ValidationResultBuilder parent, @NotNull Resource source) {
        this.parent = parent;
        this.source = source;
    }

    /**
     * Add an error with given {@code message}.
     * @param message error message
     * @return this
     */
    public @NotNull ResourceBoundValidationResultBuilder error(@NotNull String message) {
        parent.error(message, source);
        return this;
    }

    /**
     * Add an error, that the resource with given {@code id} is missing in given {@code group}
     * @param group the group the missing resource is from.
     * @param id the id of the missing resource.
     * @return this
     */
    public @NotNull ResourceBoundValidationResultBuilder errorResourceMissing(@NotNull Group<?, ?> group, @NotNull Identifier id) {
        error("Group '" + group.name() + "' is missing an item with the id '" + id + "'.");
        return this;
    }

    /**
     * Add an error, that the file at given {@code location} is missing in given {@code pack}.
     * @param pack the pack the missing file should be in
     * @param location the location where the missing file should be.
     * @return this
     */
    public @NotNull ResourceBoundValidationResultBuilder errorFileMissing(@NotNull Pack pack, @NotNull String location) {
        error("Pack '" + pack.id() + "' is missing file '" + location + "'.");
        return this;
    }

    /**
     * Add an error if the resource with given {@code id} does not exist in given {@code group}.
     * @param group the group where the resource is required.
     * @param id the id of the required resource.
     * @return this
     */
    public @NotNull ResourceBoundValidationResultBuilder requireResource(@NotNull Group<?, ?> group, @Nullable Identifier id) {
        if(id != null && !parent.resources.get(group).exists(id)) {
            errorResourceMissing(group, id);
        }
        return this;
    }

    /**
     * Add an error if given {@code location} does not exist in given {@code pack}.
     * @param pack the pack where the file must be located in.
     * @param location the location of the file.
     * @return this
     */
    public @NotNull ResourceBoundValidationResultBuilder requireFile(@NotNull Pack pack, @Nullable String location) {
        if(location != null && !pack.exists(location)) {
            errorFileMissing(pack, location);
        }
        return this;
    }

}
