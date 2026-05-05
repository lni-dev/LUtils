/*
 * Copyright (c) 2025-2026 Linus Andera
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

import de.linusdev.lutils.ansi.sgr.SGR;
import de.linusdev.lutils.ansi.sgr.SGRParameters;
import de.linusdev.lutils.data.json.JsonMapImpl;
import de.linusdev.lutils.data.json.parser.JsonParser;
import de.linusdev.lutils.id.Identifier;
import de.linusdev.lutils.optional.Container;
import de.linusdev.lutils.other.log.Logger;
import de.linusdev.lutils.pack.errors.PackContentException;
import de.linusdev.lutils.pack.errors.PackException;
import de.linusdev.lutils.pack.loader.ProgressReporter;
import de.linusdev.lutils.pack.loader.ProgressStage;
import de.linusdev.lutils.pack.loader.ResourcesLoader;
import de.linusdev.lutils.pack.map.SimilarityResult;
import de.linusdev.lutils.pack.resource.Resource;
import de.linusdev.lutils.pack.resource.ResourceCollection;
import de.linusdev.lutils.pack.validation.ValidationResultBuilder;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Blocking;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * This class contains all resources of all {@link InventoriedPack InventoriedPacks} that have been enabled.
 * Instances of this class should only be created by the {@link ResourcesLoader} and then be only used to access the loaded
 * resources.
 *
 */
@SuppressWarnings("unused")
public class Resources {

    private final static @NotNull Logger LOG = Logger.getLogger();

    private final static @NotNull ResourceCollection<?> DISCARDED_GROUP = new DiscardedGroup();

    final static @NotNull JsonParser JSON_PARSER = new JsonParser()
            .setJsonBuilderSupplier(() -> new JsonMapImpl(new HashMap<>()))
            .setAllowComments(true, (parser, str) -> {});

    private final @NotNull HashMap<Group<?, ?>, ResourceCollection<?>> groups = new HashMap<>();

    @ApiStatus.Internal
    public Resources() {}

    /**
     * This function loads the resources from given {@code packs} on the current thread (blocking).
     * @param packs The packs to load the resources from.
     * @param processingGroups The enabled processing groups
     * @param reporter the {@link ProgressReporter}
     * @throws PackException while reading or parsing the pack or its content.
     */
    @Blocking
    @ApiStatus.Internal
    public void load(
            @NotNull Collection<InventoriedPack> packs,
            @NotNull List<ProcessingGroup<?, ?>> processingGroups,
            @NotNull ProgressReporter reporter
    ) throws PackException {

        LOG.info("Start loading pack content from the following packs: " +
                packs.stream().reduce("", (str, pack) -> str + "\n - " + pack.name(), (s, s2) -> s + s2));

        // Clear all groups
        reporter.report(ProgressStage.CLEARING_PREVIOUS_DATA, 0.0);

        List<Group<?, ?>> remove = new ArrayList<>();
        groups.forEach((group, o) -> {
            if(o == DISCARDED_GROUP) remove.add(group);
            else o.clear();
        });
        for (Group<?, ?> group : remove)
            groups.remove(group);

        reporter.report(ProgressStage.CLEARING_PREVIOUS_DATA, 1.0);

        // Load content from Packs
        int current = 0;
        reporter.report(ProgressStage.LOADING_PACKS_CONTENT, current, packs.size());
        for (@NotNull InventoriedPack pack : packs) {
            if(!pack.isLoaded()) throw new IllegalArgumentException("Packs must all be loaded.");

            LOG.debug("Reading content of pack '" + pack.name() + "'.");

            try {
                for (@NotNull PackGroup<?, ?> group : pack.allowedInventoryGroups()) {
                    groups.computeIfAbsent(group, key -> group.createResourceCollection());
                }

                for (PackGroup<?, ?> group : pack.inventory()) {
                    pack.loadGroup(LOG, group, groups.get(group));
                }

            } catch (PackException e) {
                throw e;
            } catch (Throwable t) {
                throw new PackContentException(pack, t);
            }

            reporter.report(ProgressStage.LOADING_PACKS_CONTENT, ++current, packs.size());
        }

        // Start processing
        current = 0;
        reporter.report(ProgressStage.PROCESSING_PACKS_CONTENT, current, processingGroups.size());
        for (ProcessingGroup<?, ?> processingGroup : processingGroups) {
            LOG.debug("Start processing group '" + processingGroup.name() + "'.");
            groups.computeIfAbsent(processingGroup, key -> processingGroup.createResourceCollection());

            // Check if all required groups are available.
            for (Group<?, ?> required : processingGroup.requiredGroups()) {
                if(groups.get(required) == null) {
                    throw new IllegalArgumentException("Processing group '" + processingGroup + "' requires group '" + required + "' which is not present!");
                }
            }

            // Process
            processingGroup.process(this);
            reporter.report(ProgressStage.PROCESSING_PACKS_CONTENT, ++current, processingGroups.size());
        }

        // Start validation
        current = 0;
        reporter.report(ProgressStage.VALIDATING_PACKS_CONTENT, current, groups.size());
        ValidationResultBuilder valResultBuilder = new ValidationResultBuilder(this);

        for (var entry : groups.entrySet()) {

            if(entry.getValue() == DISCARDED_GROUP) {
                reporter.report(ProgressStage.VALIDATING_PACKS_CONTENT, ++current, groups.size());
                continue;
            }

            LOG.debug("Start validating group '" + entry.getKey().name() + "'.");

            entry.getKey().validate(valResultBuilder, this);
            reporter.report(ProgressStage.VALIDATING_PACKS_CONTENT, ++current, groups.size());
        }

        valResultBuilder.throwOnError();
    }

