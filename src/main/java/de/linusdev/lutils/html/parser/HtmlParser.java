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
import de.linusdev.lutils.html.HtmlObjectType;
import de.linusdev.lutils.html.impl.HtmlPage;
import de.linusdev.lutils.result.BiResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.Reader;

@SuppressWarnings("ClassCanBeRecord")
public class HtmlParser {

    private final @NotNull Registry registry;

    public HtmlParser(@NotNull Registry registry) {
        this.registry = registry;
    }

    public @NotNull HtmlPage parsePage(@NotNull Reader reader) throws IOException, ParseException {
        return HtmlPage.PARSER.parse(new HtmlParserState(null, this), new HtmlReader(reader));
    }

    /**
     * If a html object is present, it reads exactly one html object using {@link #parse(HtmlParserState, HtmlReader)}.
     * If no html object is present, {@code null} is returned. <br>
     * This method will skip all spaces and new lines. Thus, it should
     * only be called to start the parsing of html or if a line feed was just parsed.
     * @param reader reader to read from.
     * @param state {@link HtmlParserState} to use while parsing.
     * @return {@link #parse(HtmlParserState, HtmlReader)} if an object is present
     * @throws IOException see {@link #parse(HtmlParserState, HtmlReader)}
     */
    public @Nullable HtmlObject parseIfPresent(@NotNull HtmlParserState state, @NotNull HtmlReader reader) throws IOException, ParseException {
        if(!reader.availableSkipNewLinesAndSpaces())
            return null;

        return parse(state, reader);
    }

    /**
     * Parses exactly one html object from given reader. If no html object is present
     * an exception will be thrown. Can parse object of the following types:
     * <ul>
     *     <li>{@link HtmlObjectType#DOC_TYPE}</li>
     *     <li>{@link HtmlObjectType#TEXT}</li>
     *     <li>{@link HtmlObjectType#COMMENT}</li>
     *     <li>{@link HtmlObjectType#ELEMENT}</li>
     * </ul>
     * @param reader reader to read the element from
     * @param state {@link HtmlParserState} to use while parsing.
     * @return parsed {@link HtmlObject}
     * @throws IOException possible while reading.
     */
    public @NotNull HtmlObject parse(@NotNull HtmlParserState state, @NotNull HtmlReader reader) throws IOException, ParseException {

        char[] buf = new char[3];

        buf[0] = reader.skipNewLinesAndFollowingSpaces();

        if(buf[0] == '<') {
            buf[1] = reader.read();

            if(buf[1] == '!') {
                buf[2] = reader.read();

                if(buf[2] == '-') {
                    //comment
                    reader.pushBack(buf, 3);
                    return registry.getCommentParser().parse(state, reader);
                }

                // doc type
                reader.pushBack(buf, 3);
                return registry.getDocTypeParser().parse(state, reader);
            }

            // element
            reader.pushBack(buf[1]);
            return parseElement(state, reader);
        }

        // text
        reader.pushBack(buf[0]);
        return registry.getTextParser().parse(state, reader);
    }

    /**
     * Opening {@code <} of the element tag should already be read, when this method is called.
     * @param reader reader to read the element from.
     * @param state {@link HtmlParserState} to use while parsing.
     * @return {@link HtmlElement}, which was parsed.
     * @throws IOException while reading.
     */
    private @NotNull HtmlElement parseElement(@NotNull HtmlParserState state, @NotNull HtmlReader reader) throws IOException, ParseException {
        BiResult<String, Character> res = reader.readUntil(' ', '>');
        HtmlElementType<?> type = registry.getElementTypeByName(res.result1());
        reader.pushBack(res.result2());
        reader.pushBack(res.result1());
        reader.pushBack('<');


        return type.parser().parse(state, reader);
    }

    public @NotNull Registry getRegistry() {
        return registry;
    }

}
