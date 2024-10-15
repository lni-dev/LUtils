/*
 * Copyright (c) 2024 Linus Andera
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

package de.linusdev.lutils.html.parser;

import de.linusdev.lutils.html.HtmlElement;
import de.linusdev.lutils.html.HtmlElementType;
import de.linusdev.lutils.html.HtmlObject;
import de.linusdev.lutils.html.Registry;
import de.linusdev.lutils.result.BiResult;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class HtmlParser {

    private final @NotNull Registry registry;

    public HtmlParser(@NotNull Registry registry) {
        this.registry = registry;
    }

    public @NotNull HtmlObject parse(@NotNull HtmlReader reader) throws IOException {

        char[] buf = new char[3];

        buf[0] = reader.skipNewLines();

        if(buf[0] == '<') {
            buf[1] = reader.read();

            if(buf[1] == '!') {
                buf[2] = reader.read();

                if(buf[2] == '-') {
                    //comment
                    reader.pushBack(buf, 0, 3);
                    return null; // TODO
                }

                // doc type
                reader.pushBack(buf, 0, 3);
                return registry.getDocTypeParser().parse(this, reader);
            }

            // element
            return parseElement(reader);
        }

        // text
        reader.pushBack(buf[0]);
        return registry.getTextParser().parse(this, reader);
    }

    /**
     * Opening {@code <} should already be read, when this method is called.
     * @param reader
     * @return
     * @throws IOException
     */
    private HtmlElement parseElement(@NotNull HtmlReader reader) throws IOException {
        BiResult<String, Character> res = reader.readUntil(' ', '>', false);
        HtmlElementType<?> type = registry.getElementTypeByName(res.result1());

        return type.parser().parse(this, reader);
    }

    public @NotNull Registry getRegistry() {
        return registry;
    }
}
