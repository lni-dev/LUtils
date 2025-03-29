/*
 * Copyright (c) 2023-2025 Linus Andera
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.linusdev.lutils.net.http;

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
        assertEquals("/test.html", parsed.getPathAndQueryAsString());
        assertEquals("HTTP/1.1", parsed.getVersion().asString());

        assertEquals(1, parsed.getHeaders().size());
        Header contentType = parsed.getHeaders().get("content-type");

        assertEquals("application/json", contentType.getValue());
        assertNotNull(parsed.getBody());
    }

    @Test
    public void test2() throws IOException {

        String request =
                "GET /test.html?a=b HTTP/1.1\r\n" +
                        "content-type: application/json\r\n";

        HTTPRequest<InputStream> parsed = HTTPRequest.parse(new ByteArrayInputStream(request.getBytes()));

        assertTrue(RequestMethod.equals(Methods.GET, parsed.getMethod()));
        assertEquals("/test.html?a=b", parsed.getPathAndQueryAsString());
        assertEquals("/test.html", parsed.getPathAndQuery().getPath());
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
        assertEquals("/test.html", parsed.getPathAndQueryAsString());
        assertEquals("/test.html", parsed.getPathAndQuery().getPath());
        assertEquals("HTTP/1.1", parsed.getVersion().asString());

        assertEquals(1, parsed.getHeaders().size());
        Header contentType = parsed.getHeaders().get("content-type");

        assertEquals("application/json", contentType.getValue());
        assertEquals("{}", parsed.getBody());
    }

}