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

package de.linusdev.lutils.other;

import org.jetbrains.annotations.NotNull;

public class ArgUtils {

    /**
     * Throw an {@link IllegalArgumentException} if {@code value} is not greater than {@code limit}.
     * @param name variable name
     * @return {@code true}
     */
    public static boolean requireGreater(int value, int limit, @NotNull String name) {
        if(value <= limit)
            throw new IllegalArgumentException(name + " must be greater than " + limit + ", but was " + value);
        return true;
    }

    /**
     * Throw an {@link IllegalArgumentException} if {@code value} is not at least {@code epsilon} greater than {@code limit}.
     * @param name variable name
     * @return {@code true}
     */
    public static boolean requireGreater(double value, double limit, double epsilon, @NotNull String name) {
        if(value <= (limit + epsilon))
            throw new IllegalArgumentException(name + " must be at least " + epsilon + " greater than " + limit + ", but was " + value);
        return true;
    }

    /**
     * Throw an {@link IllegalArgumentException} if {@code value} is not greater than or equal to {@code limit}.
     * @param name variable name
     * @return {@code true}
     */
    public static boolean requireGreaterOrEqual(int value, int limit, @NotNull String name) {
        if(value < limit)
            throw new IllegalArgumentException(name + " must be greater than or equal to " + limit + ", but was " + value);
        return true;
    }

    /**
     * Throw an {@link IllegalArgumentException} if {@code value} is not less than {@code limit}.
     * @param name variable name
     * @return {@code true}
     */
    public static boolean requireLess(int value, int limit, @NotNull String name) {
        if(value >= limit)
            throw new IllegalArgumentException(name + " must be less than " + limit + ", but was " + value);
        return true;
    }

    /**
     * Throw an {@link IllegalArgumentException} if {@code value} is not less than or equal to {@code limit}.
     * @param name variable name
     * @return {@code true}
     */
    public static boolean requireLessOrEqual(int value, int limit, @NotNull String name) {
        if(value > limit)
            throw new IllegalArgumentException(name + " must be less than or equal to" + limit + ", but was " + value);
        return true;
    }

}
