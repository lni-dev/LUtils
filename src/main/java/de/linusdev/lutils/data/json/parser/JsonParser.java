/*
 * Copyright (c) 2022-2025 Linus Andera
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

package de.linusdev.lutils.data.json.parser;

import de.linusdev.lutils.collections.Entry;
import de.linusdev.lutils.data.Data;
import de.linusdev.lutils.data.DataBuilder;
import de.linusdev.lutils.data.Datable;
import de.linusdev.lutils.data.ParseType;
import de.linusdev.lutils.data.impl.DataWrapper;
import de.linusdev.lutils.data.json.Json;
import de.linusdev.lutils.data.json.JsonBuilder;
import de.linusdev.lutils.data.json.JsonMapImpl;
import de.linusdev.lutils.interfaces.Simplifiable;
import de.linusdev.lutils.interfaces.TBiConsumer;
import de.linusdev.lutils.other.parser.ParseException;
import de.linusdev.lutils.other.parser.ParseTracker;
import de.linusdev.lutils.other.parser.UnexpectedEndException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;


/**
 *
 * This class is used to parse {@link Data} to a json-string and json-string to {@link Json}.
 *
 * <p>
 *     Parse {@link Data} to json-string using {@link #writeData(Appendable, Data)}, {@link #writeDataToString(Data)}
 *     or {@link #writeDataToStringBuilder(Data)}.
 * </p>
 *
 * <p>
 *     Parse json-string to {@link Json} to using {@link #parseReader(Reader)}, {@link #parseStream(InputStream)}
 *     or {@link #parseString(String)}.
 * </p>
 *
 * <h3>{@link DataBuilder} to json-string can parse:</h3>
 * <ul>
 *     <li>
 *         {@link Boolean}, {@link Byte}, {@link Short}, {@link Integer}, {@link Long}, {@link Float}, {@link Double}, {@link String},
 *     </li>
 *     <li>
 *         {@link Datable}, {@link Simplifiable}
 *     </li>
 *     <li>
 *          any primitive type array
 *     </li>
 *     <li>
 *         arrays and {@link Collection collections} of all mentioned Classes
 *     </li>
 *     <li>
 *         {@link Map maps} with values of all mentioned Classes. The keys will be converted to a string
 *         using {@link Objects#toString(Object)}.
 *     </li>
 * </ul>
 *
 * <h3>Json-string to {@link Json} can parse:</h3>
 * <ul>
 *     <li style="padding-top:0">
 *         false/true to {@link Boolean} (ignores case)
 *     </li>
 *     <li>
 *         null to {@code null} (ignores case)
 *     </li>
 *     <li>
 *         Integer Numbers (1, 4, 5, ...) to {@link Long} (see {@link #setIdentifyNumberValues(boolean) identifyNumberValues})
 *     </li>
 *     <li>
 *         Decimal Numbers (5.6, ...) to {@link Double} (see {@link #setIdentifyNumberValues(boolean) identifyNumberValues})
 *     </li>
 *     <li>
 *         "strings" to {@link String}
 *     </li>
 *     <li>
 *         Arrays ([...]) to {@link List List&lt;Object&gt;} (see {@link #setListSupplier(Function) setListSupplier()})
 *     </li>
 *     <li>
 *         any other values are not supported and will most likely cause a {@link ParseException}
 *     </li>
 * </ul>
 *
 * <h3>When parsing json-string to {@link Json}:</h3>
 * <ul>
 *     <li style="padding-top:0">
 *         an empty json-string, for example "" or "   ", will be parsed to an empty {@link Json}
 *     </li>
 *     <li>
 *         a json-string that starts with a "[" (a json-array) will be parsed to an {@link Json}.
 *         The array will be accessible with the {@link #arrayWrapperKey}. see {@link #setArrayWrapperKey(String)}
 *         and {@link #DEFAULT_ARRAY_WRAPPER_KEY}
 *     </li>
 * </ul>
 */
