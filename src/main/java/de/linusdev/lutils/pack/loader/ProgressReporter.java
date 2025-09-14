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

import org.jetbrains.annotations.NotNull;

/**
 * The {@link ResourcesLoader} will report its progress to an instance of this class. This
 * is useful to show the user a progressbar while resources are reloading.
 */
public interface ProgressReporter {

    /**
     * This one does nothing!
     */
    @NotNull ProgressReporter SILENT_REPORTER = new ProgressReporter() {
        @Override
        public void report(@NotNull ProgressStage stage, double progress) { }

        @Override
        public void finished(long millis) { }
    };

    /**
     * Can be used to convert the progress reported in {@link #report(ProgressStage, double)} to linear progress spanning
     * all {@link ProgressStage stages} equally.
     * @param stage the current stage.
     * @param progress the progress inside {@code stage}.
     * @return current progress across all stages from {@code 0.0} to {@code 1.0}.
     */
    static double calculateLinearProgress(@NotNull ProgressStage stage, double progress) {
        if(stage.index < 0)
            return 0; // Error stage

        double min = stage.index * (1d / ProgressStage.COUNT);
        double add = progress / ProgressStage.COUNT;
        return min + add;
    }

    /**
     * Reporting the current progress. Called by {@link #report(ProgressStage, int, int)}.
     * @param stage {@link ProgressStage}.
     * @param progress the current progress inside {@code stage}. from {@code 0.0} to {@code 1.0}.
     * @see #calculateLinearProgress(ProgressStage, double)
     */
    void report(@NotNull ProgressStage stage, double progress);

    /**
     * Called when the resource loader finished
     * @param millis the time the resource loading took in milliseconds.
     */
    void finished(long millis);

    /**
     * Reporting the current progress.
     * @param stage {@link ProgressStage}
     * @param current current progress inside {@code stage}.
     * @param max maximum possible progress inside {@code stage}
     */
    default void report(@NotNull ProgressStage stage, int current, int max) {
        report(stage, max == 0 ? 1.0 : (double) current / max);
    }

}
