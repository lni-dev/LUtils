package de.linusdev.lutils.html;

import de.linusdev.lutils.html.builder.Html;
import de.linusdev.lutils.html.parser.HtmlParserState;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringWriter;

import static de.linusdev.lutils.html.StandardHtmlElementTypes.DIV;
import static de.linusdev.lutils.html.StandardHtmlElementTypes.PARAGRAPH;
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
        element.write(new HtmlParserState("  "), writer);
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
}