package de.linusdev.lutils.net.http.header.value.parameters;

import de.linusdev.lutils.net.http.header.contenttype.ContentTypes;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BasicHeaderValueWithCharsetTest {

    @Test
    void getCharset() {
        assertEquals("utf-8", ContentTypes.Text.csv().setCharset("utf-8").getCharset());
    }
}