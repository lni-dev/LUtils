package de.linusdev.lutils.html;

import de.linusdev.lutils.html.builder.Html;
import de.linusdev.lutils.html.impl.HtmlPage;
import de.linusdev.lutils.html.impl.StandardHtmlAttributeTypes;
import de.linusdev.lutils.html.parser.HtmlParser;
import de.linusdev.lutils.html.parser.HtmlWritingState;
import de.linusdev.lutils.html.parser.ParseException;
import de.linusdev.lutils.html.parser.Registry;
import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import static de.linusdev.lutils.html.impl.element.StandardHtmlElementTypes.DIV;
import static de.linusdev.lutils.html.impl.element.StandardHtmlElementTypes.PARAGRAPH;
import static org.junit.jupiter.api.Assertions.assertEquals;

class HtmlElementTest {

    @Test
    void test() throws IOException {
        HtmlElement element = Html.buildElement(DIV, container -> {
            container.addElement(DIV, header -> {
                header.addText("Header");
                header.addAttribute(StandardHtmlAttributeTypes.CLASS, "test");
            });
            container.addElement(DIV, body -> {
                body.addElement(PARAGRAPH, p -> p.addText("Some Text in the body"));
            });
        });

        StringWriter writer = new StringWriter();
        element.write(new HtmlWritingState("  "), writer);
        System.out.println(writer);

        assertEquals("""
                <div>
                  <div class="test">
                    Header
                  </div>
                  <div>
                    <p>Some Text in the body</p>
                  </div>
                </div>""", writer.toString());
    }

    @Test
    void testRead() throws IOException, ParseException {
        @Language("html") String html =  """
                <!doctype html>
                <div>
                  <div class="test">
                    <!-- comment test -->
                    Header <a href='https://www.linusdev.de'>Here</a>
                  </div>
                  <div>
                    <p>Some<br>Text
                        in the body
                    </p>
                  </div>
                </div>""";

        HtmlParser parser = new HtmlParser(Registry.getDefault().build());

        HtmlPage page = parser.parsePage(new StringReader(html));

        StringWriter writer = new StringWriter();
        page.write(writer);
        assertEquals("""
                <!doctype html>
                <div>
                  <div class="test">
                    <!-- comment test -->
                    Header\s
                    <a href="https://www.linusdev.de">
                      Here
                    </a>
                  </div>
                  <div>
                    <p>Some<br>Text in the body</p>
                  </div>
                </div>""", writer.toString());
    }

    @Test
    void testClone() throws IOException, ParseException {
        @Language("html") String html =  """
                <!doctype html>
                <div>
                  <div class="test">
                    <!-- comment test -->
                    Header <a href='https://www.linusdev.de'>Here</a>
                  </div>
                  <div>
                    <p>Some<br>Text
                        in the body
                    </p>
                  </div>
                </div>""";

        HtmlParser parser = new HtmlParser(Registry.getDefault().build());

        HtmlPage page = parser.parsePage(new StringReader(html));
        page = page.copy();

        StringWriter writer = new StringWriter();
        page.write(writer);
        assertEquals("""
                <!doctype html>
                <div>
                  <div class="test">
                    <!-- comment test -->
                    Header\s
                    <a href="https://www.linusdev.de">
                      Here
                    </a>
                  </div>
                  <div>
                    <p>Some<br>Text in the body</p>
                  </div>
                </div>""", writer.toString());
    }
}