@SuppressWarnings("unused")
public class JsonParser {

    public static final @NotNull JsonParser DEFAULT_INSTANCE = new JsonParser();

    public static final int CURLY_BRACKET_OPEN_CHAR      = '{';
    public static final int CURLY_BRACKET_CLOSE_CHAR     = '}';
    public static final int SQUARE_BRACKET_OPEN_CHAR     = '[';
    public static final int SQUARE_BRACKET_CLOSE_CHAR    = ']';
    public static final int QUOTE_CHAR = '\"';
    public static final int COLON_CHAR = ':';
    public static final int NEW_LINE_CHAR = '\n';
    public static final int COMMA_CHAR = ',';
    public static final int SLASH_CHAR = '/';
    public static final int ASTERISK_CHAR = '*';
    public static final int SPACE_CHAR = ' ';

    public static final String TRUE = "true";
    public static final String FALSE = "false";
    public static final String NULL = "null";

    public static final String DEFAULT_ARRAY_WRAPPER_KEY = "array";

    public static final char BYTE_TOKEN = 'B';
    public static final char SHORT_TOKEN = 'S';
    public static final char INTEGER_TOKEN = 'I';
    public static final char LONG_TOKEN = 'L';
    public static final char FLOAT_TOKEN = 'F';
    public static final char DOUBLE_TOKEN = 'D';

    //Configurable stuff
    private @NotNull String indent = "\t";

    private @NotNull Supplier<JsonBuilder> jsonBuilderSupplier = () -> new JsonMapImpl(new HashMap<>());
    private @NotNull Function<Integer, List<Object>> listSupplier = size -> size == null ? new LinkedList<>() : new ArrayList<>(size);

    private @NotNull String arrayWrapperKey = DEFAULT_ARRAY_WRAPPER_KEY;
    private boolean allowNewLineInStrings = true;
    private boolean identifyNumberValues = false;
    private boolean allowComments = false;
    private @Nullable TBiConsumer<@NotNull JsonParser, @NotNull String, ?> commentConsumer;

    /* ================================================================================================= *\
    |                                                                                                     |
    |                                             Config setter                                           |
    |                                                                                                     |
    \* ================================================================================================= */

    /**
     * What to use as indent.<br>
     * Default: {@code "\t"}
     * @param indent {@link String}
     */
    @Contract("_ -> this")
    public @NotNull JsonParser setIndent(@NotNull String indent) {
        this.indent = indent;
        return this;
    }

    /**
     * When this parser reads a json-object, this {@link Supplier} is used to create a new {@link Json} object.<br>
     * Default: {@code new JsonMapImpl(new HashMap<>())}
     * @param jsonBuilderSupplier {@link Supplier} to supply with {@link Json}
     */
    @Contract("_ -> this")
    public @NotNull JsonParser setJsonBuilderSupplier(@NotNull Supplier<JsonBuilder> jsonBuilderSupplier) {
        this.jsonBuilderSupplier = jsonBuilderSupplier;
        return this;
    }

    /**
     * When this parser reads a json-list, this {@link Supplier} is used to create a new {@link List} object.<br>
     * Default: {@code size -> size == null ? new LinkedList<>() : new ArrayList<>(size);}
     * @param listSupplier {@link Supplier} to supply with {@link List}
     */
    @Contract("_ -> this")
    public @NotNull JsonParser setListSupplier(@NotNull Function<Integer, List<Object>> listSupplier) {
        this.listSupplier = listSupplier;
        return this;
    }

    /**
     * If the json to read, does not start with a json-object, but instead with a json-array, the array will be available
     * with this key in the returned {@link Json}.<br>
     * Default: {@value #DEFAULT_ARRAY_WRAPPER_KEY}
     * @param arrayWrapperKey key to use when wrapping the array in a {@link Json}
     */
    @Contract("_ -> this")
    public @NotNull JsonParser setArrayWrapperKey(@NotNull String arrayWrapperKey) {
        this.arrayWrapperKey = arrayWrapperKey;
        return this;
    }

