package de.linusdev.lutils.other.str;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConstructableStringTest {

    @Test
    void construct() {
        ConstructableString str = new ConstructableString(
                new Part[]{
                        Part.constant("Hello "),
                        Part.placeholder("name"),
                        Part.constant(", how are you?")
                }
        );


        assertEquals("Hello World, how are you?", str.construct(Map.of("name", "World")));
    }
}