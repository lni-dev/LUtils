package de.linusdev.lutils.math;

import de.linusdev.lutils.llist.LLinkedList;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static de.linusdev.lutils.math.ChartMath.calcLabelValue;
import static de.linusdev.lutils.math.ChartMath.calcLabelValueTest;

class ChartMathTest {

    public static boolean doesFail(double max, int plc, double leaning) {
        var res = calcLabelValue(max, plc, leaning);
        //System.out.println(max + ", " + plc + " -> " + (res.result1()* res.result2()) + ", " + res.result2() + ", " + res.result1());
        double value = res.result1();
        int count = res.result2();
        if(value * (count - 1) >= max) {
            //System.err.println("FAILED! Could be less.");
            return true;
        }
        if (value * (count + 1) <= max) {
            //System.err.println("FAILED! Could be more.");
            return true;
        }

        return false;
    }

    public static boolean doesFailTest(double max, int plc, double f1, double f2, double f3) {
        var res = calcLabelValueTest(max, plc, f1, f2, f3);
        double value = res.result1();
        int count = res.result2();
        if(value * (count - 1) >= max) {
            return true;
        }
        if (value * (count + 1) <= max) {
            return true;
        }

        return false;
    }

    @Test
    public void test() {
        HashMap<Double, Integer> failCounts = new HashMap<>();
        double test = 6.686715730908084;
        for (double d = test-.1; d < test+.1; d = d + 0.000001) {
            int failCount = 0;
            for (int j = 3; j <= 7; j++) {
                for (int i = 10; i < 100000; i++) {
                    if(doesFail(i, j, d))
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
        double f1_base = 6.686715730908084;
        double f2_base = 3.33333333333333333;
        double f3_base = 1.44224957030740838232;

        var executor = Executors.newFixedThreadPool(5);

        LLinkedList<HashMap<Double, Integer>> failCounts = new LLinkedList<>();
        for (int j = 3; j <= 12; j++) {
            int finalJ = j;
            executor.submit(() -> {
                HashMap<Double, Integer> map = new HashMap<>();
                for (double d = f3_base-.2; d < f3_base+.2; d = d + 0.0001) {
                    int failCount = 0;
                    for (int i = 10; i < 1_000; i++) {
                        if(doesFailTest(i, finalJ, f1_base, f2_base, d))
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
            System.out.println("Summary for preferred label count=" + (i+3));

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