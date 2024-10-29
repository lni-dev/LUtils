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

    public static BiResult<Double, Integer> calcLabelValueTest(double max, int preferredLabelCount, double f1, double f2, double f3) {
        double unnormalizedValue = max / preferredLabelCount;
        double logV = Math.log10(unnormalizedValue);
        double normalizeFactor = Math.pow(10, Math.floor(logV));
        double normalizedValue = unnormalizedValue / normalizeFactor;

        //System.out.println(normalizedValue);
        if(normalizedValue >= f1) {
            normalizedValue = 10.0;
        } else if(normalizedValue > f2){
            normalizedValue = 5.0;
        } else if (normalizedValue > f3) {
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

}