    /**
     * Default: {@code true}
     * @param allowNewLineInStrings whether to allow new lines in keys and string-values while reading
     */
    @Contract("_ -> this")
    public @NotNull JsonParser setAllowNewLineInStrings(boolean allowNewLineInStrings) {
        this.allowNewLineInStrings = allowNewLineInStrings;
        return this;
    }

    /**
     * If enabled it puts a single character (token) after a number, to identify which type of number it is. The tokens are the following: <br>
     * <ul>
     *     <li>Byte: B {@link #BYTE_TOKEN}</li>
     *     <li>Short: S {@link #SHORT_TOKEN}</li>
     *     <li>Integer: I {@link #INTEGER_TOKEN}</li>
     *     <li>Long: L {@link #LONG_TOKEN}</li>
     *     <li>Float: F {@link #FLOAT_TOKEN}</li>
     *     <li>Double: D {@link #DOUBLE_TOKEN}</li>
     * </ul>
     * Default: {@code false}
     * @param identifyNumberValues {@code true} to enable the feature described above
     */
    @Contract("_ -> this")
    public @NotNull JsonParser setIdentifyNumberValues(boolean identifyNumberValues) {
        this.identifyNumberValues = identifyNumberValues;
        return this;
    }

    /**
     * If enabled it will allow comments in the json. E.g.:
     * <pre>
     * // This is a single line comment
     * /* This is a
     *    multi line
     *    comment
     * *&#47;</pre>
     *
     * @param allowComments {@code true} to allow comments as desribed above
     */
    @Contract("_, _ -> this")
    public @NotNull JsonParser setAllowComments(
            boolean allowComments,
            @Nullable TBiConsumer<@NotNull JsonParser, @NotNull String, ?> commentConsumer
    ) {
        this.allowComments = allowComments;
        this.commentConsumer = commentConsumer;
        return this;
    }

    /* ================================================================================================= *\
    |                                                                                                     |
    |                                             Stream to Data                                          |
    |                                                                                                     |
    \* ================================================================================================= */

    /**
     * parses the content of given stream to a {@link Json}.<br>
     * The stream will be {@link InputStream#close() closed} after parsing finished.<br>
     * If only an empty string is being read (for example "" or "   "), an empty {@link Json} will be returned.<br>
     * @param stream the stream to read the json from
     * @return parsed {@link Json}
     * @throws IOException while parsing
     * @throws ParseException while parsing
     */
    public @NotNull Json parseStream(@NotNull InputStream stream) throws IOException, ParseException {
        JsonReader reader = new JsonReader(new BufferedReader(new InputStreamReader(stream)));

        try {
            return parse(reader, null);
        } finally {
            reader.close();
        }
    }

    /**
     * parses the content of given reader to a {@link Json}.<br>
     * The reader should not be wrapped in a {@link BufferedReader}, as this method does this.<br>
     * The reader will be {@link Reader#close() closed} after parsing finished.<br>
     * If only an empty string is being read (for example "" or "   "), an empty {@link Json} will be returned.<br>
     * @param reader the reader to read the json from
     * @return parsed {@link Json}
     * @throws IOException while parsing
     * @throws ParseException while parsing
     */
    public @NotNull Json parseReader(@NotNull Reader reader) throws IOException, ParseException {
        JsonReader jsonReader = new JsonReader(new BufferedReader(reader));

        try {
            return parse(jsonReader, null);
        } finally {
            jsonReader.close();
        }
    }

    /**
     * Creates a {@link StringReader} of given {@code json} and passes it to {@link #parse(JsonReader, ParseTracker)}.
     * @param json the json to parse
     * @return parsed {@link Json}
     * @throws IOException while parsing
     * @throws ParseException while parsing
     */
    public @NotNull Json parseString(@NotNull String json) throws IOException, ParseException {
        JsonReader jsonReader = new JsonReader(new StringReader(json));

        try {
            return parse(jsonReader, null);
        } finally {
            jsonReader.close();
        }
    }

