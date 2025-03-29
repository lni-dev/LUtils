/*
 * Copyright (c) 2025 Linus Andera
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
import de.linusdev.lutils.net.http.status.ResponseStatusCode;
import de.linusdev.lutils.net.http.status.ResponseStatusCodeType;
import de.linusdev.lutils.net.http.status.StatusCodes;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

class HTTPResponseTest {
    @Test
    public void test() throws IOException {
        String response =
                "HTTP/1.1 201 Created\r\n" +
                        "content-type: application/json\r\n";

        HTTPResponse<InputStream> parsed = HTTPResponse.parse(new ByteArrayInputStream(response.getBytes()));

        assertTrue(ResponseStatusCode.equals(StatusCodes.CREATED, parsed.getStatusCode()));
        assertEquals("HTTP/1.1", parsed.getVersion().asString());

        assertEquals(1, parsed.getHeaders().size());
        Header contentType = parsed.getHeaders().get("content-type");

        assertEquals("application/json", contentType.getValue());
        assertNotNull(parsed.getBody());
    }

    @Test
    public void test2() throws IOException {
        String response =
                "HTTP/1.1 103 Test\r\n" +
                        "content-type: application/json\r\n";

        HTTPResponse<InputStream> parsed = HTTPResponse.parse(new ByteArrayInputStream(response.getBytes()));

        assertEquals(103, parsed.getStatusCode().getStatusCode());
        assertEquals("Test", parsed.getStatusCode().getName());
        assertEquals(ResponseStatusCodeType.INFORMATION, parsed.getStatusCode().getType());
        assertEquals("HTTP/1.1", parsed.getVersion().asString());

        assertEquals(1, parsed.getHeaders().size());
        Header contentType = parsed.getHeaders().get("content-type");

        assertEquals("application/json", contentType.getValue());
        assertNotNull(parsed.getBody());
    }

    @Test
    public void testBody() throws IOException {
        String response = "HTTP/1.1 201 Created\r\n" +
                "content-type: application/json\r\n" +
                "\r\n" +
                "{}";

        HTTPResponse<String> parsed = HTTPResponse.parse(new ByteArrayInputStream(response.getBytes()), (hs, in) -> {
            InputStreamReader reader = new InputStreamReader(in);
            BufferedReader br = new BufferedReader(reader);
            try {
                return br.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        assertTrue(ResponseStatusCode.equals(StatusCodes.CREATED, parsed.getStatusCode()));
        assertEquals("HTTP/1.1", parsed.getVersion().asString());

        assertEquals(1, parsed.getHeaders().size());
        Header contentType = parsed.getHeaders().get("content-type");

        assertEquals("application/json", contentType.getValue());
        assertEquals("{}", parsed.getBody());
    }

    @Test
    public void prefabricated() throws IOException {
        var res = HTTPResponse.responses().badRequest().buildResponse();

        assertEquals("""
                HTTP/1.1 400 Bad Request
                
                """, res.replaceAll("\r\n", "\n"));

        res = HTTPResponse.responses().ok().buildResponse();

        assertEquals("""
                HTTP/1.1 200 OK
                
                """, res.replaceAll("\r\n", "\n"));
    }
}