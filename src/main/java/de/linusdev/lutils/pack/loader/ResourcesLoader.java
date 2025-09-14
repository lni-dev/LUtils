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

package de.linusdev.lutils.pack.loader;

import de.linusdev.llog.LLog;
import de.linusdev.llog.base.LogInstance;
import de.linusdev.lutils.pack.AbstractPack;
import de.linusdev.lutils.pack.Pack;
import de.linusdev.lutils.pack.ProcessingGroup;
import de.linusdev.lutils.pack.Resources;
import de.linusdev.lutils.pack.errors.PackException;
import de.linusdev.lutils.pack.errors.PackLoadingException;
import de.linusdev.lutils.pack.provider.PackProvider;
import org.jetbrains.annotations.Blocking;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class is used to load resources from {@link AbstractPack AbstractPacks} and store them in a {@link Resources}
 * instance.
 */
public class ResourcesLoader {

    private final static @NotNull LogInstance LOG = LLog.getLogInstance();

    private final @NotNull List<ProcessingGroup<?, ?>> processingGroups;
    private final @NotNull List<AbstractPack> defaultPacks;
    private final @NotNull List<PackProvider> providers;
    private final @NotNull ReloadListener reloadListener;
    private final @NotNull List<AbstractPack> availablePacks;

    private final @NotNull Set<AbstractPack> activePacks;

    private final @NotNull Resources resources;

    /**
     * Create a new resource loader.
     * @param defaultPacks the default packs that are always active. If any of these throw an error the loading will fail.
     * @param processingGroups the processing groups
     * @param providers the providers for additional packs, which can be {@link #enablePack(AbstractPack) enabled}.
     * @param reloadListener a reload listener which will be notified before a reload will be started and after a reload finished.
     */
    public ResourcesLoader(
            @NotNull List<AbstractPack> defaultPacks,
            @NotNull List<ProcessingGroup<?, ?>> processingGroups,
            @NotNull List<PackProvider> providers,
            @NotNull ReloadListener reloadListener
    ) {
        this.processingGroups = processingGroups;
        this.defaultPacks = defaultPacks;
        this.providers = providers;
        this.availablePacks = new ArrayList<>();
        this.activePacks = new HashSet<>();

        this.resources = new Resources();
        this.reloadListener = reloadListener;
    }

    /**
     * The currently active packs.
     */
    public @NotNull Set<AbstractPack> getActivePacks() {
        return Set.copyOf(activePacks);
    }

    /**
     * The available packs. {@link #reloadAvailablePacks(ProgressReporter)} or {@link #reload(ProgressReporter)} must be
     * called first.
     */
    public @NotNull List<AbstractPack> getAvailablePacks() {
        return List.copyOf(availablePacks);
    }

    /**
     * Enable a pack from {@link #getAvailablePacks()}.
     * @param pack the pack to enable. Must be contained int {@link #getAvailablePacks()}.
     */
    public void enablePack(@NotNull AbstractPack pack) {
        if(!availablePacks.contains(pack))
            throw new IllegalArgumentException("This pack is not available.");

        activePacks.add(pack);
    }

    /**
     * Disable a pack. If the pack was not enabled, nothing happens.
     * @param pack the pack to disable.
     */
    public void disablePack(@NotNull AbstractPack pack) {
        activePacks.remove(pack);
    }

    /**
     * Reload the available packs.
     * @param reporter The progress reporter for the reload. Only the stage
     * {@link ProgressStage#LOADING_PACKS_METADATA LOADING_PACKS_METADATA} will be reported.
     */
    @Blocking
    public void reloadAvailablePacks(@NotNull ProgressReporter reporter) {
        _reloadAvailablePacks(reporter, true);
    }

