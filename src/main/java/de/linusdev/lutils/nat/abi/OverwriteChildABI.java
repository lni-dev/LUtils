/*
 * Copyright (c) 2024-2025 Linus Andera
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

package de.linusdev.lutils.nat.abi;

import de.linusdev.lutils.nat.struct.annos.StructureSettings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public enum OverwriteChildABI {
    /**
     * ABI of the children will not be overwritten
     */
    NO_OVERWRITE,

    /**
     * ABI of the children will be overwritten. If {@link StructureSettings} of a child does not
     * support {@link StructureSettings#customLayoutOption()}, this child's ABI will not be overwritten
     * and <b>no</b> exception will be thrown.
     */
    TRY_OVERWRITE,

    /**
     * ABI of the children will be overwritten. If {@link StructureSettings} of any child does not
     * support {@link StructureSettings#customLayoutOption()} and the ABI differs of that child's ABI, an exception will be thrown.
     */
    FORCE_OVERWRITE

    ;

    public static @NotNull OverwriteChildABI max(@Nullable OverwriteChildABI first, @Nullable OverwriteChildABI second) {
        if(first == TRY_OVERWRITE || second == TRY_OVERWRITE) {
            return TRY_OVERWRITE;
        }

        if(first == FORCE_OVERWRITE || second == FORCE_OVERWRITE) {
            return FORCE_OVERWRITE;
        }

        return NO_OVERWRITE;
    }
}
