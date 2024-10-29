package de.linusdev.lutils.math;

import de.linusdev.lutils.llist.LLinkedList;
import de.linusdev.lutils.result.BiResult;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static de.linusdev.lutils.math.ChartMath.*;

class ChartMathTest {

    /**
     *
     * @param res
     * @param max
     * @return {@code true} if failed
     */
    public static boolean doesFail(@NotNull BiResult<Double, Integer> res, double max) {
        double value = res.result1();
        int count = res.result2();
        if(value * (count - 1) >= max) {
            return true;
        }
        return value * (count + 1) <= max;
    }

    public static boolean doesFailF50(double max, int c, double f50) {
        var ret = ChartMath.calcLabelValueTest(max, c,
                f50,
                getFxx(F25_MIN, F25_MAX, c, 0.5),
                getFxx(F20_MIN, F20_MAX, c, 0.5),
                getFxx(F10_MIN, F10_MAX, c, 0.5)
        );

        return doesFail(ret, max);
    }

    public static boolean doesFailF25(double max, int c, double f25) {
        var ret = ChartMath.calcLabelValueTest(max, c,
                getFxx(F50_MIN, F50_MAX, c, 0.5),
                f25,
                getFxx(F20_MIN, F20_MAX, c, 0.5),
                getFxx(F10_MIN, F10_MAX, c, 0.5)
        );

        return doesFail(ret, max);
    }

    public static boolean doesFailF20(double max, int c, double f20) {
        var ret = ChartMath.calcLabelValueTest(max, c,
                getFxx(F50_MIN, F50_MAX, c, 0.5),
                getFxx(F25_MIN, F25_MAX, c, 0.5),
                f20,
                getFxx(F10_MIN, F10_MAX, c, 0.5)
        );

        return doesFail(ret, max);
    }

    public static boolean doesFailF10(double max, int c, double f10) {
        var ret = ChartMath.calcLabelValueTest(max, c,
                getFxx(F50_MIN, F50_MAX, c, 0.5),
                getFxx(F25_MIN, F25_MAX, c, 0.5),
                getFxx(F20_MIN, F20_MAX, c, 0.5),
                f10
        );

        return doesFail(ret, max);
    }

    public interface CallTest {
        boolean test(double max, int layerCount, double factorToTest);
    }

    @Test
    public void test() {

        double test = 1.0;
        double area = 1.0;
        double precision = area / 100.0;
        CallTest fun = ChartMathTest::doesFailF10;

        HashMap<Double, Integer> failCounts = new HashMap<>();

        for (double d = test-area; d < test+area; d = d + precision) {
            int failCount = 0;
            for (int layerCount = 3; layerCount <= 7; layerCount++) {
                for (int max = 10; max < 100000; max++) {
                    if(fun.test(max, layerCount, d))
                        failCount++;
                }
            }


            failCounts.put(d, failCount);
            System.out.println("Done for: " + d);
        }

        double smallest = -10.0;
        int smallestFailCount = 100000000;
        int countWithSame = 0;
        double same = 0.0d;
        for (Map.Entry<Double, Integer> e : failCounts.entrySet()) {
            if(e.getValue() < smallestFailCount) {
                smallestFailCount = e.getValue();
                smallest = e.getKey();
                countWithSame = 0;
                same = smallest;
            } else if(e.getValue() == smallestFailCount) {
                countWithSame++;
                same += e.getKey();
            }

        }

        System.out.println("smallest: " + smallest);
        System.out.println("failCount: " + smallestFailCount);
        System.out.println("countWithSame: " + countWithSame);
        System.out.println("Avg: " + (same/countWithSame));

    }

    @Test
    public void testFactors() throws InterruptedException {
        double test = 1.0;
        double area = 1.0;
        double precision = area / 100.0;
        int minLayerCount = 2;
        int maxLayerCount = 5;
        CallTest fun = ChartMathTest::doesFailF10;

        var executor = Executors.newFixedThreadPool(5);

        LLinkedList<HashMap<Double, Integer>> failCounts = new LLinkedList<>();
        for (int layerCount = minLayerCount; layerCount <= maxLayerCount; layerCount++) {
            int fLayerCount = layerCount;
            executor.submit(() -> {
                HashMap<Double, Integer> map = new HashMap<>();
                for (double d = test-area; d < test+area; d = d + precision) {
                    int failCount = 0;
                    for (int max = 10; max < 1_000; max++) {
                        if(fun.test(max, fLayerCount, d))
                            failCount++;
                    }
                    map.put(d, failCount);
                }

                failCounts.add(map);
            });
        }

        executor.shutdown();
        if(!executor.awaitTermination(1, TimeUnit.HOURS)) {
            System.err.println("TIMEOUT!");
        }

        for (int i = 0; i < failCounts.size(); i++) {
            HashMap<Double, Integer> map = failCounts.get(i);
            System.out.println();
            System.out.println("Summary for preferred label count=" + (i+minLayerCount));

            double value = -10.0;
            int smallestFailCount = 100000000;
            int countWithSame = 0;
            double same = 0.0d;
            double smallestSame = 0.0d;
            double largestSame = 0.0d;
            for (Map.Entry<Double, Integer> e : map.entrySet()) {
                if(e.getValue() < smallestFailCount) {
                    smallestFailCount = e.getValue();
                    value = e.getKey();
                    countWithSame = 0;
                    same = value;
                    smallestSame = value;
                    largestSame = value;
                } else if(e.getValue() == smallestFailCount) {
                    countWithSame++;
                    same += e.getKey();
                    if(e.getKey() < smallestSame)
                        smallestSame = e.getKey();
                    if(e.getKey() > largestSame)
                        largestSame = e.getKey();
                }

            }

            System.out.println("One of the best was: " + value);
            System.out.println("Fail count of that: " + smallestFailCount);
            System.out.println("Amount of values with the same fail count: " + countWithSame);
            System.out.println("Their values ranged from " + smallestSame + " to " + largestSame);
            System.out.println("The center being: " + ((smallestSame + largestSame) / 2));
            System.out.println("And the average: " + (same/countWithSame));
        }
    }
}