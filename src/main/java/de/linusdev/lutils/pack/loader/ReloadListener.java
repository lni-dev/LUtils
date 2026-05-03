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

package de.linusdev.lutils.pack.loader;

import de.linusdev.lutils.pack.Resources;
import org.jetbrains.annotations.NotNull;

/**
 * Reload listener for {@link ResourcesLoader}.
 */
public interface ReloadListener {

    @NotNull ReloadListener SOUT_RELOAD_LISTENER = new ReloadListener() {
        long start;
        @Override
        public void beforeReload(@NotNull ResourcesLoader loader, @NotNull Resources resources) {
            System.out.println("Resource reload started");
            start = System.currentTimeMillis();
        }

        @Override
        public void afterReload(@NotNull ResourcesLoader loader, @NotNull Resources resources) {
            System.out.println("Resource reload finished in " + (System.currentTimeMillis()-start) + " ms.");
        }
    };

    /**
     * This one does nothing.
     */
    @NotNull ReloadListener SILENT_RELOAD_LISTENER = new ReloadListener() {
        @Override public void beforeReload(@NotNull ResourcesLoader loader, @NotNull Resources resources) { }
        @Override public void afterReload(@NotNull ResourcesLoader loader, @NotNull Resources resources) { }
    };

    void beforeReload(@NotNull ResourcesLoader loader, @NotNull Resources resources);

    void afterReload(@NotNull ResourcesLoader loader, @NotNull Resources resources);
}