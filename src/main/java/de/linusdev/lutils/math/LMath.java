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

package de.linusdev.lutils.math;

import org.jetbrains.annotations.Range;

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

    /**
     * Floored logarithm of basis two of given unsigned {@code value}.
     * If given {@code value} is {@code 0}, {@code 0} is returned.
     * @param value value to calculate the logaritm of the basis two of, as described above.
     * @return {@code value == 0 ? 0 : floor(log2(value))}
     */
    public static int intLog2(int value) {
        if(value == 0)
            return 0;
        return Integer.numberOfTrailingZeros(Integer.highestOneBit(value));
    }

    /**
     * {@link #interpolate(double, double, double) Interpolate} between {@code minOut} and {@code maxOut}
     * based on {@code pos}, which will be normalized to {@code 0.0} and {@code 1.0} based on {@code minPos}
     * and {@code maxPos}. {@code pos} will automatically be {@link #clamp(double, double, double) clamped}
     * between {@code minPos} and {@code maxPos}.
     */
    public static double interpolate(double minPos, double maxPos, double minOut, double maxOut, double pos) {
        pos = clamp(pos, minPos, maxPos) - minPos;
        double normalizedPos = pos / (maxPos - minPos);
        return interpolate(minOut, maxOut, normalizedPos);
    }

    /**
     * Interpolates linearly between {@code minOut} and {@code maxOut} depending on {@code pos}.
     * If {@code pos} is {@code 0.0}, {@code minOut} will be returned.
     * If {@code pos} is {@code 1.0}, {@code maxOut} will be returned.
     * @param minOut minimum output
     * @param maxOut maximum output
     * @param pos position between {@code 0.0} and {@code 1.0}
     * @return linear interpolation between {@code minOut} and {@code maxOut} depending on {@code pos}.
     */
    public static double interpolate(double minOut, double maxOut, @Range(from = 0, to = 1)  double pos) {
        return (minOut * (1d - pos)) + (maxOut * pos);
    }

    /**
     * Returns {@code true} if given {@code number} is even. That means {@code (number % 2) == 0}.
     */
    public static boolean isEven(int number) {
        return (number & 0x1) == 0x0;
    }

    /**
     * Returns {@code true} if given {@code number} is not even. That means {@code (number % 2) == 1}.
     */
    public static boolean isOdd(int number) {
        return (number & 0x1) == 0x1;
    }

    /**
     * Returns {@code true} if given {@code number} is even. That means {@code (number % 2) == 0}.
     */
    public static boolean isEven(long number) {
        return (number & 0x1L) == 0x0L;
    }

    /**
     * Returns {@code true} if given {@code number} is not even. That means {@code (number % 2) == 1}.
     */
    public static boolean isOdd(long number) {
        return (number & 0x1L) == 0x1L;
    }

}
