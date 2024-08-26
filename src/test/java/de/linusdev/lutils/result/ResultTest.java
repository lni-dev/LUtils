package de.linusdev.lutils.result;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResultTest {

    @Test
    void singleResultTest() {
        SingleResult<String> result = new SingleResult<>("test");

        assertEquals(1, result.count());
        assertEquals("test", result.result());
        assertEquals("test", result.get(0));
        assertThrows(IndexOutOfBoundsException.class, () -> result.get(1));
    }

    @Test
    void biResultTest() {
        BiResult<String, Integer> result = new BiResult<>("test", 10);

        assertEquals(2, result.count());
        assertEquals("test", result.result1());
        assertEquals(10, result.result2());
        assertEquals("test", result.get(0));
        assertEquals(10, result.get(1));
        assertThrows(IndexOutOfBoundsException.class, () -> result.get(2));
    }

    @Test
    void triResultTest() {
        TriResult<String, Float, Integer> result = new TriResult<>("test", 3.2f, 13);

        assertEquals(3, result.count());
        assertEquals("test", result.result1());
        assertEquals(3.2f, result.result2());
        assertEquals(13, result.result3());
        assertEquals("test", result.get(0));
        assertEquals(3.2f, result.get(1));
        assertEquals(13, result.get(2));
        assertThrows(IndexOutOfBoundsException.class, () -> result.get(3));
    }
}