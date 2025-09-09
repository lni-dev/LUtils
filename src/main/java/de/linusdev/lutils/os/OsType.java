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

import java.util.Locale;

public enum OsType {
    LINUX(
            "Linux",
            new String[] { "linux" },
            new String[] { "lib" },
            new String[] { "so" }
    ),
    WINDOWS(
            "Windows",
            new String[] { "windows" },
            new String[] { "" },
            new String[] { "dll" }
    ),
    MACOS(
            "MacOS",
            new String[] { "mac" },
            new String[] { "lib" },
            new String[] { "dylib", "jnilib" }
    ),
    UNKNOWN(
            "Unknown",
            new String[0],
            new String[0],
            new String[0]
    );

    public static @NotNull OsType of(@NotNull String name) {
        name = name.toLowerCase(Locale.ROOT);
        for (OsType type : values()) {
            for (@NotNull String fix : type.fixes) {
                if(name.contains(fix)) return type;
            }
        }

        return UNKNOWN;
    }

    private final @NotNull String name;
    private final @NotNull String @NotNull [] fixes;
    private final @NotNull String @NotNull [] possibleNativeLibraryFilePrefixes;
    private final @NotNull String @NotNull [] defaultNativeLibraryFileEnding;

    OsType(
            @NotNull String name,
            @NotNull String @NotNull [] fix,
            @NotNull String @NotNull [] possibleNativeLibraryFilePrefixes,
            @NotNull String @NotNull [] defaultNativeLibraryFileEnding
    ) {
        this.name = name;
        this.fixes = fix;
        this.possibleNativeLibraryFilePrefixes = possibleNativeLibraryFilePrefixes;
        this.defaultNativeLibraryFileEnding = defaultNativeLibraryFileEnding;
    }

    public @NotNull String getName() {
        return name;
    }

    public String @NotNull [] getPossibleNativeLibraryFilePrefixes() {
        return possibleNativeLibraryFilePrefixes;
    }

    public String @NotNull [] getDefaultNativeLibraryFileEnding() {
        return defaultNativeLibraryFileEnding;
    }
}
