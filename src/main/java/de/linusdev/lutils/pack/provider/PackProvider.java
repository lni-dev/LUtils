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

import de.linusdev.lutils.other.debug.Debuggable;
import de.linusdev.lutils.pack.AbstractPack;
import de.linusdev.lutils.pack.errors.PackException;
import de.linusdev.lutils.pack.loader.ResourcesLoader;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * A provider of packs for the {@link ResourcesLoader}. To get debug information about a pack
 * provider instance see {@link Debuggable.InfoString#debug_info_string() debug_info_string()}.
 */
public interface PackProvider extends Debuggable.InfoString {

    /**
     * Provide a list of packs.
     * @throws PackException if the provider cannot provide due to errors.
     */
    @NotNull List<@NotNull AbstractPack> provide() throws PackException;

    /**
     * The name of this pack provider. If possible unique.
     */
    @NotNull String name();

}