    /**
     * <p>
     *     parses a json-object or a json-array (will be wrapped with {@link #arrayWrapperKey}) to a {@link Json}.
     *     <br><br>
     *     If only an empty string is being read (for example "" or "   ") and empty {@link Json} will be returned.
     * </p>
     *
     * @param reader to read from
     * @return parsed {@link Json}
     * @throws IOException while parsing
     * @throws ParseException while parsing
     */
    private @NotNull Json parse(@NotNull JsonReader reader, @Nullable ParseTracker tracker) throws IOException, ParseException {
        if(tracker == null)
            tracker = new ParseTracker();
        int i = reader.read(tracker);
        if(i == -1) return jsonBuilderSupplier.get().build();

        if(i == CURLY_BRACKET_OPEN_CHAR) {
            return parseJsonObject(reader, tracker);

        } else if (i == SQUARE_BRACKET_OPEN_CHAR) {
            JsonBuilder builder = jsonBuilderSupplier.get();
            builder.add(arrayWrapperKey, parseJsonArray(reader, tracker));
            return builder.build();

        } else if (allowComments && i == SLASH_CHAR) {
            parseComment(reader, tracker);
            return parse(reader, tracker);
        } else {
            throw new ParseException(tracker, (char) i);
        }

    }

    /**
     * Assumes that the starting {@value SLASH_CHAR} has already been read and {@link #allowComments}
     * is {@code true}.
     * @param reader to read from
     * @param tracker {@link ParseTracker}
     * @throws IOException while reading
     */
    private void parseComment(@NotNull JsonReader reader, @NotNull ParseTracker tracker) throws IOException, ParseException {
        assert allowComments;
        int i = reader.readNextChar(tracker);

        if(i == SLASH_CHAR) {
            // Read single line comment
            String comment = reader.readToEOL(tracker);
            if(commentConsumer != null) {
                try {
                    commentConsumer.consume(this, comment);
                } catch (Throwable e) {
                    throw new ParseException(tracker, e);
                }
            }
        } else if(i == ASTERISK_CHAR) {
            // Read multi line comment
            String comment = reader.readMultiLineComment(tracker);
            if(commentConsumer != null) {
                try {
                    commentConsumer.consume(this, comment);
                } catch (Throwable e) {
                    throw new ParseException(tracker, e);
                }
            }
        } else {
            throw new ParseException(tracker, (char) i);
        }
    }

    /**
     * If {@link #allowComments} is {@code false} returns given {@code i} immediately.
     * Checks if given {@code i} is a {@value SLASH_CHAR} and reads a comment or multiple
     * comments if it is.
     * @param i current char to check
     * @param reader to read from if {@code i} is the start of a comment
     * @param tracker {@link ParseTracker}
     * @return given char {@code i} or the next char after the comment(s)
     * @throws IOException while reading
     * @throws ParseException if the comment is malformed
     */
    private int parsePossibleComment(int i, @NotNull JsonReader reader, @NotNull ParseTracker tracker) throws IOException, ParseException {
        if(!allowComments)
            return i;
        while (i == SLASH_CHAR) {
            parseComment(reader, tracker);
            i = reader.read(tracker);
        }
        return i;
    }

