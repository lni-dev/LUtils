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

package de.linusdev.lutils.pack.packs.simple;

import de.linusdev.lutils.pack.PackGroup;
import de.linusdev.lutils.pack.packs.AbstractZipPack;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.List;

public class SimpleZipPack extends AbstractZipPack {

    private final @NotNull String infoFileName;
    private final @NotNull List<PackGroup<?, ?>> allowedInventoryGroups;

    protected SimpleZipPack(
            @NotNull Path file,
            @NotNull String infoFileName,
            @NotNull List<PackGroup<?, ?>> allowedInventoryGroups
    ) {
        super(file);
        this.infoFileName = infoFileName;
        this.allowedInventoryGroups = allowedInventoryGroups;
    }

    @Override
    protected @NotNull String infoFileName() {
        return infoFileName;
    }

    @Override
    protected @NotNull List<PackGroup<?, ?>> allowedInventoryGroups() {
        return allowedInventoryGroups;
    }
}
