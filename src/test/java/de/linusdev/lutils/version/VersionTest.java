package de.linusdev.lutils.version;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VersionTest {

    @Test
    void test() {
        Version v1 = Version.of(ReleaseType.RELEASE, 1, 2, 3);

        assertEquals(ReleaseType.RELEASE, v1.type());
        assertEquals(1, v1.version().major());
        assertEquals(2, v1.version().minor());
        assertEquals(3, v1.version().patch());
        assertNull(v1.prefix());
        assertNull(v1.postfix());
        assertEquals("1.2.3", v1.getAsUserFriendlyString());

        Version v2 = Version.of(ReleaseType.BETA, SimpleVersion.of(1, 2, 3), "pre", "post");
        assertEquals(ReleaseType.BETA, v2.type());
        assertEquals(1, v2.version().major());
        assertEquals(2, v2.version().minor());
        assertEquals(3, v2.version().patch());
        assertEquals("pre", v2.prefix());
        assertEquals("post", v2.postfix());
        assertEquals("pre-1.2.3-post:beta", v2.getAsUserFriendlyString());

        Version v3 = Version.of("pre-1.2.3-post:beta");
        assertEquals(ReleaseType.BETA, v3.type());
        assertEquals(1, v3.version().major());
        assertEquals(2, v3.version().minor());
        assertEquals(3, v3.version().patch());
        assertEquals("pre", v3.prefix());
        assertEquals("post", v3.postfix());
        assertEquals("pre-1.2.3-post:beta", v3.getAsUserFriendlyString());

        assertEquals(v2, v3);
    }
}