    /**
     * Parses a json-object to a {@link Json}
     * @param reader to read from
     * @param tracker {@link ParseTracker}
     * @return parsed {@link Json}
     * @throws IOException while parsing
     * @throws ParseException while parsing
     */
    private @NotNull Json parseJsonObject(@NotNull JsonReader reader, @NotNull ParseTracker tracker) throws IOException, ParseException {
        int i = reader.read(tracker);
        JsonBuilder builder = jsonBuilderSupplier.get();

        if(i == CURLY_BRACKET_CLOSE_CHAR) return builder.build();

        while(i != -1){
            //inside the json-object, we first expect a key...
            if((i = parsePossibleComment(i, reader, tracker)) != QUOTE_CHAR) throw new ParseException(tracker, (char) i);

            String key = reader.readString(allowNewLineInStrings, tracker);

            //now we expect a colon (':')
            i = parsePossibleComment(reader.read(tracker), reader, tracker);
            if(i != COLON_CHAR) throw new ParseException(tracker, (char) i);

            reader.pushBack(parsePossibleComment(reader.read(tracker), reader, tracker));

            //now read the value for the key
            builder.add(key, parseJsonValue(reader, tracker));

            i = parsePossibleComment(reader.read(tracker), reader, tracker);
            //now expect a comma, or a '}'
            if(i == COMMA_CHAR) {
                i = reader.read(tracker);
                continue;
            }
            if(i == CURLY_BRACKET_CLOSE_CHAR) return builder.build();
            throw new ParseException(tracker, (char) i);
        }

        throw new UnexpectedEndException(tracker);
    }

    /**
     * Parses a json-value to {@link String}, {@link Json}, {@link List}, {@link Boolean}, {@link Number} or {@code null}
     * @param reader to read from
     * @param tracker {@link ParseTracker}
     * @return {@link String}, {@link Json}, {@link List}, {@link Boolean}, {@link Number} or {@code null}
     * @throws IOException while parsing
     * @throws ParseException while parsing
     */
    private @Nullable Object parseJsonValue(@NotNull JsonReader reader, @NotNull ParseTracker tracker) throws IOException, ParseException {
        int i = reader.read(tracker);

        if(i == QUOTE_CHAR) {
            //simple string
            return reader.readString(allowNewLineInStrings, tracker);

        } else if(i == CURLY_BRACKET_OPEN_CHAR) {
            //json-object
            return parseJsonObject(reader, tracker);

        } else if (i == SQUARE_BRACKET_OPEN_CHAR) {
            //json-array
            return parseJsonArray(reader, tracker);

        } else {
            //boolean or number
            reader.pushBack(i);
            return reader.readValue(tracker, identifyNumberValues);

        }
    }

    /**
     * Parses a json-array to a {@link List} of {@link Object}.<br>
     * The elements of the returned list are parsed with {@link #parseJsonValue(JsonReader, ParseTracker)}.
     * @param reader to read from
     * @param tracker {@link ParseTracker}
     * @return {@link List} of {@link Object}
     * @throws IOException while parsing
     * @throws ParseException while parsing
     */
    private @NotNull List<Object> parseJsonArray(@NotNull JsonReader reader, @NotNull ParseTracker tracker) throws IOException, ParseException {
        int i = 0;
        boolean valueParsed = false;
        List<Object> list =listSupplier.apply(null);

        while(i != -1){
            i = parsePossibleComment(reader.read(tracker), reader, tracker);
            if(i == SQUARE_BRACKET_CLOSE_CHAR) return list;
            if(valueParsed) {
                if(i == COMMA_CHAR) {
                    valueParsed = false;
                    continue;
                }

                // We needed a comma or a square bracket close!
                throw new ParseException(tracker, (char) i);
            }

            reader.pushBack(i);
            // Read item
            list.add(parseJsonValue(reader, tracker));
            valueParsed = true;
        }

        throw new UnexpectedEndException(tracker);
    }

    /* ================================================================================================= *\
    |                                                                                                     |
    |                                     Data to Json (Not String-Json)                                  |
    |                                                                                                     |
    \* ================================================================================================= */

