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