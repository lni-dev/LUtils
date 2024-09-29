package de.linusdev.lutils.other;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NumberUtilsTest {

    @Test
    void convertTo() {

        assertEquals(10f, NumberUtils.convertTo(10L, Float.class));
        assertEquals(10f, NumberUtils.convertTo(10L, float.class));

    }
}