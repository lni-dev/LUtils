package de.linusdev.lutils.math;

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
     * @param res
     * @param max
     * @param print
     * @return {@code true} if failed
     */
    public static boolean doesFail(@NotNull BiResult<Double, Integer> res, double max, boolean print) {
        double value = res.result1();
        int count = res.result2();
        if(value * (count - 1) >= max || value * (count + 1) <= max) {
            if(print) System.out.println("max=" + max + " failed");
            return true;
        }

        return false;
    }

    public static boolean doesFailF50(double max, int c, double f50, double leaning) {
        var ret = ChartMath.calcLabelValueTest(max, c,
                f50,
                getFxx(F25_MIN, F25_MAX, c, leaning),
                getFxx(F20_MIN, F20_MAX, c, leaning),
                getFxx(F10_MIN, F10_MAX, c, leaning)
        );

        return doesFail(ret, max, false);
    }

    public static boolean doesFailF25(double max, int c, double f25, double leaning) {
        var ret = ChartMath.calcLabelValueTest(max, c,
                getFxx(F50_MIN, F50_MAX, c, leaning),
                f25,
                getFxx(F20_MIN, F20_MAX, c, leaning),
                getFxx(F10_MIN, F10_MAX, c, leaning)
        );

        return doesFail(ret, max, false);
    }

    public static boolean doesFailF20(double max, int c, double f20, double leaning) {
        var ret = ChartMath.calcLabelValueTest(max, c,
                getFxx(F50_MIN, F50_MAX, c, leaning),
                getFxx(F25_MIN, F25_MAX, c, leaning),
                f20,
                getFxx(F10_MIN, F10_MAX, c, leaning)
        );

        return doesFail(ret, max, false);
    }

    public static boolean doesFailF10(double max, int c, double f10, double leaning) {
        var ret = ChartMath.calcLabelValueTest(max, c,
                getFxx(F50_MIN, F50_MAX, c, leaning),
                getFxx(F25_MIN, F25_MAX, c, leaning),
                getFxx(F20_MIN, F20_MAX, c, leaning),
                f10
        );

        return doesFail(ret, max, false);
    }

    public interface CallTest {
        boolean test(double max, int layerCount, double factorToTest, double leaning);
    }

    @Test
    public void printTest() {
        double max = 8.0;
        int plc = 6;

        double normalizedValue = max / plc / Math.pow(10, Math.floor(Math.log10(max / plc)));
        System.out.println("normalizedValue=" + normalizedValue);
        print(max, plc, 0.0);
        print(max, plc, 0.5);
        print(max, plc, 1.0);
    }

    public void print(double max, int plc, double leaning) {
        var ret = ChartMath.calcLabelValue(max, plc, leaning);
        System.out.println("max=" + max + ", plc=" + plc + ", leaning=" + leaning + " -> value=" + ret.result1() +  ", count=" + ret.result2() + ", maxValue=" + (ret.result1()*ret.result2()));
    }

    @Test
    public void test() throws InterruptedException {
        int minLayerCount = 6;
        int maxLayerCount = 6;

        int maxMax = 100;

        var executor = Executors.newFixedThreadPool(5);

        int[] failCounts0 = new int[maxLayerCount-minLayerCount + 1];
        for (int layerCount = minLayerCount; layerCount <= maxLayerCount; layerCount++) {
            int fLayerCount = layerCount;
            executor.submit(() -> {

                int failCount = 0;
                for (double max = fLayerCount; max < maxMax; max+=0.001) {
                    if(doesFail(ChartMath.calcLabelValue(max, fLayerCount, 0.0), max, true))
                        failCount++;
                }
                failCounts0[fLayerCount - minLayerCount] = failCount;
            });
        }

        int[] failCounts05 = new int[maxLayerCount-minLayerCount + 1];
        for (int layerCount = minLayerCount; layerCount <= maxLayerCount; layerCount++) {
            int fLayerCount = layerCount;
            executor.submit(() -> {

                int failCount = 0;
                for (int max = fLayerCount; max < maxMax; max++) {
                    if(doesFail(ChartMath.calcLabelValue(max, fLayerCount, 0.5), max, true))
                        failCount++;
                }
                failCounts05[fLayerCount - minLayerCount] = failCount;
            });
        }

        int[] failCounts1 = new int[maxLayerCount-minLayerCount + 1];
        for (int layerCount = minLayerCount; layerCount <= maxLayerCount; layerCount++) {
            int fLayerCount = layerCount;
            executor.submit(() -> {

                int failCount = 0;
                for (int max = fLayerCount; max < maxMax; max++) {
                    if(doesFail(ChartMath.calcLabelValue(max, fLayerCount, 1), max, true))
                        failCount++;
                }
                failCounts1[fLayerCount - minLayerCount] = failCount;
            });
        }

        executor.shutdown();
        if(!executor.awaitTermination(1, TimeUnit.HOURS)) {
            System.err.println("TIMEOUT!");
        }

        for (int i = 0; i < failCounts0.length; i++) {
            int failCount = failCounts0[i];
            System.out.println("[layerCount=" + (i+minLayerCount) + ", leaning=0.0]: " + failCount);
        }

        for (int i = 0; i < failCounts05.length; i++) {
            int failCount = failCounts05[i];
            System.out.println("[layerCount=" + (i+minLayerCount) + ", leaning=0.5]: " + failCount);
        }

        for (int i = 0; i < failCounts1.length; i++) {
            int failCount = failCounts1[i];
            System.out.println("[layerCount=" + (i+minLayerCount) + ", leaning=1.0]: " + failCount);
        }

    }

    @Test
    public void testFactors() throws InterruptedException {
        int testLayer = 6;
        System.out.println();System.out.println();
        System.out.println("Test F10:");
        testFactors(getFxx(F10_MIN, F10_MAX, testLayer, 0.5), ChartMathTest::doesFailF10, testLayer);

        System.out.println();System.out.println();
        System.out.println("Test F20:");
        testFactors(getFxx(F20_MIN, F20_MAX, testLayer, 0.5), ChartMathTest::doesFailF20, testLayer);

        System.out.println();System.out.println();
        System.out.println("Test F25");
        testFactors(getFxx(F25_MIN, F25_MAX, testLayer, 0.5), ChartMathTest::doesFailF25, testLayer);

        System.out.println();System.out.println();
        System.out.println("Test F50:");
        testFactors(getFxx(F50_MIN, F50_MAX, testLayer, 0.5), ChartMathTest::doesFailF50, testLayer);
    }

    @Test
    public void testSingle() throws InterruptedException {
        System.out.println("Test:");
        testFactors(3.75, ChartMathTest::doesFailF25, 5);
    }


    public void testFactors(double test, CallTest fun, int testLayer) throws InterruptedException {
        double area = 0.1;
        double precision = area / 100000;
        int minLayerCount = testLayer;
        int maxLayerCount = testLayer;
        double leaning = 0.5;

        int maxMax = 1000;
        var executor = Executors.newFixedThreadPool(5);

        HashMap<Double, Integer>[] failCounts = new HashMap[maxLayerCount - minLayerCount + 1];
        for (int layerCount = minLayerCount; layerCount <= maxLayerCount; layerCount++) {
            int fLayerCount = layerCount;
            executor.submit(() -> {
                HashMap<Double, Integer> map = new HashMap<>();
                for (double d = test-area; d < test+area; d = d + precision) {
                    int failCount = 0;
                    for (double max = fLayerCount; max < maxMax; max += 0.1) {
                        if(fun.test(max, fLayerCount, d, leaning))
                            failCount++;
                    }
                    map.put(d, failCount);
                }

                failCounts[fLayerCount - minLayerCount] = map;
            });
        }

        executor.shutdown();
        if(!executor.awaitTermination(1, TimeUnit.HOURS)) {
            System.err.println("TIMEOUT!");
        }

        for (int i = 0; i < failCounts.length; i++) {
            HashMap<Double, Integer> map = failCounts[i];
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