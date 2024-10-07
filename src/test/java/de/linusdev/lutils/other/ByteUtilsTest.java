package de.linusdev.lutils.other;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ByteUtilsTest {

    @Test
    void constructInt() {
        assertEquals(255, ByteUtils.constructInt((byte) 0xFF));
        assertEquals(0x44FF, ByteUtils.constructInt((byte) 0x44, (byte) 0xFF));
        assertEquals(0x2244FF, ByteUtils.constructInt((byte) 0x22, (byte) 0x44, (byte) 0xFF));
        assertEquals(0x112244FF, ByteUtils.constructInt((byte) 0x11, (byte) 0x22, (byte) 0x44, (byte) 0xFF));
    }
}