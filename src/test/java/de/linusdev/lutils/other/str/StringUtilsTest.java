package de.linusdev.lutils.other.str;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class StringUtilsTest {

    @Test
    void constructableStringOfText() {
        assertNull(StringUtils.computePossibleConstructableStringOfText(null));
        assertNull(StringUtils.computePossibleConstructableStringOfText(""));
        assertNull(StringUtils.computePossibleConstructableStringOfText("test"));

        assertEquals("Hello World!", StringUtils.computePossibleConstructableStringOfText("Hello ${name}!").construct(Map.of("name", "World")::get));
        assertEquals("Hello World!", StringUtils.computePossibleConstructableStringOfText("Hello ${n1Az_-}!").construct(Map.of("n1Az_-", "World")::get));
        assertEquals("Hello World, wow!",
                StringUtils.computePossibleConstructableStringOfText("Hello ${n1Az_-}, ${test}!")
                        .construct(Map.of("n1Az_-", "World", "test", "wow")::get));

        assertEquals("Hello World, wow",
                StringUtils.computePossibleConstructableStringOfText("Hello ${n1Az_-}, ${test}")
                        .construct(Map.of("n1Az_-", "World", "test", "wow")::get));

        assertEquals("World, wow",
                StringUtils.computePossibleConstructableStringOfText("${n1Az_-}, ${test}")
                        .construct(Map.of("n1Az_-", "World", "test", "wow")::get));

        assertNull(StringUtils.getConstructableStringOfText(null));
        assertTrue(StringUtils.getConstructableStringOfText("").isConstant());
        assertTrue(StringUtils.getConstructableStringOfText("test").isConstant());
        assertFalse(StringUtils.getConstructableStringOfText("Hello ${n1Az_-}, ${test}!").isConstant());
    }

    @Test
    void indent() {
        assertEquals("""
                    test
                    1234
                    4567\
                """, StringUtils.indent("test\n1234\n4567", "    ", true));

        assertEquals("""
                test:
                 - 1234
                 - 4567\
                """, StringUtils.indent("test:\n1234\n4567", " - ", false));
    }
}