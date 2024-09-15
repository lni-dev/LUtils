package de.linusdev.lutils.nat.size;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SizeTest {

    @Test
    void test() {
        Size size = new Size(1024);

        assertEquals(1024L, size.get());
        assertEquals(1024L, size.getAsInt());
        assertEquals("1KiB bytes", size.toString());

        size = new Size(1, ByteUnits.KiB);

        assertEquals(1024L, size.get());
        assertEquals(1024L, size.getAsInt());
        assertEquals("1KiB bytes", size.toString());
    }

    @Test
    void test2() {
        Size size = new Size(32432434343L);

        assertEquals(32432434343L, size.get());
        assertThrows(AssertionError.class, size::getAsInt);
        assertEquals("30GiB bytes", size.toString());

        Size size2 = new Size(1, ByteUnits.PiB);

        assertEquals(1125899906842624L, size2.get());
        assertThrows(AssertionError.class, size2::getAsInt);
        assertEquals("1PiB bytes", size2.toString());
    }
}