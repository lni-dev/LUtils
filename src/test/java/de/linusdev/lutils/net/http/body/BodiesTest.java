package de.linusdev.lutils.net.http.body;

import de.linusdev.lutils.net.http.HTTPResponse;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BodiesTest {

    @Test
    void html() throws IOException {
        String response = HTTPResponse.builder()
                .setBody(Bodies.html().ofResource(BodiesTest.class, "index.html"))
                .buildResponse();

        String content = Files.readString(Paths.get("src/test/resources/de/linusdev/lutils/net/http/body/index.html"), StandardCharsets.UTF_8);

        assertEquals("HTTP/1.1 200 OK\r\n" +
                     "Content-Length: " + content.getBytes(StandardCharsets.UTF_8).length + "\r\n" +
                     "Content-Type: text/html\r\n" +
                     "\r\n" + content,
                response
        );
    }

    @Test
    void css() throws IOException {
        Path res = Paths.get("src/test/resources/de/linusdev/lutils/net/http/body/index.css");
        String response = HTTPResponse.builder()
                .setBody(Bodies.css().ofRegularFile(res))
                .buildResponse();

        String content = Files.readString(res, StandardCharsets.UTF_8);

        assertEquals("HTTP/1.1 200 OK\r\n" +
                      "Content-Length: " + content.getBytes(StandardCharsets.UTF_8).length + "\r\n" +
                      "Content-Type: text/css\r\n" +
                      "\r\n" + content,
                response
        );
    }
}