    /**
     * This can be used by {@link ProcessingGroup ProcessingGroups} to discard a group that has been processed and should
     * be removed from this resources
     * @param group the group to discard.
     */
    public void discardGroup(@NotNull Group<?, ?> group) {
        groups.put(group, DISCARDED_GROUP);
    }

    /**
     * Get the resource collection of {@code group}.
     * @param group the group to get the resource collection of
     * @return the resource collection
     * @param <T> resource collection type
     * @throws IllegalArgumentException if no resource collection is present for given group
     * @throws IllegalStateException if the resource collection of given group has been {@link #discardGroup(Group) discarded}.
     */
    public <T extends ResourceCollection<?>> @NotNull T get(@NotNull Group<T, ?> group) {
        ResourceCollection<?> ret = groups.get(group);

        if(ret == DISCARDED_GROUP)
            throw new IllegalStateException("Group '" + group + "' has been discarded.");

        if(ret == null)
            throw new IllegalArgumentException("Group '" + group + "' is not present.");

        //noinspection unchecked
        return (T) ret;
    }

    /**
     * Get a debug string that shows all resources contained.
     * @param name the name of this instance.
     * @return debug string as described above
     */
    public @NotNull String getContainedItemsAsUserFriendlyString(@NotNull String name) {
        StringBuilder toLog = new StringBuilder("Items contained in Resources '" + name + "':");

        String green = new SGR().add(SGRParameters.FOREGROUND_GREEN).construct();

        for (var entry : groups.entrySet()) {
            if(entry.getValue() == DISCARDED_GROUP)
                continue;

            toLog.append("\n").append(entry.getKey()).append(":");

            for (Resource resource : entry.getValue()) {
                toLog.append("\n - '")
                        .append(green).append(resource.getIdentifier()).append(SGR.reset())
                        .append("' from ")
                        .append(resource.getSource().id());
            }
        }

        return toLog.toString();
    }

    private static class DiscardedGroup implements ResourceCollection<Resource> {

        private Error error() {
            return new Error("Cannot use this function on a discarded group.");
        }

        @Override
        public @Nullable Resource get(@NotNull Identifier id) {
            throw error();
        }

        @Override
        public @NotNull Container<Resource> grab(@NotNull Identifier id) {
            throw error();
        }

        @Override
        public boolean exists(@NotNull Identifier id) {
            throw error();
        }

        @Override
        public void clear() {
            throw error();
        }

        @Override
        public @NotNull Iterator<Resource> iterator() {
            throw error();
        }

        @Override
        public @NotNull List<SimilarityResult<Resource>> like(@NotNull Identifier id) {
            throw error();
        }
    }
}
