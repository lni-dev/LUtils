package de.linusdev.lutils.http.header.value;

import de.linusdev.lutils.http.HTTPMessageBuilder;
import de.linusdev.lutils.http.HTTPRequest;
import de.linusdev.lutils.http.body.Body;
import de.linusdev.lutils.http.body.BodyParsers;
import de.linusdev.lutils.http.header.HeaderNames;
import de.linusdev.lutils.http.header.contenttype.ContentType;
import de.linusdev.lutils.http.header.contenttype.ContentTypes;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class BasicHeaderValueTest {

    @Test
    void test() throws IOException {
        HTTPMessageBuilder builder = HTTPRequest
                .builder()
                .POST("/abc", new Body() {
                    @Override
                    public @NotNull ContentType contentType() {
                        return ContentTypes.Text.plain()
                                .setCharset("utf-8")
                                .set("test", "123")
                                .add("text/html")
                                .add("text/html");
                    }

                    @Override
                    public int length() {
                        return -1;
                    }

                    @Override
                    public @NotNull InputStream stream() {
                        return new ByteArrayInputStream("test".getBytes(StandardCharsets.UTF_8));
                    }
                });

        HTTPRequest<String> request = HTTPRequest.parse(
                new ByteArrayInputStream(builder.buildRequest().getBytes(StandardCharsets.UTF_8)),
                BodyParsers.newStringBodyParser()
        );

        var contentTypeHeader = request.getHeaders().get(HeaderNames.CONTENT_TYPE).parseValue(BasicHeaderValue.PARSER);

        assertEquals("123", contentTypeHeader.get("test"));
        assertEquals("utf-8", contentTypeHeader.get("charset"));
        assertEquals(2, contentTypeHeader.getValues().size());
        assertEquals("text/plain", contentTypeHeader.getValues().get(0));
        assertEquals("text/html", contentTypeHeader.getValues().get(1));
        assertTrue(contentTypeHeader.contains("text/plain"));
        assertFalse(contentTypeHeader.contains("something"));
    }
}