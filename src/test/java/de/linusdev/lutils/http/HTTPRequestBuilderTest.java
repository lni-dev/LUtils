package de.linusdev.lutils.http;

import de.linusdev.lutils.http.body.Body;
import de.linusdev.lutils.http.header.HeaderMap;
import de.linusdev.lutils.http.header.HeaderNames;
import de.linusdev.lutils.http.header.contenttype.ContentType;
import de.linusdev.lutils.http.header.contenttype.ContentTypes;
import de.linusdev.lutils.http.method.Methods;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("ExtractMethodRecommender")
class HTTPRequestBuilderTest {

    @Test
    void GET() throws IOException {
        HTTPRequestBuilder builder = new HTTPRequestBuilder();
        builder.GET("www.example.de/test");

        ByteArrayOutputStream stream = new ByteArrayOutputStream();


        builder.build(stream, 2048);

        assertEquals(
                "GET www.example.de/test HTTP/1.1\r\n" +
                        "\r\n",
                stream.toString(StandardCharsets.UTF_8)
        );
    }

    @Test
    void POST() throws IOException {
        HTTPRequestBuilder builder = new HTTPRequestBuilder();
        builder.POST("/test", new Body() {
            @Override
            public ContentType contentType() {
                return ContentType.of("text/plain");
            }

            @Override
            public int length() {
                return "test body".getBytes(StandardCharsets.UTF_8).length;
            }

            @Override
            public @NotNull InputStream stream() {
                return new ByteArrayInputStream("test body".getBytes(StandardCharsets.UTF_8));
            }
        });

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        builder.build(stream, 2048);
        assertEquals(
                "POST /test HTTP/1.1\r\n" +
                        "Content-Length: " + "test body".getBytes(StandardCharsets.UTF_8).length + "\r\n" +
                        "Content-Type: text/plain\r\n" +
                        "\r\ntest body",
                stream.toString(StandardCharsets.UTF_8)
        );
    }

    @Test
    void build() throws IOException {
        HTTPRequestBuilder builder = new HTTPRequestBuilder();
        builder.setHeader("test", "wow");
        builder.setMethod(Methods.PUT);
        builder.setPath("/abc");
        builder.setBody(new Body() {
            @Override
            public @Nullable ContentType contentType() {
                return null;
            }

            @Override
            public int length() {
                return -1;
            }

            @Override
            public @NotNull InputStream stream() {
                return new ByteArrayInputStream("some body text".getBytes(StandardCharsets.UTF_8));
            }
        });

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        builder.build(stream, 2048);

        assertEquals("PUT /abc HTTP/1.1\r\n" +
                "Content-Length: " + "some body text".getBytes(StandardCharsets.UTF_8).length +"\r\n" +
                "test: wow\r\n" +
                "\r\n" +
                "some body text", stream.toString(StandardCharsets.UTF_8));
    }

    @Test
    void setHeader() throws IOException {

        HTTPRequestBuilder builder;
        ByteArrayOutputStream stream;

        builder = new HTTPRequestBuilder()
                .GET("test")
                .setHeader(HeaderNames.CONTENT_TYPE, ContentTypes.Text.csv());

        stream = new ByteArrayOutputStream();
        builder.build(stream, 2048);

        assertEquals(
                "GET test HTTP/1.1\r\n" +
                        "Content-Type: text/csv\r\n" +
                        "\r\n",
                stream.toString(StandardCharsets.UTF_8)
        );

        builder = new HTTPRequestBuilder()
                .GET("test")
                .setHeader(HeaderNames.CONTENT_TYPE, ContentTypes.Text.csv().asString());

        stream = new ByteArrayOutputStream();
        builder.build(stream, 2048);

        assertEquals(
                "GET test HTTP/1.1\r\n" +
                        "Content-Type: text/csv\r\n" +
                        "\r\n",
                stream.toString(StandardCharsets.UTF_8)
        );


        HeaderMap map = new HeaderMap();
        map.put("test", "abc");
        builder = new HTTPRequestBuilder()
                .GET("test")
                .setHeaders(map);

        stream = new ByteArrayOutputStream();
        builder.build(stream, 2048);

        assertEquals(
                "GET test HTTP/1.1\r\n" +
                        "test: abc\r\n" +
                        "\r\n",
                stream.toString(StandardCharsets.UTF_8)
        );

    }

    @Test
    void setHeaders() {
    }
}