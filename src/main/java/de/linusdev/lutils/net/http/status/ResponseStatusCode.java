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

package de.linusdev.lutils.net.http.status;

import de.linusdev.lutils.net.http.method.RequestMethod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public interface ResponseStatusCode {

    /**
     * Map of {@link ResponseStatusCode} used in {@link #of(int, String)}.
     */
    @NotNull Map<Integer, ResponseStatusCode> STATUS_CODES = new HashMap<>();

    /**
     *
     * @param statusCode status code as int
     * @param name Status code name or reason phrase.
     * @return {@link ResponseStatusCode} from {@link #STATUS_CODES} if {@link #getStatusCode() statusCode} and
     * {@link #getName() name} match or a new {@link ResponseStatusCode} with given {@code statusCode} and given
     * {@code name}.
     */
    static @NotNull ResponseStatusCode of(int statusCode, @Nullable String name) {
       ResponseStatusCode ret = STATUS_CODES.get(statusCode);
       if(ret != null && ret.getName().equals(name))
           return ret;

       return new ResponseStatusCode() {

           @Override
           public int getStatusCode() {
               return statusCode;
           }

           @Override
           public @NotNull String getName() {
               return name == null ? "" : name;
           }
       };
    }

    /**
     * Same as {@link #of(int, String)}, with {@code statusCode} parsed to int.
     */
    static @NotNull ResponseStatusCode of(@NotNull String statusCode, @Nullable String name) {
       try {
           return of(Integer.parseInt(statusCode), name);
       } catch (NumberFormatException e) {
           throw new IllegalArgumentException("Status code must be an integer, but was: '" + statusCode + "'", e);
       }
    }

    /**
     * Checks if the given {@link ResponseStatusCode statusCodes} are the same.
     * @param first {@link ResponseStatusCode}
     * @param second {@link ResponseStatusCode}
     * @return {@code true} if {@link #getStatusCode()} of both {@link RequestMethod methods} are equal
     */
    static boolean equals(@NotNull ResponseStatusCode first, @NotNull ResponseStatusCode second) {
        return first == second || first.getStatusCode() == second.getStatusCode();
    }

    /**
     * {@link ResponseStatusCodeType} of this status code.
     */
    default @NotNull ResponseStatusCodeType getType() {
        return ResponseStatusCodeType.of(getStatusCode());
    }

    /**
     * Status code as int.
     */
    int getStatusCode();

    /**
     * Name or reason-phrase of this status code.
     */
    @NotNull String getName();

}
