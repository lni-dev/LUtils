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

import java.util.Locale;

public enum OsArchitectureType {
    /**
     * 32-bit Intel or AMD processor.
     */
    X86("x86"),
    /**
     * 64-bit Intel or AMD processor.
     */
    AMD64("amd64", "x86_64"),
    /**
     * 32-bit ARM processor.
     */
    ARM("arm"),
    /**
     * 64-bit ARM processor.
     */
    AARCH64("aarch64"),

    /**
     * Unknown.
     */
    UNKNOWN
    ;

    public static @NotNull OsArchitectureType of(@NotNull String name) {
        name = name.toLowerCase(Locale.ROOT);
        for (OsArchitectureType type : values()) {
            for (@NotNull String arch : type.names) {
                if(name.equals(arch)) return type;
            }
        }

        return UNKNOWN;
    }

    private final @NotNull String @NotNull [] names;

    OsArchitectureType(@NotNull String @NotNull ... names) {
        this.names = names;
    }
}
