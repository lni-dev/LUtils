package de.linusdev.lutils.html.lhtml;

import de.linusdev.lutils.html.HtmlAddable;
import de.linusdev.lutils.html.parser.HtmlParser;
import de.linusdev.lutils.html.parser.ParseException;
import de.linusdev.lutils.io.ResourceUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LhtmlPageTest {

    @Test
    void test() throws IOException, ParseException {
        String html = ResourceUtils.readString(LhtmlPageTest.class, "page.html");

        LhtmlPage page = LhtmlPage.parse(new HtmlParser(Lhtml.getRegistry()), new StringReader(html));
        HtmlAddable header = page.getPlaceholder("header");
        LhtmlTemplateElement item = page.getTemplate("item");
        HtmlAddable name = item.getPlaceholder("name");

        name.addText("Sophie");
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
                        <div lhtml-template="item">
                          <h1>Hello <span lhtml-placeholder="name">Sophie</span>!</h1>
                          <p><span lhtml-placeholder="name">Sophie</span>is pretty.</p>
                        </div>
                      </div>
                      <div lhtml-placeholder="body"></div>
                    </div>
                  </body>
                </html>""", page.writeToString());
        System.out.println(page.writeToString());
    }

    @Test
    void test2() throws IOException, ParseException {
        String html = ResourceUtils.readString(LhtmlPageTest.class, "page2.html");

        LhtmlPage page = LhtmlPage.parse(new HtmlParser(Lhtml.getRegistry()), new StringReader(html));
        HtmlAddable header = page.getPlaceholder("header");

        LhtmlTemplateElement item = page.getTemplate("item").copy();
        HtmlAddable name = item.getPlaceholder("name");
        HtmlAddable list = item.getPlaceholder("list");

        LhtmlTemplateElement listItem = item.getTemplate("item").copy();
        listItem.getPlaceholder("id").addText("0");

        list.addContent(listItem);

        listItem = item.getTemplate("item").copy();
        listItem.getPlaceholder("id").addText("1");

        list.addContent(listItem);

        name.addText("Sophie");
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
                        <div lhtml-template="item">
                          <h1>Hello <span lhtml-placeholder="name">Sophie</span>!</h1>
                          <p><span lhtml-placeholder="name">Sophie</span>is pretty.</p>
                          <ul lhtml-placeholder="list">
                            <li lhtml-template="item">
                              Item\s
                              <span lhtml-placeholder="id">0</span>
                            </li>
                            <li lhtml-template="item">
                              Item\s
                              <span lhtml-placeholder="id">1</span>
                            </li>
                          </ul>
                        </div>
                      </div>
                      <div lhtml-placeholder="body"></div>
                    </div>
                  </body>
                </html>""", page.writeToString());
        System.out.println(page.writeToString());
    }
}