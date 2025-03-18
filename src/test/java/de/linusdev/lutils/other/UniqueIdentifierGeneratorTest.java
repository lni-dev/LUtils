package de.linusdev.lutils.other;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UniqueIdentifierGeneratorTest {

    @Test
    void test() {

        var gen = new UniqueIdentifierGenerator("prefix-", 10);

        assertEquals(10, gen.getNextAsInt());
        assertEquals(11L, gen.getNext());
        assertEquals("prefix-12", gen.getNextAsString());

        gen = new UniqueIdentifierGenerator(15);
        assertEquals("15", gen.getNextAsString());

    }
}