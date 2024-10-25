package de.linusdev.lutils.html.lhtml;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class LhtmlInjectorTest {

    @Test
    void getConstructableStringOfValue() {

        assertNull(LhtmlInjector.getConstructableStringOfValue(null));
        assertNull(LhtmlInjector.getConstructableStringOfValue(""));
        assertNull(LhtmlInjector.getConstructableStringOfValue("test"));

        assertEquals("Hello World!", LhtmlInjector.getConstructableStringOfValue("Hello ${name}!").construct(Map.of("name", "World")));
        assertEquals("Hello World!", LhtmlInjector.getConstructableStringOfValue("Hello ${n1Az_-}!").construct(Map.of("n1Az_-", "World")));
        assertEquals("Hello World, wow!",
                LhtmlInjector.getConstructableStringOfValue("Hello ${n1Az_-}, ${test}!")
                        .construct(Map.of("n1Az_-", "World", "test", "wow")));

        assertEquals("Hello World, wow",
                LhtmlInjector.getConstructableStringOfValue("Hello ${n1Az_-}, ${test}")
                        .construct(Map.of("n1Az_-", "World", "test", "wow")));

        assertEquals("World, wow",
                LhtmlInjector.getConstructableStringOfValue("${n1Az_-}, ${test}")
                        .construct(Map.of("n1Az_-", "World", "test", "wow")));

    }
}