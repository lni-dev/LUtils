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

import de.linusdev.lutils.pack.resource.Resource;
import de.linusdev.lutils.pack.resource.ResourceCollection;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * A Processing Group it a special group that processes other non processing {@link Group groups}.
 * @param <C> The {@link ResourceCollection} type of this group.
 * @param <I> The resource type of this group.
 */
public abstract class ProcessingGroup<C extends ResourceCollection<I>, I extends Resource> extends Group<C, I> {

    protected ProcessingGroup(@NotNull String groupName) {
        super(groupName);
    }

    /**
     * Process the required groups' resource collections contained in {@code resources} and fill {@code resourceCollection}.
     * @param resources {@link Resources} containing the resource collections of all groups mentioned in {@link #requiredGroups()}.
     * @param resourceCollection the group of this
     */
    protected abstract void process(@NotNull Resources resources, @NotNull C resourceCollection);

    /**
     * Calls {@link #process(Resources, ResourceCollection)}
     * @param resources {@link Resources} containing the resource collections of all groups mentioned in {@link #requiredGroups()}.
     */
    protected void process(@NotNull Resources resources) {
        process(resources, resources.get(this));
    }

    /**
     * The groups this group depends on. Currently only {@link PackGroup PackInvGroups} are supported.
     */
    public abstract @NotNull List<Group<?, ?>> requiredGroups();
}
