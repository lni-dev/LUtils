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

public class InvalidOsTypeError extends Error {

    public InvalidOsTypeError(
            @Nullable String message,
            @NotNull OsType @NotNull ... requiredOs
    ) {
        super("Any of the following operating systems is required: " +
                Arrays.toString(requiredOs) + ", but current os is '" +
                OsUtils.CURRENT_OS + "'. " + (message == null ? "" : message)
        );
    }

    @SuppressWarnings("unused")
    public InvalidOsTypeError(
            @Nullable String message,
            @NotNull List<@NotNull OsType> requiredOs
    ) {
        this(message, requiredOs.toArray(OsType[]::new));
    }
}
