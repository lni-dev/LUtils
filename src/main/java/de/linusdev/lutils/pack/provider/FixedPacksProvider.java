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

package de.linusdev.lutils.pack.provider;

import de.linusdev.lutils.other.debug.DebugInfoStringBuilder;
import de.linusdev.lutils.pack.AbstractPack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Provides a fixed list of packs.
 */
@SuppressWarnings("ClassCanBeRecord")
public class FixedPacksProvider implements PackProvider {

    private final @NotNull List<@NotNull AbstractPack> packs;

    public FixedPacksProvider(@NotNull AbstractPack @NotNull ... packs) {
        this.packs = List.of(packs);
    }

    @Override
    public @NotNull List<@NotNull AbstractPack> provide() {
        return packs;
    }

    @Override
    public @NotNull String name() {
        return getClass().getSimpleName();
    }

    @Override
    public @NotNull String debug_info_string(int maxDepth) {
        return new DebugInfoStringBuilder(this, name(), maxDepth)
                .addList("packs", packs)
                .build();
    }
}
