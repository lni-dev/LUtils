package de.linusdev.lutils.version;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SimpleVersionTest {

    @Test
    void test() {
        SimpleVersion v1 = SimpleVersion.of(1, 2, 3);
        assertEquals(1, v1.major());
        assertEquals(2, v1.minor());
        assertEquals(3, v1.patch());

        SimpleVersion v2 = SimpleVersion.of("1.2.3");
        assertEquals(1, v2.major());
        assertEquals(2, v2.minor());
        assertEquals(3, v2.patch());

        assertEquals("1.2.3", v2.getAsUserFriendlyString());
        assertEquals("1.2.3", v1.getAsUserFriendlyString());
    }

}