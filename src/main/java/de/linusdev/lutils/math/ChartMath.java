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

import de.linusdev.lutils.result.BiResult;

public class ChartMath {

    /**
     *
     * @param max max char value
     * @param preferredLabelCount preferred count of labels
     * @param leaningFactor between 0.0 and 1.0.<br>
     *                      <ul>
     *                          <li>
     *                              1.0 means: Lean towards higher value labels / fewer labels<br>
     *                              side effect: the highest label is more likely to be greater than max. It is more likely that the chart has some unused space at the top
     *                          </li>
     *                          <li>
     *                              0.0 means: Lean towards lower value labels / more labels<br>
     *                              side effect: the highest label is more likely to be less than max.
     *                          </li>
     *                      </ul>
     * @return label-value and label count
     */
    public static BiResult<Double, Integer> calcLabelValue(double max, int preferredLabelCount, double leaningFactor) {
        double unnormalizedValue = max / preferredLabelCount;
        double logV = Math.log10(unnormalizedValue);
        double normalizeFactor = Math.pow(10, Math.floor(logV));
        double normalizedValue = unnormalizedValue / normalizeFactor;

        //System.out.println(normalizedValue);
        if(normalizedValue >= leaningFactor) {
            normalizedValue = 10.0;
        } else if(normalizedValue > 3.33333333333333333){
            normalizedValue = 5.0;
        } else if (normalizedValue > 1.44224957030740838232) {
            normalizedValue = 2.5;
        } else {
            normalizedValue = 1.0;
        }

        double value = normalizedValue * normalizeFactor;

        if(value * (preferredLabelCount - 1) >= max)
            preferredLabelCount--;
        else if(value * (preferredLabelCount + 1) <= max)
            preferredLabelCount++;

        return new BiResult<>(value, preferredLabelCount);
    }

    protected static double[] F50_MAX = { 6.666 };
    protected static double[] F50_MIN = { 6.666 };

    protected static double[] F25_MAX = { 3.333 };
    protected static double[] F25_MIN = { 3.333 };

    protected static double[] F20_MAX = { 1.442 };
    protected static double[] F20_MIN = { 1.442 };

    protected static double[] F10_MAX = { 1.0 };
    protected static double[] F10_MIN = { 1.0 };

    protected static double getFxxMinMax(double[] fxxMinMax, int preferredLabelCount) {
        int index = preferredLabelCount - 2;
        if(index >= fxxMinMax.length)
            return fxxMinMax[fxxMinMax.length-1];
        return fxxMinMax[index];
    }

    protected static double getFxx(double[] fxxMin, double[] fxxMax, int preferredLabelCount, double leaning) {
        return LMath.interpolate(getFxxMinMax(fxxMin, preferredLabelCount), getFxxMinMax(fxxMax, preferredLabelCount), leaning);
    }


    public static BiResult<Double, Integer> calcLabelValueTest(double max, int preferredLabelCount, double f50, double f25, double f20, double f10) {
        double unnormalizedValue = max / preferredLabelCount;
        double logV = Math.log10(unnormalizedValue);
        double normalizeFactor = Math.pow(10, Math.floor(logV));
        double normalizedValue = unnormalizedValue / normalizeFactor;

        //System.out.println(normalizedValue);
        if(normalizedValue >= f50) {
            normalizedValue = 10.0;
        } else if(normalizedValue > f25){
            normalizedValue = 5.0;
        } else if (normalizedValue > f20) {
            normalizedValue = 2.5;
        } else if (normalizedValue > f10) {
            normalizedValue = 2.0;
        } else {
            normalizedValue = 1.0;
        }

        double value = normalizedValue * normalizeFactor;

        if(value * (preferredLabelCount - 1) >= max)
            preferredLabelCount--;
        else if(value * (preferredLabelCount + 1) <= max)
            preferredLabelCount++;

        return new BiResult<>(value, preferredLabelCount);
    }

}