    /**
     * This function converts an object as if it was converted during parsing: {@code parseString(writeDataToString(data))}.
     * {@link ParseType#CONTENT_ONLY CONTENT_ONLY}-{@link Data} is not supported except {@link DataWrapper}.
     * @param obj the object to convert
     * @return the converted object
     */
    @Contract("null -> null; !null -> !null")
    public @Nullable Object convertObjectToJsonValidObject(@Nullable Object obj) {
        if(obj == null)
            return null;

        if(obj instanceof Json json)
            return json;

        if(obj instanceof DataWrapper wrapper) {
            return convertObjectToJsonValidObject(wrapper.wrappedValue());
        }

        if(obj instanceof Datable datable)
            return convertDataToJson(datable.getData());

        if (obj instanceof Simplifiable simple)
            return simple.simplify();

        if (obj instanceof String || obj instanceof Boolean)
            return obj;

        if(obj instanceof Integer || obj instanceof Byte || obj instanceof Short) {
            if(identifyNumberValues) {
                return obj;
            } else {
                return ((Number) obj).longValue();
            }
        }

        if(obj instanceof Double || obj instanceof Float) {
            if(identifyNumberValues) {
                return obj;
            } else {
                return ((Number) obj).doubleValue();
            }
        }

        if (obj instanceof Collection<?> collection) {
            ArrayList<Object> list = new ArrayList<>(collection.size());

            for (Object o : collection)
                list.add(convertObjectToJsonValidObject(o));

            return list;
        }

        if (obj instanceof Map<?,?> map) {
            JsonBuilder jsonBuilder = jsonBuilderSupplier.get();
            for (Map.Entry<?, ?> entry : map.entrySet())
                jsonBuilder.add(Objects.toString(entry.getKey()), convertObjectToJsonValidObject(entry.getValue()));
        }

        if (obj instanceof Object[])
            return obj;

        if (obj.getClass().isArray()) {
            if (obj instanceof byte[] a) {
                List<Object> list = listSupplier.apply(a.length);
                for (int i = 0; i < a.length; i++) list.set(i, a[i]) ;
                return list;

            } else if (obj instanceof short[] a) {
                List<Object> list = listSupplier.apply(a.length);
                for (int i = 0; i < a.length; i++) list.set(i, a[i]) ;
                return list;

            } else if (obj instanceof int[] a) {
                List<Object> list = listSupplier.apply(a.length);
                for (int i = 0; i < a.length; i++) list.set(i, a[i]) ;
                return list;

            } else if (obj instanceof long[] a) {
                List<Object> list = listSupplier.apply(a.length);
                for (int i = 0; i < a.length; i++) list.set(i, a[i]) ;
                return list;

            } else if (obj instanceof float[] a) {
                List<Object> list = listSupplier.apply(a.length);
                for (int i = 0; i < a.length; i++) list.set(i, a[i]) ;
                return list;

            } else if (obj instanceof double[] a) {
                List<Object> list = listSupplier.apply(a.length);
                for (int i = 0; i < a.length; i++) list.set(i, a[i]) ;
                return list;
            }
        }

        return obj.toString();
    }

    /**
     * Convert a {@link Data} to {@link Json}. This method behaves exactly as {@code parseString(writeDataToString(data))}.
     * <br>
     * Limitations: {@link ParseType#CONTENT_ONLY CONTENT_ONLY}-{@link Data} is not supported.
     * @param data {@link Data} to convert.
     * @return {@link Json} as described above.
     */
    @Contract("null -> null; !null -> !null")
    public @Nullable Json convertDataToJson(@Nullable Data data) {
        if(data == null)
            return null;

        if(data instanceof Json json)
            return json;

        JsonBuilder jsonBuilder = jsonBuilderSupplier.get();

        if(data.parseType() == ParseType.NORMAL) {
            for (Entry<String, Object> entry : data) {
                String key = entry.getKey();
                Object value = entry.getValue();

                jsonBuilder.add(key, convertObjectToJsonValidObject(value));
            }

        } else if (data.parseType() == ParseType.CONTENT_ONLY) {
            throw new IllegalStateException("Cannot convert content only data.");
        }

        return jsonBuilder.build();
    }

    /* ================================================================================================= *\
    |                                                                                                     |
    |                                             Data to String                                          |
    |                                                                                                     |
    \* ================================================================================================= */


    /**
     *
     * @param data {@link Data} to write to a {@link StringBuffer}
     * @return {@link StringBuffer#toString()}
     */
    public @NotNull String writeDataToString(@Nullable Data data) {
        return writeDataToStringBuilder(data).toString();
    }

