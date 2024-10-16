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

package de.linusdev.lutils.html.impl;

import de.linusdev.lutils.html.*;
import de.linusdev.lutils.html.builder.HtmlElementBuilder;
import de.linusdev.lutils.html.parser.*;
import de.linusdev.lutils.result.BiResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.Writer;
import java.util.*;
import java.util.function.Consumer;

import static de.linusdev.lutils.html.parser.AttrReaderState.*;

@SuppressWarnings("ClassCanBeRecord")
public class StandardHtmlElement implements HtmlElement, HtmlWritable {

    private final @NotNull StandardHtmlElementTypes.Type tag;
    private final @NotNull List<@NotNull HtmlObject> content;
    private final @NotNull Map<String, HtmlAttribute> attributes;

    public StandardHtmlElement(
            @NotNull StandardHtmlElementTypes.Type tag,
            @NotNull List<@NotNull HtmlObject> content,
            @NotNull Map<String, HtmlAttribute> attributes
    ) {
        this.tag = tag;
        this.content = content;
        this.attributes = attributes;
    }

    @Override
    public @NotNull HtmlObjectType type() {
        return HtmlObjectType.ELEMENT;
    }

    @Override
    public @NotNull StandardHtmlElementTypes.Type tag() {
        return tag;
    }

    @Override
    public @NotNull List<@NotNull HtmlObject> content() {
        return content;
    }

    @Override
    public @NotNull Iterator<@NotNull HtmlElement> children() {
        return new Iterator<>() {

            final Iterator<@NotNull HtmlObject> it = content.iterator();
            @Nullable HtmlElement next = null;

            @Override
            public boolean hasNext() {
                if (next == null && !it.hasNext())
                    return false;
                if (next != null)
                    return true;
                getNext();
                return hasNext();
            }

            @Override
            public HtmlElement next() {
                if (next == null) {
                    getNext();
                    return next();
                }

                HtmlElement buf = next;
                next = null;
                return buf;
            }

            private void getNext() {
                if (next != null) return;
                HtmlObject obj = it.next();
                while (obj.type() != HtmlObjectType.ELEMENT && it.hasNext()) {
                    obj = it.next();
                }

                if (obj.type() != HtmlObjectType.ELEMENT)
                    return;

                next = obj.asHtmlElement();
            }
        };
    }

    @Override
    public @NotNull Map<String,HtmlAttribute> attributes() {
        return attributes;
    }

    @Override
    public void write(@NotNull HtmlParserState state, @NotNull Writer writer) throws IOException {
        writer.append("<").append(tag.name());

        for (HtmlAttribute attr : attributes.values()) {
            writer.append(" ").append(attr.type().name());

            if(attr.value() != null) {
                writer.append("=\"").append(attr.value()).append("\"");
            }
        }

        if(content.isEmpty()) {
            writer.append("/>");
        } else {
            writer.append(">");

            if(!tag.isInline()) {
                state.addIndent();
            }

            for (@NotNull HtmlObject obj : content) {
                if(!tag.isInline()) writer.append("\n").append(state.getIndent());
                obj.write(state, writer);
            }

            if(!tag.isInline()) {
                state.removeIndent();
                writer.append("\n").append(state.getIndent());
            }

            writer.append("</").append(tag.name()).append(">");
        }
    }

    public static class Builder implements HtmlElementBuilder {

        private final @NotNull StandardHtmlElementTypes.Type tag;
        private final @NotNull List<@NotNull HtmlObject> content;
        private final @NotNull Map<String, HtmlAttribute> attributes;

        public Builder(@NotNull StandardHtmlElementTypes.Type tag) {
            this.tag = tag;
            this.content = new ArrayList<>();
            this.attributes = new HashMap<>();
        }

        public <B extends HtmlElementBuilder> void addElement(@NotNull HtmlElementType<B> type, Consumer<B> adjuster) {
            B builder = type.builder();
            adjuster.accept(builder);
            content.add(builder.build());
        }

        public void addAttribute(@NotNull HtmlAttributeType type, @Nullable String value) {
            attributes.put(type.name(), new StandardHtmlAttribute(type, value));
        }

        public void addText(@NotNull String text) {
            content.add(new HtmlText(text));
        }

        @Override
        public @NotNull HtmlElement build() {
            return new StandardHtmlElement(tag, content, attributes);
        }
    }

    public static class Parser implements HtmlObjectParser<StandardHtmlElement> {

        private final @NotNull StandardHtmlElementTypes.Type type;

        public Parser(@NotNull StandardHtmlElementTypes.Type type) {
            this.type = type;
        }

        @Override
        public @NotNull StandardHtmlElement parse(@NotNull HtmlParser parser, @NotNull HtmlReader reader) throws IOException, ParseException {
            char c = reader.read();
            if(c != '<')
                throw new ParseException(c);

            BiResult<String, Character> res = reader.readUntil(' ', '>');
            String tag = res.result1();

            if(tag.charAt(tag.length()-1) == '/' && res.result2() == '>') {
                if(!tag.substring(0, tag.length() - 1).equals(type.name()))
                    throw new ParseException("Illegal tag name '" + tag + "'.");

                // is immediately closed
                return new StandardHtmlElement(type, List.of(), Map.of());
            }

            if(!tag.equals(type.name()))
                throw new ParseException("Illegal tag name '" + tag + "'.");

            Map<String, HtmlAttribute> attributes = new HashMap<>();

            if(res.result2() == ' ') {
                // Read attributes
                AttributeReader attrReader = reader.getAttributeReader();

                while(attrReader.state != TAG_END) {
                    String name = attrReader.readAttributeName();

                    if(attrReader.state == TAG_SELF_CLOSE) {
                        if(name != null) {
                            HtmlAttributeType attrType = parser.getRegistry().getAttributeTypeByName(name);
                            attributes.put(name, new StandardHtmlAttribute(attrType, null));
                        }

                        return new StandardHtmlElement(type, List.of(), attributes);
                    } else if(attrReader.state == ATTR_VALUE) {
                        String value = attrReader.readAttributeValue();
                        HtmlAttributeType attrType = parser.getRegistry().getAttributeTypeByName(name);
                        attributes.put(name, new StandardHtmlAttribute(attrType, value));
                    } else if(name != null) {
                        HtmlAttributeType attrType = parser.getRegistry().getAttributeTypeByName(name);
                        attributes.put(name, new StandardHtmlAttribute(attrType, null));
                    }
                }
            }

            // read content
            ArrayList<HtmlObject> content = new ArrayList<>();

            for(;;) {
                c = reader.skipNewLinesAndSpaces();

                if(c == '<') {
                    c = reader.read();
                    if(c == '/') {
                        // end tag
                        break;
                    }
                    reader.pushBack(c);
                    reader.pushBack('<');
                    content.add(parser.parse(reader));
                } else {
                    reader.pushBack(c);
                    content.add(parser.parse(reader));
                }
            }

            tag = reader.readUntil('>').trim();

            if(!tag.equals(type.name()))
                throw new ParseException("Illegal end tag name '" + tag + "', should be '" + type.name() + "'.");

            return new StandardHtmlElement(type, content, attributes);
        }
    }
}
