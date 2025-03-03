package de.linusdev.lutils.net.http.path;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PathAndQueryTest {
    @Test
    public void test() {
        PathAndQuery p = new PathAndQuery("/test?abc=def&hello=lol");
        assertEquals("/test", p.getPath());
        assertEquals("def", p.getParameters().get("abc"));
        assertEquals("lol", p.getParameters().get("hello"));
    }
}