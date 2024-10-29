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
     *                              0.0 means: Lean towards higher value labels / fewer labels<br>
     *                              side effect: the highest label is more likely to be greater than max. It is more likely that the chart has some unused space at the top
     *                          </li>
     *                          <li>
     *                              1.0 means: Lean towards lower value labels / more labels<br>
     *                              side effect: the highest label is more likely to be less than max.
     *                          </li>
     *                      </ul>
     * @return label-value and label count
     */
    public static BiResult<Double, Integer> calcLabelValue(double max, int preferredLabelCount, double leaningFactor) {
        return calcLabelValueTest(max, preferredLabelCount,
                getFxx(F50_MIN, F50_MAX, preferredLabelCount, leaningFactor),
                getFxx(F25_MIN, F25_MAX, preferredLabelCount, leaningFactor),
                getFxx(F20_MIN, F20_MAX, preferredLabelCount, leaningFactor),
                getFxx(F10_MIN, F10_MAX, preferredLabelCount, leaningFactor)
        );
    }

    //                                    2 NE/T             3 NE/T             4 NE/T             5 NE               6                   7                  8               9               10              11              12
    protected static double[] F50_MAX = { 9.999999999999999, 6.666666666666666, 7.499999999999999, 6.999999999999999, 6.666649999987496, 7.157099999996525, };
    protected static double[] F50_MIN = { 5.000000000000001, 5.000000000000001, 5.000000000000001, 6.000000000000001, 6.6650499999875, 6.414299999998256, };

    //                                    2                  3                  4                  5                  6                  7                   8               9               10              11              12
    protected static double[] F25_MAX = { 4.999999999999999, 4.166649999997801, 3.749999999999999, 3.499999999999999, 3.334950000002313, 3.5857000000026438, };
    protected static double[] F25_MIN = { 2.500000000000001, 2.500000000000001, 2.500000000000001, 3.000000000000001, 3.3317000000023205, 3.2000000000018300, };
    //                                    2                  3                  4                  5                  6                   7                   8               9               10              11              12
    protected static double[] F20_MAX = { 2.499999999999999, 2.499999999999999, 2.499999999999999, 2.799999999999999, 2.4341490000282398, 2.4419000000007607, };
    protected static double[] F20_MIN = { 2.000000000000001, 2.000000000000001, 2.000000000000001, 2.000000000000001, 2.2341500000002843, 1.7857999999998520, };

    //                                    2                  3                  4                  5                  6             7               8               9               10              11              12
    protected static double[] F10_MAX = { 1.999999999999999, 1.666666666666666, 1.499999999999999, 1.399999999999999, 1.3334833332987255, 1.999999999999, 1.999999999999, 1.999999999999, 1.999999999999, 1.999999999999, 1.999999999999 };
    protected static double[] F10_MIN = { 1.000000000000001, 1.000000000000001, 1.000000000000001, 1.200000000000001, 1.3333833332987253, 1.211999999999, 1.284999999999, 1.248999999999, 1.190999999999, 1.172999999999, 1.158999999999 };

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
