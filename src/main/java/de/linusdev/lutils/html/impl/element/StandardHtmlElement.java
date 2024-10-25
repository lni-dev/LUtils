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

package de.linusdev.lutils.html.impl.element;

import de.linusdev.lutils.html.*;
import de.linusdev.lutils.html.builder.HtmlElementBuilder;
import de.linusdev.lutils.html.impl.HtmlText;
import de.linusdev.lutils.html.impl.StandardHtmlAttribute;
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
import java.util.function.Function;

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

    @Override
    public @NotNull StandardHtmlElement copy() {
        List<@NotNull HtmlObject> content = new ArrayList<>(this.content.size());
        Map<String, HtmlAttribute> attributes = new HashMap<>(this.attributes.size());

        this.content.forEach(object -> content.add(object.copy()));
        this.attributes.forEach((key, attr) -> attributes.put(key, attr.copy()));
        
        return new StandardHtmlElement(tag, content, attributes);
    }

    @Override
    public void write(@NotNull HtmlWritingState state, @NotNull Writer writer) throws IOException {
        writer.append("<").append(tag.name());

        for (HtmlAttribute attr : attributes.values()) {
            writer.append(" ");
            attr.write(state, writer);
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

        /**
         * Same as {@link #newNormal(String)}, but with a custom builder supplied by {@code builder}.
         */
        public static <B extends StandardHtmlElement.Builder> @NotNull CustomType<B> newCustom(
                Function<@NotNull CustomType<B>, @NotNull B> builder,
                @NotNull String name
        ) {
            return new CustomType<>(builder, name, false, false);
        }

        /**
         * Same as {@link #newInline(String)}, but with a custom builder supplied by {@code builder}.
         */
        public static <B extends StandardHtmlElement.Builder> @NotNull CustomType<B> newCustomInline(
                Function<@NotNull CustomType<B>, @NotNull B> builder,
                @NotNull String name
        ) {
            return new CustomType<>(builder, name, true, false);
        }

        /**
         * Same as {@link #newVoid(String)}, but with a custom builder supplied by {@code builder}.
         */
        public static <B extends StandardHtmlElement.Builder> @NotNull CustomType<B> newCustomVoid(
                Function<@NotNull CustomType<B>, @NotNull B> builder,
                @NotNull String name
        ) {
            return new CustomType<>(builder, name, false, true);
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

        @Override
        public String toString() {
            return HtmlElementType.toString(this);
        }
    }

    public static class CustomType<B extends StandardHtmlElement.Builder> extends Type {

        protected final @NotNull Function<@NotNull CustomType<B>, @NotNull B> builder;

        public CustomType(@NotNull Function<@NotNull CustomType<B>, @NotNull B> builder, @NotNull String name, boolean inline, boolean voidElement) {
            super(name, inline, voidElement);
            this.builder = builder;
        }

        @Override
        public @NotNull B builder() {
            return builder.apply(this);
        }
    }

    public static class Builder implements HtmlElementBuilder {

        protected final @NotNull Type tag;
        protected final @NotNull List<@NotNull HtmlObject> content;
        protected final @NotNull Map<String, HtmlAttribute> attributes;

        public Builder(@NotNull Type tag) {
            this.tag = tag;
            this.content = new ArrayList<>();
            this.attributes = new HashMap<>();
        }

        public void addContent(@NotNull HtmlObject object) {
            object = onContentAdd(object);
            if(object != null)
                content.add(object);
        }

        public <B extends HtmlElementBuilder> void addElement(@NotNull HtmlElementType<B> type, Consumer<B> adjuster) {
            B builder = type.builder();
            adjuster.accept(builder);
            addContent(builder.build());
        }

        public void addAttribute(@NotNull HtmlAttributeType type, @Nullable String value) {
            HtmlAttribute attribute = onAttributeAdd(new StandardHtmlAttribute(type, value));
            if(attribute != null)
                attributes.put(attribute.type().name(), attribute);
        }

        public void addAttribute(@NotNull HtmlAttribute attribute) {
            attribute = onAttributeAdd(attribute);
            if(attribute != null)
                attributes.put(attribute.type().name(), attribute);
        }

        public void addText(@NotNull String text) {
            addContent(new HtmlText(text));
        }

        public @NotNull Map<String, HtmlAttribute> getCurrentAttributes() {
            return attributes;
        }

        protected @Nullable HtmlObject onContentAdd(@NotNull HtmlObject object) {
            return object;
        }

        protected @Nullable HtmlAttribute onAttributeAdd(@NotNull HtmlAttribute attribute) {
            return attribute;
        }

        @Override
        public @NotNull StandardHtmlElement build() {
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
            Builder builder = type.builder();

            char c = reader.read();
            if(c != '<')
                throw new ParseException(c);

            BiResult<String, Character> res = reader.readUntil(' ', '>');
            String tag = res.result1();

            if(tag.charAt(tag.length()-1) == '/' && res.result2() == '>') {
                if(!tag.substring(0, tag.length() - 1).equals(type.name()))
                    throw new ParseException("Illegal tag name '" + tag + "'.");

                // is immediately closed
                return builder.build();
            }

            if(!tag.equals(type.name()))
                throw new ParseException("Illegal tag name '" + tag + "'.");



            if(res.result2() == ' ') {
                // Read attributes
                AttributeReader attrReader = reader.getAttributeReader();

                while(attrReader.state != TAG_END) {
                    String name = attrReader.readAttributeName();

                    if(attrReader.state == TAG_SELF_CLOSE) {
                        if(name != null) {
                            HtmlAttributeType attrType = state.getRegistry().getAttributeTypeByName(name);
                            HtmlAttribute attribute = state.onAttributeParsed(attrType, null);
                            if(attribute != null)
                                builder.addAttribute(attrType, null);
                        }

                        return builder.build();
                    } else if(attrReader.state == ATTR_VALUE) {
                        String value = attrReader.readAttributeValue();
                        HtmlAttributeType attrType = state.getRegistry().getAttributeTypeByName(name);
                        HtmlAttribute attribute = state.onAttributeParsed(attrType, value);
                        if(attribute != null)
                            builder.addAttribute(attribute);
                    } else if(name != null) {
                        HtmlAttributeType attrType = state.getRegistry().getAttributeTypeByName(name);
                        HtmlAttribute attribute = state.onAttributeParsed(attrType, null);
                        if(attribute != null)
                            builder.addAttribute(attribute);
                    }
                }
            }

            if(type.isVoidElement()) {
                return builder.build();
            }

            // read content
            int id = state.onStartParsingContent(type, builder.getCurrentAttributes());

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
                    if(child != null) builder.addContent(child);
                } else {
                    reader.pushBack(c);
                    HtmlObject child = state.onObjectParsed(state.getParser().parse(state, reader));
                    if(child != null) builder.addContent(child);
                }
            }

            state.onEndParsingContent(id);
            tag = reader.readUntil('>').trim();

            if(!tag.equals(type.name()))
                throw new ParseException("Illegal end tag name '" + tag + "', should be '" + type.name() + "'.");

            return builder.build();
        }
    }

}
