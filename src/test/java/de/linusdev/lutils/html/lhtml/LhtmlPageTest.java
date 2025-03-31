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

package de.linusdev.lutils.html.lhtml;

import de.linusdev.lutils.html.HtmlAddable;
import de.linusdev.lutils.html.lhtml.skeleton.LhtmlPageSkeleton;
import de.linusdev.lutils.html.parser.HtmlParser;
import de.linusdev.lutils.io.ResourceUtils;
import de.linusdev.lutils.other.parser.ParseException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LhtmlPageTest {

    @Test
    void test() throws IOException, ParseException {
        String html = ResourceUtils.readString(LhtmlPageTest.class, "page.html");

        LhtmlPageSkeleton pageSkeleton = Lhtml.parsePage(new StringReader(html));
        LhtmlPage page = pageSkeleton.copy();
        HtmlAddable<?> header = page.getPlaceholder("header");
        LhtmlTemplateElement item = page.getTemplate("item");
        HtmlAddable<?> name = item.getPlaceholder("name");

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
                          <p><span lhtml-placeholder="name">Sophie</span> is pretty.</p>
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

        LhtmlPageSkeleton pageSkeleton = LhtmlPage.parse(new HtmlParser(Lhtml.getRegistry().build()), new StringReader(html));
        LhtmlPage page = pageSkeleton.copy();
        HtmlAddable<?> header = page.getPlaceholder("header");

        LhtmlTemplateElement item = page.getTemplate("item");
        item.setValue("clazz", "some-class");
        HtmlAddable<?> name = item.getPlaceholder("name");
        HtmlAddable<?> list = item.getPlaceholder("list");

        LhtmlTemplateElement listItem = item.getTemplate("item");
        listItem.getPlaceholder("id").addText("0");

        list.addContent(listItem);

        listItem = item.getTemplate("item");
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
                          <h1 class="some-class">Hello <span lhtml-placeholder="name">Sophie</span>!</h1>
                          <p><span lhtml-placeholder="name">Sophie</span> is pretty.</p>
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

    @Test
    void test3() throws IOException, ParseException {
        String html = """
                <html>
                <head>
                    <title>title</title>
                </head>
                <body>
                  <div lhtml-placeholder="p"></div>
                  <div lhtml-template="item" id="${id}">
                    <a id="${id}">${name}</a>
                  </div>
                </body>
                </html>""";

        LhtmlPageSkeleton pageSkeleton = LhtmlPage.parse(new HtmlParser(Lhtml.getRegistry().build()), new StringReader(html));
        LhtmlPage page = pageSkeleton.copy();

        LhtmlTemplateElement item = page.getTemplate("item");
        page.getPlaceholder("p").addContent(item);

        item.setValue("id", "value");
        item.setValue("name", "test");

        assertEquals("""
                <html>
                  <head>
                    <title>title</title>
                  </head>
                  <body>
                    <div lhtml-placeholder="p">
                      <div lhtml-template="item" id="value">
                        <a id="value">
                          test
                        </a>
                      </div>
                    </div>
                  </body>
                </html>""", page.writeToString());
        System.out.println(page.writeToString());
    }
}