    /**
     * Reload the available packs.
     * @param reporter progress reporter to report to
     * @param finish whether to finish the progress reporter after reloading.
     */
    @Blocking
    private synchronized void _reloadAvailablePacks(@NotNull ProgressReporter reporter, boolean finish) {
        long startTime = System.currentTimeMillis();
        availablePacks.clear();
        for (PackProvider provider : providers) {
            try {
                availablePacks.addAll(provider.provide());
            } catch (PackException e) {
                LOG.throwable("Pack provider '" + provider.name() + "' failed. Provider info: " + provider.debug_info_string(), e);
            }
        }

        int packCount = defaultPacks.size() + availablePacks.size();
        int current = 0;
        reporter.report(ProgressStage.LOADING_PACKS_METADATA, current, packCount);

        // Load default packs
        try {
            for (Pack defaultPack : defaultPacks) {
                defaultPack.load();
                reporter.report(ProgressStage.LOADING_PACKS_METADATA, ++current, packCount);
            }
        } catch (PackLoadingException e) {
            throw new Error("Failed to load default packs.", e);
        }

        // Load all available packs
        List<AbstractPack> toRemove = new ArrayList<>(0);
        for (AbstractPack availablePack : availablePacks) {
            try {
                availablePack.load();
                reporter.report(ProgressStage.LOADING_PACKS_METADATA, ++current, packCount);
            } catch (PackLoadingException e) {
                LOG.throwable("Failed to load a custom pack! The pack cannot be enabled.", e);
                toRemove.add(availablePack);
            }
        }

        toRemove.forEach(availablePacks::remove);

        // Check if all active packs are actually available
        for (AbstractPack activePack : activePacks) {
            if(!availablePacks.contains(activePack)) {
                LOG.debug("Removing pack '" + activePack + "' from active packs, because it is not available anymore.");
                toRemove.add(activePack);
            }

        }

        // Remove all packs that cannot be activated
        toRemove.forEach(activePacks::remove);

        if(finish) {
            reporter.finished(System.currentTimeMillis() - startTime);
        }
    }

    /**
     * Reload resources from packs
     * @param reporter {@link ProgressReporter} to report to.
     */
    @Blocking
    private synchronized void _reload(@NotNull ProgressReporter reporter) {

        ArrayList<AbstractPack> packs = new ArrayList<>(defaultPacks);
        packs.addAll(activePacks);

        try {
            resources.load(packs, processingGroups, reporter);

        } catch (PackException e) {

            if(activePacks.isEmpty()) {
                //Active packs was already empty. This must be due to a default pack.
                throw new Error("Resource loading failed with only default packs active.", e);
            }

            AbstractPack pack = e.getPack();
            boolean isActivePack = activePacks.contains(pack);

            if(pack == null || !isActivePack) {
                // An Unknown pack caused the error. Remove all active packs and try again.
                activePacks.clear();
                LOG.throwable(new Exception("Resource reload failed! ", e));
                LOG.info("Retrying with only default packs!");
                reporter.report(ProgressStage.ERROR_RESTART, 0, 1);
                _reload(reporter);
                return;
            }

            activePacks.remove(pack);
            LOG.throwable(new Exception("Resource reload failed! ", e));
            LOG.info("Retrying with pack '" + pack.name() + "' removed.");
            reporter.report(ProgressStage.ERROR_RESTART, 0, 1);
            _reload(reporter);

        }
    }

    /**
     * Reload resources from all active packs.
     * Will {@link #reloadAvailablePacks(ProgressReporter) reload available packs}.
     * May disable packs if they cause errors.
     * Will fail if default packs cause errors.
     * @param reporter the reporter to report to. Will be {@link ProgressReporter#finished(long) finished}.
     */
    @Blocking
    public void reload(@NotNull ProgressReporter reporter) {
        reloadListener.beforeReload(this, resources);
        long startTime = System.currentTimeMillis();

        // Ensure all active packs are available.
        _reloadAvailablePacks(reporter, false);

        _reload(reporter);

        long time = System.currentTimeMillis() - startTime;
        reporter.finished(time);
        reloadListener.afterReload(this, resources);
    }

    /**
     * The {@link Resources} instance containing all resources loaded from the active packs.
     * The returned instance can be stored and will be changed when a {@link #reload(ProgressReporter) reload} happens.
     */
    public @NotNull Resources getResources() {
        return resources;
    }

    /**
     * RELOAD LISTENER!
     */
    public interface ReloadListener {
        void beforeReload(@NotNull ResourcesLoader loader, @NotNull Resources resources);
        void afterReload(@NotNull ResourcesLoader loader, @NotNull Resources resources);
    }
}