    /**
     *
     * @param data {@link Data} to write to a {@link StringBuffer}
     * @return {@link StringBuffer}
     */
    public @NotNull StringBuilder writeDataToStringBuilder(@Nullable Data data){
        StringBuilder writer = new StringBuilder(data == null ? 10 : data.size() * 10);
        try {
            writeData(writer, data);
        } catch (IOException _ignored) {
            //noinspection CallToPrintStackTrace will never happen, because StringBuilder, does not throw this exception, but let's print it anyway.
            _ignored.printStackTrace();
        }
        return writer;
    }

    /**
     *
     * @param data {@link Data} to write. {@code null} will write an empty Data: "{}"
     * @param writer {@link Writer} to write to
     * @throws IOException {@link IOException} while writing
     */
    public void writeData(@NotNull Appendable writer, @Nullable Data data) throws IOException {
        SpaceOffsetTracker offset = new SpaceOffsetTracker(indent);
        writeJson(writer, offset, data);
    }

    private void writeJson(
            @NotNull Appendable writer,
            @NotNull SpaceOffsetTracker offset,
            @Nullable Data data
    ) throws IOException {
        if (data == null) data = DataBuilder.empty();
        if(data.parseType() == ParseType.NORMAL) {
            writer.append((char) CURLY_BRACKET_OPEN_CHAR);
            offset.add();

            boolean first = true;
            for (Entry<?, ?> entry : data) {
                if (!first) writer.append((char) COMMA_CHAR);
                else first = false;

                writer.append((char) NEW_LINE_CHAR).append(offset.toString());
                writeKey(writer, entry.getKey());
                writeJsonValue(writer, offset, entry.getValue());

            }

            writer.append((char) NEW_LINE_CHAR);
            offset.remove();
            writer.append(offset.toString()).append((char) CURLY_BRACKET_CLOSE_CHAR);

        } else if (data.parseType() == ParseType.CONTENT_ONLY){

            boolean first = true;
            for (Entry<?, ?> entry : data) {
                if (!first) writer.append((char) COMMA_CHAR).append((char) SPACE_CHAR);
                else first = false;

                writer.append(offset.toString());
                writeJsonValue(writer, offset, entry.getValue());

            }

        }
    }

    private void writeKey(@NotNull Appendable writer, @NotNull Object key) throws IOException {
        writer.append((char) QUOTE_CHAR);
        ParseHelper.escape2(Objects.toString(key), writer);
        writer.append((char) QUOTE_CHAR).append((char) COLON_CHAR).append((char) SPACE_CHAR);
    }

