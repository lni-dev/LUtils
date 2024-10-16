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

/**
 * This class should be used to read attributes from html element tags.
 */
public class AttributeReader {

    private final @NotNull HtmlReader reader;

    public AttrReaderState state = READING;

    public AttributeReader(@NotNull HtmlReader reader) {
        this.reader = reader;
    }

    /**
     * Checks if an attribute name can be read and reads it, if one is available. Also changes the
     * {@link #state} of this attribute reader.
     * If {@link #state} is {@link AttrReaderState#READING} or {@link AttrReaderState#ATTR_VALUE} after this method
     * returned, then the return value was definitely not {@code null}.
     * @return the read attribute name or {@code null} if no attribute name could be read.
     * @throws IOException while reading
     * @throws ParseException while parsing
     */
    public String readAttributeName() throws IOException, ParseException {
        char r = reader.skipNewLinesAndSpaces();

        if (r == '>') {
            state = TAG_END;
            return null;
        } else if (r == '/') {
            if ((r = reader.read()) != '>')
                throw new ParseException(r);

            state = TAG_SELF_CLOSE;
            return null;
        }

        reader.pushBack(r);
        var res = reader.readUntil('=', ' ', '>', '/');

        if (res.result2() == '>') {
            state = TAG_END;
            return res.result1();
        } else if (res.result2() == '/') {
            if ((r = reader.read()) != '>')
                throw new ParseException(r);

            state = TAG_SELF_CLOSE;
            return res.result1();
        } else if (res.result2() == ' ') {
            state = READING;
            return res.result1();
        }

        state = ATTR_VALUE;
        return res.result1();
    }

    /**
     * Caller must ensure, that {@link #state} is {@link AttrReaderState#ATTR_VALUE} when this method is called.
     * @return attribute value as string.
     * @throws IOException while reading.
     */
    public @NotNull String readAttributeValue() throws IOException {
        if (state != ATTR_VALUE) throw new IllegalStateException("State is not ATTR_VALUE, but is " + state);

        state = READING;
        return reader.readString();
    }
}