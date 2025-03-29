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

package de.linusdev.lutils.math;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LMathTest {

    @Test
    void clampInt() {
        assertEquals(10, LMath.clamp(10, 3, 13));
        assertEquals(3, LMath.clamp(1, 3, 13));
        assertEquals(13, LMath.clamp(123, 3, 13));
    }

    @Test
    void clampLong() {
        assertEquals(10L, LMath.clamp(10L, 3L, 13L));
        assertEquals(3L, LMath.clamp(1L, 3L, 13L));
        assertEquals(13L, LMath.clamp(123L, 3L, 13L));
    }

    @Test
    void clampFloat() {
        assertEquals(10f, LMath.clamp(10f, 3f, 13f));
        assertEquals(3f, LMath.clamp(1f, 3f, 13f));
        assertEquals(13f, LMath.clamp(123f, 3f, 13f));
    }

    @Test
    void clampDouble() {
        assertEquals(10.0, LMath.clamp(10.0, 3.0, 13.0));
        assertEquals(3.0, LMath.clamp(1.0, 3.0, 13.0));
        assertEquals(13.0, LMath.clamp(123.0, 3.0, 13.0));
    }

    @Test
    void minUnsigned() {
        assertEquals(0x1000_0000, LMath.minUnsigned(0x1000_0000, 0x1000_0001));
        assertEquals(0x1000_0001, LMath.minUnsigned(0x1000_0001, 0x1000_0002));
        assertEquals(0x0000_0001, LMath.minUnsigned(0x1000_0000, 0x0000_0001));
        assertEquals(0x0000_0000, LMath.minUnsigned(0x1000_0000, 0x0000_0000));

        assertEquals(0x1000_0000, LMath.minUnsigned(0x1000_0001, 0x1000_0000));
        assertEquals(0x1000_0001, LMath.minUnsigned(0x1000_0002, 0x1000_0001));
        assertEquals(0x0000_0001, LMath.minUnsigned(0x0000_0001, 0x1000_0000));
        assertEquals(0x0000_0000, LMath.minUnsigned(0x0000_0000, 0x1000_0000));
    }

    @Test
    void maxUnsigned() {
        assertEquals(0x1000_0001, LMath.maxUnsigned(0x1000_0000, 0x1000_0001));
        assertEquals(0x1000_0002, LMath.maxUnsigned(0x1000_0001, 0x1000_0002));
        assertEquals(0x1000_0000, LMath.maxUnsigned(0x1000_0000, 0x0000_0001));
        assertEquals(0x1000_0000, LMath.maxUnsigned(0x1000_0000, 0x0000_0000));

        assertEquals(0x1000_0001, LMath.maxUnsigned(0x1000_0001, 0x1000_0000));
        assertEquals(0x1000_0002, LMath.maxUnsigned(0x1000_0002, 0x1000_0001));
        assertEquals(0x1000_0000, LMath.maxUnsigned(0x0000_0001, 0x1000_0000));
        assertEquals(0x1000_0000, LMath.maxUnsigned(0x0000_0000, 0x1000_0000));
    }

    @Test
    void clampUnsigned() {
        assertEquals(10, LMath.clampUnsigned(10, 3, 13));
        assertEquals(3, LMath.clampUnsigned(1, 3, 13));
        assertEquals(13, LMath.clampUnsigned(123, 3, 13));
    }

    @Test
    void intLog2() {
        assertEquals(0, LMath.intLog2(0));
        assertEquals(0, LMath.intLog2(1));

        assertEquals(1, LMath.intLog2(2));
        assertEquals(1, LMath.intLog2(3));

        assertEquals(2, LMath.intLog2(4));
        assertEquals(2, LMath.intLog2(5));
        assertEquals(2, LMath.intLog2(6));
        assertEquals(2, LMath.intLog2(7));

        assertEquals(3, LMath.intLog2(8));
        assertEquals(3, LMath.intLog2(9));
        assertEquals(3, LMath.intLog2(10));
        assertEquals(3, LMath.intLog2(15));

        assertEquals(4, LMath.intLog2(16));
        assertEquals(4, LMath.intLog2(31));

        assertEquals(5, LMath.intLog2(32));
        assertEquals(5, LMath.intLog2(63));

        assertEquals(19, LMath.intLog2(1048575));
        assertEquals(20, LMath.intLog2(1048576));

        assertEquals(30, LMath.intLog2(Integer.MAX_VALUE));

        assertEquals(31, LMath.intLog2(0x80000000));
        assertEquals(31, LMath.intLog2(0xFFFFFFFF));
    }

    @Test
    void interpolate1() {
        assertEquals(10.0, LMath.interpolate(5.0, 15.0, 0.5));
        assertEquals(5.0, LMath.interpolate(5.0, 15.0, 0.0));
        assertEquals(15.0, LMath.interpolate(5.0, 15.0, 1.0));
    }

    @Test
    void interpolate2() {
        assertEquals(10.0, LMath.interpolate(1.0, 4.0,5.0, 15.0, 2.5));
        assertEquals( 5.0, LMath.interpolate(1.0, 4.0,5.0, 15.0, 1.0));
        assertEquals(15.0, LMath.interpolate(1.0, 4.0,5.0, 15.0, 4.0));
    }
}