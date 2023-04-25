/*
 * Copyright (c) 2023 Linus Andera
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

package de.linusdev.lutils.http_WIP.method;

import org.jetbrains.annotations.NotNull;

/**
 * HTTP Request Method
 */
public interface RequestMethod {

    static @NotNull RequestMethod of(@NotNull String method) {
        try {
            return  Methods.valueOf(method);
        } catch (IllegalArgumentException e) {
            return () -> method;
        }
    }


    /**
     * Checks if the given {@link RequestMethod methods} are the same.
     * @param first {@link RequestMethod}
     * @param second {@link RequestMethod}
     * @return {@code true} if {@link #getName()} of both {@link RequestMethod methods} are the same
     */
    @SuppressWarnings("unused")
    static boolean equals(@NotNull RequestMethod first, @NotNull RequestMethod second) {
        return first == second || first.getName().equals(second.getName());
    }

    /**
     * Name of this {@link RequestMethod}. for example: "GET"
     * @return name as {@link String}
     */
    @NotNull String getName();

}
