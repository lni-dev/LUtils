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

package de.linusdev.lutils.pack.errors;

import de.linusdev.lutils.pack.AbstractPack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PackLoadingException extends PackException {
    public PackLoadingException(@NotNull AbstractPack pack, @Nullable Throwable cause) {
        super(pack, "Could not load pack '" + pack + "'", cause);
    }

    public PackLoadingException(@NotNull AbstractPack pack, @NotNull String message, @Nullable Throwable cause) {
        super(pack, "Could not load pack '" + pack + "'. " + message, cause);
    }
}
