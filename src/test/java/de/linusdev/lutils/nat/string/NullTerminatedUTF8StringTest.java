package de.linusdev.lutils.nat.string;

import de.linusdev.lutils.nat.struct.annos.SVWrapper;
import org.junit.jupiter.api.Test;

import java.nio.BufferOverflowException;

import static org.junit.jupiter.api.Assertions.*;

class NullTerminatedUTF8StringTest {

    @Test
    void test() {
        NullTerminatedUTF8String string = NullTerminatedUTF8String.newAllocated(SVWrapper.length(50));

        assertEquals(50, string.length());

        String test1 = "Test1234-+ÖÜ";
        string.set(test1);
        assertEquals(test1, string.get());

        String test2 = "a".repeat(51);
        assertThrows(BufferOverflowException.class, () -> string.set(test2));

        String test3 = "Test3";
        string.set(test3);
        assertEquals(test3, string.get());

        String test4 = "Test4";
        string.set(test4);
        assertEquals(test4, string.get());
    }

    @Test
    void ofString() {
        NullTerminatedUTF8String string = NullTerminatedUTF8String.newAllocated("Test");

        assertEquals("Test", string.get());

    }

    @Test
    void ofStringAllocatable() {
        NullTerminatedUTF8String string = NullTerminatedUTF8String.newAllocatable("Test");

        assertFalse(string.isInitialised());
        string.allocate();
        assertTrue(string.isInitialised());
        assertEquals("Test", string.get());

    }
}