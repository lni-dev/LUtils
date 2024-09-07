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

package de.linusdev.lutils.math;

/**
 * LUtils custom math functions
 */
public class LMath {

    /**
     * {@link Math#PI PI} as float.
     */
    public static final float PIf = (float) Math.PI;

    /**
     * Clamp {@code value} between {@code min} and {@code max}.
     * @param value input value
     * @param min minimum
     * @param max maximum
     * @return {@code value} or {@code min}/{@code max} if {@code value} is smaller/bigger than {@code min}/{@code max} respectively.
     */
    public static int clamp(int value, int min, int max) {
        return Math.min(Math.max(value, min), max);
    }

    /**
     * Clamp {@code value} between {@code min} and {@code max}.
     * @param value input value
     * @param min minimum
     * @param max maximum
     * @return {@code value} or {@code min}/{@code max} if {@code value} is smaller/bigger than {@code min}/{@code max} respectively.
     */
    public static long clamp(long value, long min, long max) {
        return Math.min(Math.max(value, min), max);
    }

    /**
     * Clamp {@code value} between {@code min} and {@code max}.
     * @param value input value
     * @param min minimum
     * @param max maximum
     * @return {@code value} or {@code min}/{@code max} if {@code value} is smaller/bigger than {@code min}/{@code max} respectively.
     */
    public static float clamp(float value, float min, float max) {
        return Math.min(Math.max(value, min), max);
    }

    /**
     * Clamp {@code value} between {@code min} and {@code max}.
     * @param value input value
     * @param min minimum
     * @param max maximum
     * @return {@code value} or {@code min}/{@code max} if {@code value} is smaller/bigger than {@code min}/{@code max} respectively.
     */
    public static double clamp(double value, double min, double max) {
        return Math.min(Math.max(value, min), max);
    }

    /**
     * Smaller of the two values (compared as unsigned)
     */
    public static int minUnsigned(int first, int second) {
        return Integer.compareUnsigned(first, second) == 1 ? second : first;
    }

    /**
     * Larger of the two values (compared as unsigned)
     */
    public static int maxUnsigned(int first, int second) {
        return Integer.compareUnsigned(first, second) == -1 ? second : first;
    }

    /**
     * Clamp {@code value} between {@code min} and {@code max} (compared as unsigned).
     * @param value input value
     * @param min minimum
     * @param max maximum
     * @return {@code value} or {@code min}/{@code max} if {@code value} is smaller/bigger than {@code min}/{@code max} respectively.
     */
    public static int clampUnsigned(int value, int min, int max) {
        return minUnsigned(maxUnsigned(value, min), max);
    }

}
