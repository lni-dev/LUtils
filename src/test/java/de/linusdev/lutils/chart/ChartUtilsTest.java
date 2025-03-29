/*
 * Copyright (c) 2025 Linus Andera
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

package de.linusdev.lutils.chart;

import org.junit.jupiter.api.Test;

import static de.linusdev.lutils.chart.ChartUtils.LabelLeaning;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class ChartUtilsTest {

    public static boolean DEBUG = false;

    @Test
    public void testCalcBestAndReadableLabelValue() {
        int maxPlc = 10;

        System.out.println();System.out.println();
        System.out.println("Leaning: 0.0");
        for (int plc = 1; plc <= maxPlc; plc++) {
            for (double max = 10; max < 100; max += 0.1) {
                var res = ChartUtils.calcBestAndReadableLabelValue(max, 1, plc, 20, 0.0);

                double value = res.result1();
                int count = res.result2();

                if(value * (count - 1) >= max || value * (count + 1) <= max) {
                    if(!DEBUG) fail();
                    System.out.println("max=" + max + ", plc=" + plc + " failed");
                }
            }

            System.out.println("plc=" + plc + " done!");
        }

        System.out.println();System.out.println();
        System.out.println("Leaning: 1.0");
        for (int plc = 1; plc <= maxPlc; plc++) {
            for (double max = 10; max < 100; max += 0.1) {
                var res = ChartUtils.calcBestAndReadableLabelValue(max, 1, plc, 20, 1.0);

                double value = res.result1();
                int count = res.result2();

                if(value * (count - 1) >= max || value * (count + 1) <= max) {
                    if(!DEBUG) fail();
                    System.out.println("max=" + max + ", plc=" + plc + " failed");
                }
            }

            System.out.println("plc=" + plc + " done!");
        }

        System.out.println();System.out.println();
        System.out.println("Leaning: -1.0");
        for (int plc = 1; plc <= maxPlc; plc++) {
            for (double max = 10; max < 100; max += 0.1) {
                var res = ChartUtils.calcBestAndReadableLabelValue(max, 1, plc, 20,-1.0);

                double value = res.result1();
                int count = res.result2();

                if(value * (count - 1) >= max || value * (count + 1) <= max) {
                    if(!DEBUG) fail();
                    System.out.println("max=" + max + ", plc=" + plc + " failed");
                }
            }

            System.out.println("plc=" + plc + " done!");
        }

    }

    @Test
    public void testCalcBestLabelValue() {
        int maxPlc = 10;

        System.out.println();System.out.println();
        System.out.println("Leaning: BEST");
        for (int plc = 1; plc <= maxPlc; plc++) {
            for (double max = 10; max < 100; max += 0.1) {
                var res = ChartUtils.calcBestLabelValue(max, 1, plc, 20, LabelLeaning.BEST);

                double value = res.result1();
                int count = res.result2();

                if(value * (count - 1) >= max || value * (count + 1) <= max) {
                    if(!DEBUG) fail();
                    System.out.println("max=" + max + ", plc=" + plc + " failed");
                }
            }

            System.out.println("plc=" + plc + " done!");
        }

        System.out.println();System.out.println();
        System.out.println("Leaning: HIGHER_THAN_MAX");
        for (int plc = 1; plc <= maxPlc; plc++) {
            for (double max = 10; max < 100; max += 0.1) {
                var res = ChartUtils.calcBestLabelValue(max, 1, plc, 20, LabelLeaning.HIGHER_THAN_MAX);

                double value = res.result1();
                int count = res.result2();

                if(value * (count - 1) >= max || value * (count + 1) <= max) {
                    if(!DEBUG) fail();
                    System.out.println("max=" + max + ", plc=" + plc + " failed");
                }
            }

            System.out.println("plc=" + plc + " done!");
        }

        System.out.println();System.out.println();
        System.out.println("Leaning: LOWER_THAN_MAX");
        for (int plc = 1; plc <= maxPlc; plc++) {
            for (double max = 10; max < 100; max += 0.1) {
                var res = ChartUtils.calcBestLabelValue(max, 1, plc, 20, LabelLeaning.LOWER_THAN_MAX);

                double value = res.result1();
                int count = res.result2();

                if(value * (count - 1) >= max || value * (count + 1) <= max) {
                    if(!DEBUG) fail();
                    System.out.println("max=" + max + ", plc=" + plc + " failed");
                }
            }

            System.out.println("plc=" + plc + " done!");
        }

    }

    @Test
    public void printTest() {
        double max = 15.999999999999979;
        int plc = 11;

        print(max, plc, +1.0);
        print(max, plc,  0.0);
        //print(max, 9,  0.0);
        print(max, plc, -1.0);
    }

    @Test
    public void printTest2() {
        double max =15.199999999999982;
        int plc = 11;

        print(max, plc, LabelLeaning.HIGHER_THAN_MAX);
        print(max, plc,  LabelLeaning.BEST);
        print(max, plc, LabelLeaning.LOWER_THAN_MAX);
    }

    public void print(double max, int plc, double leaning) {
        var ret = ChartUtils.calcBestAndReadableLabelValue(max, 1, plc, 20, leaning);
        System.out.println("max=" + max + ", plc=" + plc + ", leaning=" + leaning + " -> value=" + ret.result1() +  ", count=" + ret.result2() + ", maxValue=" + (ret.result1()*ret.result2()));
    }

    public void print(double max, int plc, LabelLeaning leaning) {
        var ret = ChartUtils.calcBestLabelValue(max, 1, plc, 20, leaning);
        System.out.println("max=" + max + ", plc=" + plc + ", leaning=" + leaning + " -> value=" + ret.result1() +  ", count=" + ret.result2() + ", maxValue=" + (ret.result1()*ret.result2()));
    }



    @Test
    public void choseTestRange() {
        assertEquals(1, ChartUtils.choseTestRange(1));
        assertEquals(1, ChartUtils.choseTestRange(2));

        assertEquals(2, ChartUtils.choseTestRange(3));
        assertEquals(2, ChartUtils.choseTestRange(4));

        assertEquals(3, ChartUtils.choseTestRange(5));
        assertEquals(3, ChartUtils.choseTestRange(6));
        assertEquals(3, ChartUtils.choseTestRange(7));

        assertEquals(4, ChartUtils.choseTestRange(8));
        assertEquals(4, ChartUtils.choseTestRange(14));

        assertEquals(5, ChartUtils.choseTestRange(16));
        assertEquals(5, ChartUtils.choseTestRange(18));

        assertEquals(6, ChartUtils.choseTestRange(20));

        assertEquals(8, ChartUtils.choseTestRange(28));

        assertEquals(10, ChartUtils.choseTestRange(34));
        assertEquals(10, ChartUtils.choseTestRange(340));
    }

    @Test
    void calcBestLabelValue() {
        var res = ChartUtils.calcBestLabelValue(10_023, 25_399, 1, 4, 10, LabelLeaning.BEST);
        assertEquals(10000, res.result3());
        assertEquals(3, res.result2());
        assertEquals(5000, res.result1());
    }

    @Test
    void calcBestAndReadableLabelValue() {
        var res = ChartUtils.calcBestAndReadableLabelValue(10_023, 25_399, 1, 4, 10, 0.0);
        assertEquals(10000, res.result3());
        assertEquals(3, res.result2());
        assertEquals(5000, res.result1());
    }
}