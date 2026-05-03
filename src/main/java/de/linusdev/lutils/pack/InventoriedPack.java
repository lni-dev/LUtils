/*
 * Copyright (c) 2026 Linus Andera
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

import de.linusdev.lutils.other.log.Logger;
import de.linusdev.lutils.pack.errors.PackContentException;
import de.linusdev.lutils.pack.resource.ResourceCollection;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

public interface InventoriedPack extends Pack {

    /**
     * Inventory of this pack. E.g. all {@link PackGroup PackGroups} contained in this pack.
     */
    @NotNull Collection<PackGroup<?, ?>> inventory();

    /**
     * {@link PackGroup Inventory groups} that are allowed to be in this group.
     * If other groups are contained in the pack, it can still be loaded. All groups which
     * are not contained in this list will be ignored.
     * @return The list of allowed inventory groups.
     */
    @NotNull List<PackGroup<?, ?>> allowedInventoryGroups();

    /**
     * Load all resources from the given {@code group} into {@code col}.
     * @param log {@link Logger} to log while loading the group
     * @param group the group from {@link #inventory()} to load
     * @param col the {@link ResourceCollection} to store the loaded resources.
     * @throws PackContentException if problems with the pack contents appear.
     */
    void loadGroup(
            @NotNull Logger log,
            @NotNull PackGroup<?, ?> group,
            @NotNull ResourceCollection<?> col
    ) throws PackContentException;

}
