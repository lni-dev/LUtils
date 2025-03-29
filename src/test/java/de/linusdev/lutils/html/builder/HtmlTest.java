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

package de.linusdev.lutils.html.builder;

import de.linusdev.lutils.html.HtmlObject;
import de.linusdev.lutils.html.impl.HtmlPage;
import de.linusdev.lutils.html.impl.element.StandardHtmlElementTypes;
import de.linusdev.lutils.html.parser.*;
import de.linusdev.lutils.other.parser.ParseException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HtmlTest {

    @Test
    void buildPage() throws IOException {

        HtmlPage page = Html.buildPage(head -> {
            head.addElement(StandardHtmlElementTypes.TITLE, title -> {
                title.addText("Test Page");
            });
        }, body -> {
            body.addElement(StandardHtmlElementTypes.H1, h1 -> {
                h1.addText("Hello World");
            });
            body.addElement(StandardHtmlElementTypes.P, p -> {
                p.addText("This works wonderful");
            });
        });

        StringWriter writer = new StringWriter();
        page.write(writer);

        assertEquals("""
                <!doctype html>
                <html>
                  <head>
                    <title>Test Page</title>
                  </head>
                  <body>
                    <h1>Hello World</h1>
                    <p>This works wonderful</p>
                  </body>
                </html>""", writer.toString());
    }

    @Test
    void childrenTest() throws IOException, ParseException {
        String html = """
                <div>
                  Some Text
                  <li>test</li>
                  <div>a</div>
                  more text
                  <div></div>
                </div>""";

        HtmlParser parser = new HtmlParser(Registry.getDefault().build());
        HtmlObject parsed = parser.parse(new HtmlParserState(null, parser), new HtmlReader(new StringReader(html)));

        StringBuilder sb = new StringBuilder();
        parsed.asHtmlElement().children().forEachRemaining(element -> sb.append(element.tag().name()).append(" "));
        assertEquals("li div div ", sb.toString());
    }

    private static Stream<Arguments> provideStringsForTest3() {
        return Stream.of(
                Arguments.of("<div>  </div>", "<div>\n  \n</div>"),
                Arguments.of("<div></div>", "<div></div>"),
                Arguments.of("<div>Some <span>test</span> here</div>", "<div>\nSome \n<span>test</span>\n here\n</div>"),
                Arguments.of("<div>Some<span>test</span>here</div>", "<div>\nSome\n<span>test</span>\nhere\n</div>"),
                Arguments.of("<span>Some <span>te</span><span>st</span></span>", "<span>Some <span>te</span><span>st</span></span>")
        );
    }

    @ParameterizedTest()
    @MethodSource(value = "provideStringsForTest3")
    void test3(String html, String result) throws IOException, ParseException {

        HtmlParser parser = new HtmlParser(Registry.getDefault().build());
        HtmlObject parsed = parser.parse(new HtmlParserState(null, parser), new HtmlReader(new StringReader(html)));

        StringWriter writer = new StringWriter();
        parsed.write(new HtmlWritingState(""), writer);
        System.out.println(writer);
        assertEquals(result, writer.toString());
    }
}