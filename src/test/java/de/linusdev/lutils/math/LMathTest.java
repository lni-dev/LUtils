package de.linusdev.lutils.math;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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
}