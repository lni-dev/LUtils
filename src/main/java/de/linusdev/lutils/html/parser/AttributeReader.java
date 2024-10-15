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

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import static de.linusdev.lutils.html.parser.AttrReaderState.*;

public class AttributeReader {

    private final @NotNull HtmlReader reader;

    public AttrReaderState state = READING;

    public AttributeReader(@NotNull HtmlReader reader) {
        this.reader = reader;
    }

    public String readAttributeName() throws IOException {
        char r;
        while ((r = reader.read()) == ' ') {
        }
        if (r == '>') {
            state = TAG_END;
            return null;
        } else if (r == '/') {
            if ((r = reader.read()) != '>')
                throw new IOException("Illegal char '" + r + "'.");

            state = TAG_SELF_CLOSE;
            return null;
        }

        var res = reader.readUntil('=', ' ', '>', '/', false);

        if (res.result2() == '>') {
            state = TAG_END;
            return res.result1();
        } else if (res.result2() == '/') {
            if ((r = reader.read()) != '>')
                throw new IOException("Illegal char '" + r + "'.");

            state = TAG_SELF_CLOSE;
            return res.result1();
        } else if (res.result2() == ' ') {
            state = READING;
            return res.result1();
        }

        state = ATTR_VALUE;
        return res.result1();
    }

    public @NotNull String readAttributeValue() throws IOException {
        if (state != ATTR_VALUE) throw new IllegalStateException("State is not ATTR_VALUE, but is " + state);

        state = READING;
        return reader.readString();
    }
}