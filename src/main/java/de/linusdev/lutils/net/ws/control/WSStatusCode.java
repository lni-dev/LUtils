/*
 * Copyright (c) 2024 Linus Andera
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

package de.linusdev.lutils.net.ws.control;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @see WSStatusCodes
 */
public interface WSStatusCode {

    static @NotNull WSStatusCode of(int statusCode) {
        return () -> statusCode;
    }

    /**
     * Tests, whether the {@link #code()} of given {@link WSStatusCode}s are equal.
     */
    static boolean equals(@NotNull WSStatusCode that, @Nullable Object other) {
        if(that == other) return true;
        if(other == null) return false;
        if(!(other instanceof WSStatusCode otherCode)) return false;
        return otherCode.code() == that.code();
    }

    /**
     * Integer representation of the status code. Must be smaller than {@code 0xFFFF}.
     */
    int code();

}
