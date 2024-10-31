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

import de.linusdev.lutils.other.ArgUtils;
import de.linusdev.lutils.result.BiResult;
import de.linusdev.lutils.result.TriResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.function.Function;

import static java.lang.Math.abs;
import static java.lang.Math.pow;

public class ChartMath {

    public enum LabelLeaning {
        HIGHER_THAN_MAX {
            @Override
            public double round(double value) {
                return Math.ceil(value);
            }
        },
        BEST {
            @Override
            public double round(double value) {
                return Math.round(value);
            }
        },
        LOWER_THAN_MAX {
            @Override
            public double round(double value) {
                return Math.floor(value);
            }
        };

        public abstract double round(double value);
    }

    /**
     * Works best when {@code preferredLabelCount} is {@code 1} to {@code 10}.
     * @param maxValue The maximum value of your diagram
     * @param minLabelCount smallest allowed label count, must be smaller than or equal to {@code preferredLabelCount}.
     * @param preferredLabelCount The preferred number of labels
     * @param maxLabelCount  highest allowed label count, must be greater than or equal to {@code preferredLabelCount}.
     * @param leaning whether the last label should be higher, lower or closest to {@code max}.
     * @return The label-value and the label count
     */
    public static BiResult<Double, Integer> calcBestLabelValue(
            @Range(from = 0, to = (long)Double.MAX_VALUE) double maxValue,
            @Range(from = 1, to = 1000) int minLabelCount,
            @Range(from = 1, to = 10) int preferredLabelCount,
            @Range(from = 1, to = 1000) int maxLabelCount,
            @NotNull LabelLeaning leaning
    ) {
        return choseBasedOnRating(
                minLabelCount,
                preferredLabelCount,
                maxLabelCount,
                count -> _calculateLabelValue(maxValue, count, leaning)
        );
    }

    /**
     * Quick and dirty method to calculate a label value. For better results see
     * <ul>
     *     <li>{@link #calcBestLabelValue(double, int, int, int, LabelLeaning)}</li>
     *     <li>{@link #calcBestAndReadableLabelValue(double, int, int, int, double)}</li>
     * </ul>
     * @param maxValue The maximum value of your diagram
     * @param labelCount The number of labels
     * @param leaning whether the last label should be higher, lower or closest to {@code max}.
     * @return The label-value
     */
    @SuppressWarnings("unused")
    public static double calcLabelValue(
            @Range(from = 0, to = (long)Double.MAX_VALUE) double maxValue,
            @Range(from = 1, to = 1000) int labelCount,
            @NotNull LabelLeaning leaning
    ) {
        return _calculateLabelValue(maxValue, labelCount, leaning).result1();
    }

    /**
     * Works best when {@code preferredLabelCount} is {@code 1} to {@code 10}.
     * @param maxValue The maximum value of your diagram
     * @param minLabelCount smallest allowed label count, must be smaller than or equal to {@code preferredLabelCount}.
     * @param preferredLabelCount The preferred number of labels
     * @param maxLabelCount  highest allowed label count, must be greater than or equal to {@code preferredLabelCount}.
     * @param leaning whether the last label should be higher ({@code 1.0}), lower ({@code -1.0}) or closest ({@code 0.0}) to {@code max}.
     * @return The label-value and the label count
     */
    public static BiResult<Double, Integer> calcBestAndReadableLabelValue(
            @Range(from = 0, to = (long)Double.MAX_VALUE) double maxValue,
            @Range(from = 1, to = 1000) int minLabelCount,
            @Range(from = 1, to = 10) int preferredLabelCount,
            @Range(from = 1, to = 1000) int maxLabelCount,
            @Range(from = -1, to = +1) double leaning
    ) {
        double sigLeaning = Math.signum(leaning);
        double difAdd = pow(abs(leaning), 5) * 5;
        double difMultiply = abs(leaning) + 1;

        return choseBasedOnRating(
                minLabelCount,
                preferredLabelCount,
                maxLabelCount,
                count -> _calcReadableLabelValue(maxValue, count, sigLeaning, difAdd, difMultiply)
        );
    }

    /**
     * Calculates a label value. Simply rounds to the next suitable value.
     */
    private static TriResult<Double, Double, Boolean> _calculateLabelValue(
            double maxValue,
            @Range(from = 1, to = 1000) int labelCount,
            @NotNull LabelLeaning leaning
    ) {
        double exactValue = maxValue / labelCount;
        double normalizeFactor = Math.pow(10, Math.floor(Math.log10(exactValue)));
        double normalizedValue = exactValue / normalizeFactor;
        double roundedValue = leaning.round(normalizedValue);
        double value = roundedValue * normalizeFactor;
        double rating = abs(maxValue - value*labelCount);
        //System.out.println("["+ labelCount + "]: rating=" + rating + ", normalizedMax=" + normalizedMax + ", labelMax=" + (roundedValue*labelCount) + ", fail=" + (value * (labelCount - 1) >= normalizedMax || value * (labelCount + 1) <= normalizedMax));
        return new TriResult<>(roundedValue * normalizeFactor, rating, value * (labelCount - 1) >= maxValue || value * (labelCount + 1) <= maxValue);
    }

