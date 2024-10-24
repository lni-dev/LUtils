package de.linusdev.lutils.html.builder;

import de.linusdev.lutils.html.impl.HtmlPage;
import de.linusdev.lutils.html.impl.element.StandardHtmlElementTypes;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringWriter;

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
            body.addElement(StandardHtmlElementTypes.PARAGRAPH, p -> {
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
}