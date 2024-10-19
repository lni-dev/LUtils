package de.linusdev.lutils.html.lhtml;

import de.linusdev.lutils.html.EditableHtmlElement;
import de.linusdev.lutils.html.parser.HtmlParser;
import de.linusdev.lutils.html.parser.ParseException;
import de.linusdev.lutils.html.parser.Registry;
import de.linusdev.lutils.io.ResourceUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LhtmlPageTest {

    @Test
    void test() throws IOException, ParseException {
        String html = ResourceUtils.readString(LhtmlPageTest.class, "page.html");

        LhtmlPage page = LhtmlPage.parse(new HtmlParser(Registry.getDefault()), new StringReader(html));
        EditableHtmlElement header = page.getPlaceholder("header");
        LhtmlTemplate item = page.getTemplate("item");
        EditableHtmlElement name = item.getPlaceholder("name");

        name.addText("World");
        header.addContent(item);

        assertEquals("""
                <!doctype html>
                <html lang="en">
                  <head>
                    <title>Test</title>
                  </head>
                  <body>
                    <div>
                      <div lhtml-placeholder="header">
                        <p lhtml-template="item">Hello <span lhtml-placeholder="name">World</span>!</p>
                      </div>
                      <div lhtml-placeholder="body"></div>
                    </div>
                  </body>
                </html>""", page.writeToString());
        System.out.println(page.writeToString());
    }
}