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

package de.linusdev.lutils.os;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("UnusedReturnValue")
public class OsUtils {

    /**
     * The current {@link OsType}.
     */
    public static final @NotNull OsType CURRENT_OS = OsType.of(System.getProperty("os.name"));

    /**
     * The current {@link OsArchitectureType}.
     */
    public static final @NotNull OsArchitectureType CURRENT_ARCH = OsArchitectureType.of(System.getProperty("os.arch"));

    /**
     * Ensures that {@link #CURRENT_OS} is contained in given {@code osTypes}.
     * @param reason The reason added to the exception
     * @param osTypes allowed operating system types, that do not throw an exception.
     * @return {@code true}
     * @throws InvalidOsTypeError if {@link #CURRENT_OS} is not contained in given {@code osTypes}.
     */
    public static boolean requireAnyOfOsTypes(@Nullable String reason, @NotNull OsType @NotNull ... osTypes) {
        boolean statisfied = false;

        for (@NotNull OsType osType : osTypes) {
            statisfied |= CURRENT_OS == osType;
        }

        if(!statisfied)
            throw new InvalidOsTypeError(reason, osTypes);

        return true;
    }

    /**
     * @see #requireAnyOfOsTypes(String, OsType...)
     */
    public static boolean requireAnyOfOsTypes(@NotNull OsType @NotNull ... osTypes) {
        return requireAnyOfOsTypes(null, osTypes);
    }

    /**
     * @see #requireAnyOfOsTypes(String, OsType...)
     */
    public static boolean requireOsType(@Nullable String reason, @NotNull OsType os) {
        return requireAnyOfOsTypes(reason, os);
    }

    /**
     * @see #requireAnyOfOsTypes(String, OsType...)
     */
    public static boolean requireOsType(@NotNull OsType os) {
        return requireOsType(null, os);
    }

    /**
     * Ensures that {@link #CURRENT_ARCH} is contained in given {@code osArchitectures}.
     * @param reason The reason added to the exception
     * @param osArchitectures allowed operating system architecture types, that do not throw an exception.
     * @return {@code true}
     * @throws InvalidOsArchError if {@link #CURRENT_ARCH} is not contained in given {@code osArchitectures}.
     */
    public static boolean requireAnyOfOsArch(@Nullable String reason, @NotNull OsArchitectureType @NotNull ... osArchitectures) {
        boolean statisfied = false;

        for (@NotNull OsArchitectureType osArch: osArchitectures) {
            statisfied |= CURRENT_ARCH == osArch;
        }

        if(!statisfied)
            throw new InvalidOsArchError(reason, osArchitectures);

        return true;
    }

    /**
     * @see #requireAnyOfOsArch(String, OsArchitectureType...)
     */
    public static boolean requireAnyOfOsArch(@NotNull OsArchitectureType @NotNull ... osTypes) {
        return requireAnyOfOsArch(null, osTypes);
    }

    /**
     * @see #requireAnyOfOsArch(String, OsArchitectureType...)
     */
    public static boolean requireOsArch(@Nullable String reason, @NotNull OsArchitectureType os) {
        return requireAnyOfOsArch(reason, os);
    }

    /**
     * @see #requireAnyOfOsArch(String, OsArchitectureType...)
     */
    public static boolean requireOsArch(@NotNull OsArchitectureType os) {
        return requireOsArch(null, os);
    }

}
