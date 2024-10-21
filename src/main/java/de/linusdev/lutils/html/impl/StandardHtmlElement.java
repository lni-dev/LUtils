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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static de.linusdev.lutils.html.parser.AttrReaderState.*;

@SuppressWarnings("ClassCanBeRecord")
public class StandardHtmlElement implements EditableHtmlElement {

    protected final @NotNull Type tag;
    protected final @NotNull List<@NotNull HtmlObject> content;
    protected final @NotNull Map<String, HtmlAttribute> attributes;

    protected StandardHtmlElement(
            @NotNull Type tag,
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
    public @NotNull Type tag() {
        return tag;
    }

    @Override
    public @NotNull List<@NotNull HtmlObject> content() {
        return content;
    }

    @Override
    public @NotNull Map<String,HtmlAttribute> attributes() {
        return attributes;
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public @NotNull StandardHtmlElement clone() {
        List<@NotNull HtmlObject> content = new ArrayList<>(this.content.size());
        Map<String, HtmlAttribute> attributes = new HashMap<>(this.attributes.size());

        this.content.forEach(object -> content.add(object.clone()));
        this.attributes.forEach((key, attr) -> attributes.put(key, attr.clone()));
        
        return new StandardHtmlElement(tag, content, attributes);
    }

    @Override
    public void write(@NotNull HtmlWritingState state, @NotNull Writer writer) throws IOException {
        writer.append("<").append(tag.name());

        for (HtmlAttribute attr : attributes.values()) {
            writer.append(" ").append(attr.type().name());

            if(attr.value() != null) {
                writer.append("=\"").append(attr.value()).append("\"");
            }
        }

        if(tag.isVoidElement()) {
            writer.append(">");
        } else if(content.isEmpty()) {
            writer.append(">");
            writer.append("</").append(tag.name()).append(">");
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

    public static class Type implements HtmlElementType<StandardHtmlElement.Builder> {

        /**
         * Normal element with content and attributes. Will be written as
         * <pre>{@code
         * <tag attr="value">
         *     content
         * </tag>
         * }</pre>
         * @param name tag name
         * @return {@link HtmlAttributeType} as described above
         */
        public static @NotNull Type newNormal(@NotNull String name)  {
            return new Type(name, false, false);
        }

        /**
         * Void element with attributes and <b>no</b> content. Will be written as
         * <pre>{@code
         * <tag attr="value">
         * }</pre>
         * @param name tag name
         * @return {@link HtmlAttributeType} as described above
         */
        public static @NotNull Type newVoid(@NotNull String name)  {
            return new Type(name, false, true);
        }

        /**
         * Normal element with content and attributes. Will be written as
         * <pre>{@code
         * <tag attr="value">content</tag>
         * }</pre>
         * @param name tag name
         * @return {@link HtmlAttributeType} as described above
         */
        public static @NotNull Type newInline(@NotNull String name)  {
            return new Type(name, true, false);
        }

        private final @NotNull String name;
        private final boolean inline;
        private final boolean voidElement;

        public Type(@NotNull String name, boolean inline, boolean voidElement) {
            this.name = name;
            this.inline = inline;
            this.voidElement = voidElement;
        }

        @Override
        public @NotNull String name() {
            return name;
        }

        @Override
        public @NotNull StandardHtmlElement.Builder builder() {
            return new StandardHtmlElement.Builder(this);
        }

        @Override
        public @NotNull HtmlObjectParser<? extends HtmlElement> parser() {
            return new StandardHtmlElement.Parser(this);
        }

        @SuppressWarnings("BooleanMethodIsAlwaysInverted")
        public boolean isInline() {
            return inline;
        }

        public boolean isVoidElement() {
            return voidElement;
        }
    }

    public static class Builder implements HtmlElementBuilder {

        private final @NotNull Type tag;
        private final @NotNull List<@NotNull HtmlObject> content;
        private final @NotNull Map<String, HtmlAttribute> attributes;

        public Builder(@NotNull Type tag) {
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

        private final @NotNull Type type;

        public Parser(@NotNull Type type) {
            this.type = type;
        }

        @Override
        public @NotNull StandardHtmlElement parse(@NotNull HtmlParserState state, @NotNull HtmlReader reader) throws IOException, ParseException {
            char c = reader.read();
            if(c != '<')
                throw new ParseException(c);

            BiResult<String, Character> res = reader.readUntil(' ', '>');
            String tag = res.result1();

            if(tag.charAt(tag.length()-1) == '/' && res.result2() == '>') {
                if(!tag.substring(0, tag.length() - 1).equals(type.name()))
                    throw new ParseException("Illegal tag name '" + tag + "'.");

                // is immediately closed
                return new StandardHtmlElement(type, new ArrayList<>(), new HashMap<>());
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
                            HtmlAttributeType attrType = state.getRegistry().getAttributeTypeByName(name);
                            attributes.put(name, new StandardHtmlAttribute(attrType, null));
                        }

                        return new StandardHtmlElement(type, new ArrayList<>(), attributes);
                    } else if(attrReader.state == ATTR_VALUE) {
                        String value = attrReader.readAttributeValue();
                        HtmlAttributeType attrType = state.getRegistry().getAttributeTypeByName(name);
                        attributes.put(name, new StandardHtmlAttribute(attrType, value));
                    } else if(name != null) {
                        HtmlAttributeType attrType = state.getRegistry().getAttributeTypeByName(name);
                        attributes.put(name, new StandardHtmlAttribute(attrType, null));
                    }
                }
            }

            if(type.isVoidElement()) {
                return new StandardHtmlElement(type, new ArrayList<>(), attributes);
            }

            // read content
            ArrayList<HtmlObject> content = new ArrayList<>();
            int id = state.onStartParsingContent(type, attributes);

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
                    HtmlObject child = state.onObjectParsed(state.getParser().parse(state, reader));
                    if(child != null) content.add(child);
                } else {
                    reader.pushBack(c);
                    HtmlObject child = state.onObjectParsed(state.getParser().parse(state, reader));
                    if(child != null) content.add(child);
                }
            }

            state.onEndParsingContent(id);
            tag = reader.readUntil('>').trim();

            if(!tag.equals(type.name()))
                throw new ParseException("Illegal end tag name '" + tag + "', should be '" + type.name() + "'.");

            return new StandardHtmlElement(type, content, attributes);
        }
    }
}
