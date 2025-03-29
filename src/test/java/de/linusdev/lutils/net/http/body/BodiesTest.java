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