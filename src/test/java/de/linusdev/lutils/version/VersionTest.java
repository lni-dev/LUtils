package de.linusdev.lutils.version;

import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

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
        assertEquals("pre-1.2.3-post_beta", v2.getAsArchiveReadyString());

        Version v3 = Version.of("pre-1.2.3-post:beta");
        assertEquals(ReleaseType.BETA, v3.type());
        assertEquals(1, v3.version().major());
        assertEquals(2, v3.version().minor());
        assertEquals(3, v3.version().patch());
        assertEquals("pre", v3.prefix());
        assertEquals("post", v3.postfix());
        assertEquals("pre-1.2.3-post:beta", v3.getAsUserFriendlyString());

        assertEquals(v2, v3);

        Version v4 = Version.of(ReleaseType.DEVELOPMENT_BUILD, "1.2.3");

        assertEquals(ReleaseType.DEVELOPMENT_BUILD, v4.type());
        assertEquals(1, v4.version().major());
        assertEquals(2, v4.version().minor());
        assertEquals(3, v4.version().patch());
        assertNull(v4.prefix());
        assertNull(v4.postfix());
        assertEquals("1.2.3:dev", v4.getAsUserFriendlyString());

        Version a = Version.of(ReleaseType.RELEASE, 3, 0, 0);
        Version b = Version.of(ReleaseType.RELEASE, 1, 2, 3);
        Version c = Version.of(ReleaseType.RELEASE, 2, 2, 3);
        Version d = Version.of(ReleaseType.RELEASE, 1, 2, 4);
        Version e = Version.of(ReleaseType.DEVELOPMENT_BUILD, 1, 2, 4);

        Version[] versions = Stream.of(a, b, c, d, e).sorted().toArray(Version[]::new);
        assertArrayEquals(new Version[] {b, e, d, c, a}, versions);


    }
}