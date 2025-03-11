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

package de.linusdev.lutils.os;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class InvalidOsArchError extends Error {

    public InvalidOsArchError(
            @Nullable String message,
            @NotNull OsArchitectureType @NotNull ... requiredOsArchitectures
    ) {
        super("Any of the following operating system architectures is required: " +
                Arrays.toString(requiredOsArchitectures) + ", but current os-arch is '" +
                OsUtils.CURRENT_ARCH + "'. " + (message == null ? "" : message)
        );
    }

    @SuppressWarnings("unused")
    public InvalidOsArchError(
            @Nullable String message,
            @NotNull List<@NotNull OsArchitectureType> requiredOsArchitectures
    ) {
        this(message, requiredOsArchitectures.toArray(OsArchitectureType[]::new));
    }
}
