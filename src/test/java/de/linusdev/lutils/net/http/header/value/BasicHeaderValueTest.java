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

package de.linusdev.lutils.net.http.header.value;

import de.linusdev.lutils.net.http.HTTPMessageBuilder;
import de.linusdev.lutils.net.http.HTTPRequest;
import de.linusdev.lutils.net.http.body.Body;
import de.linusdev.lutils.net.http.body.BodyParsers;
import de.linusdev.lutils.net.http.header.HeaderNames;
import de.linusdev.lutils.net.http.header.contenttype.ContentType;
import de.linusdev.lutils.net.http.header.contenttype.ContentTypes;
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
                    public long length() {
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