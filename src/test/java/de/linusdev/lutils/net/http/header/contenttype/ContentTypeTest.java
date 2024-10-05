package de.linusdev.lutils.net.http.header.contenttype;

import de.linusdev.lutils.net.http.header.contenttype.ContentTypes;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ContentTypeTest {

    @Test
    void asString() {

        assertEquals(
                "text/csv; charset=test",
                ContentTypes.Text.csv().setCharset("test").asString()
        );

        assertEquals(
                "text/html",
                ContentTypes.Text.html().asString()
        );

        assertEquals(
                "text/csv; abc=def",
                ContentTypes.Text.csv().set("abc", "def").asString()
        );
    }
}