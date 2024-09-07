package de.linusdev.lutils.nat;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EndianUtilsTest {

    @Test
    void littleEndianBytesToInt() {
        assertEquals(1732584433, EndianUtils.littleEndianBytesToInt((byte) 0xf1, (byte) 0x23, (byte) 0x45, (byte) 0x67));
    }

    @Test
    void bigEndianBytesToInt() {
        assertEquals(-249346713, EndianUtils.bigEndianBytesToInt((byte) 0xf1, (byte) 0x23, (byte) 0x45, (byte) 0x67));
    }
}