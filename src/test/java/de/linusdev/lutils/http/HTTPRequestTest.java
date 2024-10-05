/*
 * Copyright (c) 2023 Linus Andera all rights reserved
 */

package de.linusdev.lutils.http;

import de.linusdev.lutils.net.http.HTTPRequest;
import de.linusdev.lutils.net.http.header.Header;
import de.linusdev.lutils.net.http.method.Methods;
import de.linusdev.lutils.net.http.method.RequestMethod;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

class HTTPRequestTest {

    @Test
    public void test() throws IOException {

        String request =
                "GET /test.html HTTP/1.1\r\n" +
                "content-type: application/json\r\n";

        HTTPRequest<InputStream> parsed = HTTPRequest.parse(new ByteArrayInputStream(request.getBytes()));

        assertTrue(RequestMethod.equals(Methods.GET, parsed.getMethod()));
        assertEquals("/test.html", parsed.getPath());
        assertEquals("HTTP/1.1", parsed.getVersion().asString());

        assertEquals(1, parsed.getHeaders().size());
        Header contentType = parsed.getHeaders().get("content-type");

        assertEquals("application/json", contentType.getValue());
        assertNotNull(parsed.getBody());
    }

    @Test
    public void testBody() throws IOException {
        String request = "GET /test.html HTTP/1.1\r\n" +
                "content-type: application/json\r\n" +
                "\r\n" +
                "{}";

        HTTPRequest<String> parsed = HTTPRequest.parse(new ByteArrayInputStream(request.getBytes()), (hs, in) -> {
            InputStreamReader reader = new InputStreamReader(in);
            BufferedReader br = new BufferedReader(reader);
            try {
                return br.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        assertTrue(RequestMethod.equals(Methods.GET, parsed.getMethod()));
        assertEquals("/test.html", parsed.getPath());
        assertEquals("HTTP/1.1", parsed.getVersion().asString());

        assertEquals(1, parsed.getHeaders().size());
        Header contentType = parsed.getHeaders().get("content-type");

        assertEquals("application/json", contentType.getValue());
        assertEquals("{}", parsed.getBody());
    }

}