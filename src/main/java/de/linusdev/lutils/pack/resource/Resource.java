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

package de.linusdev.lutils.pack.resource;

import de.linusdev.lutils.id.Identifiable;
import de.linusdev.lutils.pack.AbstractPack;
import de.linusdev.lutils.pack.Group;
import de.linusdev.lutils.pack.Pack;
import de.linusdev.lutils.pack.Resources;
import org.jetbrains.annotations.NotNull;

/**
 * An identifiable item of a {@link ResourceCollection}. Used in combination with {@link Group}
 * and {@link Resources}.
 */
public interface Resource extends Identifiable {

    /**
     * The {@link Pack} this item was loaded from.
     */
    @NotNull AbstractPack getSource();


}
