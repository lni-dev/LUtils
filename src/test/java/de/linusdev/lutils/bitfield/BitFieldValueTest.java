package de.linusdev.lutils.bitfield;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BitFieldValueTest {

    @Test
    void bitPosToValue() {
        assertEquals(8, IntBitFieldValue.bitPosToValue(3));
        assertEquals(8L, LongBitFieldValue.bitPosToValue(3));
    }
}