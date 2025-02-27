package de.linusdev.lutils.html;

import de.linusdev.lutils.html.impl.element.StandardHtmlElement;
import de.linusdev.lutils.html.impl.element.StandardHtmlElementTypes;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HtmlAddableTest {

    @Test
    void addCssLink() {
        StandardHtmlElement ele = StandardHtmlElementTypes.DIV.builder()
                .addCssLink("some/other/link")
                .build();

        ele.addCssLink("some/link");

        assertEquals("""
                <div>
                  <link rel="stylesheet" href="some/other/link">
                  <link rel="stylesheet" href="some/link">
                </div>""", ele.writeToString()
        );
    }
}