    private static final double[] READABLE_FACTORS = new double[]{1,2,2.5,5,10};

    private static TriResult<Double, Double, Boolean> _calcReadableLabelValue(
            double maxValue,
            int labelCount,
            double sigLeaning,
            double difAdd,
            double difMultiply
    ) {
        double exactValue = maxValue / labelCount;
        double normalizeFactor = Math.pow(10, Math.floor( Math.log10(exactValue)));
        double normalizedMax = maxValue / normalizeFactor;

        double smallestDif = 1000000.0;
        double smallest = -1;
        //noinspection ForLoopReplaceableByForEach
        for (int i = 0; i < READABLE_FACTORS.length; i++) {
            double eMax = READABLE_FACTORS[i] * labelCount;
            double dif = normalizedMax - eMax;

            if(Math.signum(dif) == sigLeaning) {
                dif = (abs(dif) + difAdd) * difMultiply;
            } else {
                dif = abs(dif);
            }

            if(dif < smallestDif) {
                smallestDif = dif;
                smallest = READABLE_FACTORS[i];
            }
        }

        double value = smallest * normalizeFactor;
        double rating = abs(maxValue - (value*labelCount));
        //System.out.println("["+ labelCount + "]: rating=" + rating + ", normalizedMax=" + normalizedMax + ", labelMax=" + (smallest*labelCount) + ", fail=>" + (normalizedMax / labelCount));

        return new TriResult<>(value, rating,  value * (labelCount - 1) >= maxValue || value * (labelCount + 1) <= maxValue);
    }

    /**
     * Chooses a test-range around given {@code labelCount}. The test range will be smaller for a small {@code labelCount}.
     * @param labelCount preferred label count
     * @return test range between {@code 1} and {@code 10}.
     */
    protected static int choseTestRange(int labelCount) {
        return (int) Math.max(1, Math.round(2.0*((-(Math.exp(labelCount))/(0.25*labelCount*Math.exp(labelCount-0.5)+labelCount-1.0))+2.6) + LMath.clamp((labelCount-15)/4.0 , 0, 5)));
    }

    /**
     * Chooses the best label-value and count based on label rating. label count will be between
     * {@code minLabelCount} and {@code maxLabelCount}
     * @param baseLabelCount the preferred label count
     * @param labelCountToRatingFunction function that returns the label-value, a rating and a fail-state(true=failed) for given label count.
     * @param minLabelCount min label count (inclusive)
     * @param maxLabelCount max label count (inclusive)
     * @return label-value and label count
     */
    private static BiResult<Double, Integer> choseBasedOnRating(
            int minLabelCount,
            int baseLabelCount,
            int maxLabelCount,
            Function<Integer, TriResult<Double, Double, Boolean>> labelCountToRatingFunction
    ) {
        ArgUtils.requireGreater(minLabelCount, 0, "minLabelCount");
        ArgUtils.requireGreaterOrEqual(maxLabelCount, minLabelCount, "maxLabelCount");
        ArgUtils.requireGreaterOrEqual(baseLabelCount, minLabelCount, "baseLabelCount");
        ArgUtils.requireLessOrEqual(baseLabelCount, maxLabelCount, "baseLabelCount");

        if(minLabelCount == 1)
            minLabelCount = -10; // 1 might easily be inside the test range, but 1 is a hard limit to the bottom, so avoid increasing the test-range.

        int bestLabelCount = baseLabelCount;
        var res = labelCountToRatingFunction.apply(baseLabelCount);

        int testLabelCount;
        int testRange = choseTestRange(baseLabelCount);

        if(baseLabelCount + testRange > maxLabelCount) {
            // maxLabelCount is too small, so increase the range, so we check more lower label-counts
            testRange += maxLabelCount - (baseLabelCount + testRange);
        } else if (baseLabelCount - testRange < minLabelCount) {
            // minLabelCount is too big, so increase the range, so we check more higher label-counts
            testRange += minLabelCount - (baseLabelCount - testRange);
        }

        boolean failed = res.result3();
        for (int i = 1; i == 1 || (i < testRange && failed); i++) {
            if(i < minLabelCount || i >  maxLabelCount)
                continue;
            testLabelCount = baseLabelCount-i;
            if(testLabelCount > 1) {
                var resLess = labelCountToRatingFunction.apply(testLabelCount);
                if(resLess.result2() < res.result2() && !resLess.result3()) {
                    res = resLess;
                    bestLabelCount = testLabelCount;
                    failed = false;
                }
            }

            testLabelCount = baseLabelCount + i;
            var resMore = labelCountToRatingFunction.apply(testLabelCount);
            if(resMore.result2() < res.result2() && !resMore.result3()) {
                res = resMore;
                bestLabelCount = testLabelCount;
                failed = false;
            }
        }

        //if(failed) System.out.println("failed");

        return new BiResult<>(res.result1(), bestLabelCount);
    }

}
