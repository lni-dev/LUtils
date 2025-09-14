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

import de.linusdev.lutils.pack.Group;
import de.linusdev.lutils.pack.ProcessingGroup;
import de.linusdev.lutils.pack.Resources;
import de.linusdev.lutils.pack.item.ResourceCollection;
import de.linusdev.lutils.pack.validation.ValidationResultBuilder;

public enum ProgressStage {

    /**
     * Load pack name, id, description, inventory, etc.
     */
    LOADING_PACKS_METADATA(0),

    /**
     * {@link ResourceCollection#clear() Clearing} resource collections.
     */
    CLEARING_PREVIOUS_DATA(1),

    /**
     * Reading the resources from the packs.
     */
    LOADING_PACKS_CONTENT(2),

    /**
     * Executing {@link ProcessingGroup processing groups}.
     */
    PROCESSING_PACKS_CONTENT(3),

    /**
     * {@link Group#validate(ValidationResultBuilder, Resources) Validating} resource collections.
     */
    VALIDATING_PACKS_CONTENT(4),

    /**
     * An error occurred. Removing the pack that caused the error and trying to reload.
     * The progress reported to {@link ProgressReporter#report(ProgressStage, int, int)} will always be {@code 0}
     * and {@code max} will be {@code 1}.
     */
    ERROR_RESTART(-1),

    ;

    public static final int COUNT = values().length - 1;

    public final int index;

    ProgressStage(int index) {
        this.index = index;
    }
}