    private void writeJsonValue(@NotNull Appendable writer, @NotNull SpaceOffsetTracker offset, @Nullable Object value) throws IOException {
        if (value == null) {
            writer.append(NULL);

        } else if (value instanceof Datable) {
            writeJson(writer, offset, ((Datable) value).getData());

        } else if (value instanceof Simplifiable) {
            writeJsonValue(writer, offset, ((Simplifiable) value).simplify());

        } else if (value instanceof String) {
            writer.append((char) QUOTE_CHAR);
            ParseHelper.escape2((String) value, writer);
            writer.append((char) QUOTE_CHAR);

        } else if (value instanceof Boolean bool) {
            writer.append(bool ? TRUE : FALSE);

        } else if (value instanceof Integer) {
            writer.append(value.toString());
            if (identifyNumberValues) writer.append(INTEGER_TOKEN);

        } else if (value instanceof Long) {
            writer.append(value.toString());
            if (identifyNumberValues) writer.append(LONG_TOKEN);

        } else if (value instanceof Byte) {
            writer.append(value.toString());
            if (identifyNumberValues) writer.append(BYTE_TOKEN);

        } else if (value instanceof Short) {
            writer.append(value.toString());
            if (identifyNumberValues) writer.append(SHORT_TOKEN);

        } else if (value instanceof Double) {
            writer.append(value.toString());
            if (identifyNumberValues) writer.append(DOUBLE_TOKEN);

        } else if (value instanceof Float) {
            writer.append(value.toString());
            if (identifyNumberValues) writer.append(FLOAT_TOKEN);

        } else if (value instanceof Collection) {
            writer.append((char) SQUARE_BRACKET_OPEN_CHAR).append((char) NEW_LINE_CHAR);
            offset.add();

            boolean first = true;
            for (Object o : (Collection<?>) value) {
                if (!first) writer.append((char) COMMA_CHAR).append((char) NEW_LINE_CHAR);
                else first = false;
                writer.append(offset.toString());
                writeJsonValue(writer, offset, o);
            }

            writer.append((char) NEW_LINE_CHAR);
            offset.remove();
            writer.append(offset.toString()).append((char) SQUARE_BRACKET_CLOSE_CHAR);

        } else if (value instanceof Map<?,?> map) {
            writer.append((char) CURLY_BRACKET_OPEN_CHAR).append((char) NEW_LINE_CHAR);
            offset.add();

            boolean first = true;
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                if (!first) writer.append((char) COMMA_CHAR).append((char) NEW_LINE_CHAR);
                else first = false;
                writer.append(offset.toString());
                writeKey(writer, entry.getKey());
                writeJsonValue(writer, offset, entry.getValue());
            }

            writer.append((char) NEW_LINE_CHAR);
            offset.remove();
            writer.append(offset.toString()).append((char) CURLY_BRACKET_CLOSE_CHAR);

        } else if (value instanceof Object[]) {
            writeJsonValue(writer, offset, (Object[]) value);

        } else if (value.getClass().isArray()) {
            if (value instanceof byte[] a) {
                Object[] o = new Object[a.length];
                for (int i = 0; i < a.length; i++) o[i] = a[i];
                writeJsonValue(writer, offset, o);

            } else if (value instanceof short[] a) {
                Object[] o = new Object[a.length];
                for (int i = 0; i < a.length; i++) o[i] = a[i];
                writeJsonValue(writer, offset, o);

            } else if (value instanceof int[] a) {
                Object[] o = new Object[a.length];
                for (int i = 0; i < a.length; i++) o[i] = a[i];
                writeJsonValue(writer, offset, o);

            } else if (value instanceof long[] a) {
                Object[] o = new Object[a.length];
                for (int i = 0; i < a.length; i++) o[i] = a[i];
                writeJsonValue(writer, offset, o);

            } else if (value instanceof float[] a) {
                Object[] o = new Object[a.length];
                for (int i = 0; i < a.length; i++) o[i] = a[i];
                writeJsonValue(writer, offset, o);

            } else if (value instanceof double[] a) {
                Object[] o = new Object[a.length];
                for (int i = 0; i < a.length; i++) o[i] = a[i];
                writeJsonValue(writer, offset, o);

            }

        } else {
            //If the Object is none of the above, a simple string is added instead
            writer.append((char) QUOTE_CHAR);
            ParseHelper.escape2(value.toString(), writer);
            writer.append((char) QUOTE_CHAR);
        }
    }

    private void writeJsonValue(
            @NotNull Appendable writer,
            @NotNull SpaceOffsetTracker offset,
            @NotNull Object[] value
    ) throws IOException {
        writer.append((char) SQUARE_BRACKET_OPEN_CHAR).append((char) NEW_LINE_CHAR);
        offset.add();

        boolean first = true;
        for (Object o : value) {
            if (!first) writer.append((char) COMMA_CHAR).append((char) NEW_LINE_CHAR);
            else first = false;
            writer.append(offset.toString());
            writeJsonValue(writer, offset, o);
        }

        writer.append((char) NEW_LINE_CHAR);
        offset.remove();
        writer.append(offset.toString()).append((char) SQUARE_BRACKET_CLOSE_CHAR);
    }

}
