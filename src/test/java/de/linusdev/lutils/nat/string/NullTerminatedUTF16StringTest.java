package de.linusdev.lutils.nat.string;

import de.linusdev.lutils.nat.struct.annos.SVWrapper;
import org.junit.jupiter.api.Test;

import java.nio.BufferOverflowException;

import static org.junit.jupiter.api.Assertions.*;

class NullTerminatedUTF16StringTest {
    @Test
    void test() {
        NullTerminatedUTF16String string = NullTerminatedUTF16String.newAllocated(SVWrapper.length(50));

        assertEquals(50, string.length());

        String test1 = "Test1234-+ÖÜ";
        string.set(test1);
        assertEquals(test1, string.get());

        String test2 = "a".repeat(50);
        assertThrows(BufferOverflowException.class, () -> string.set(test2));

        String test3 = "Test3";
        string.set(test3);
        assertEquals(test3, string.get());

        String test4 = "Test4";
        string.set(test4);
        assertEquals(test4, string.get());
    }
}