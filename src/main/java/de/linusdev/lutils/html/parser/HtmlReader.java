/*
 * Copyright (c) 2024-2025 Linus Andera
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

import de.linusdev.lutils.html.HtmlUtils;
import de.linusdev.lutils.other.parser.ParseTracker;
import de.linusdev.lutils.result.BiResult;
import org.jetbrains.annotations.NotNull;

import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;

public class HtmlReader {

    private final @NotNull Reader reader;

    private final char[] pushBackBuffer = new char[1024];
    private int pushBackIndex = 0;

    private final @NotNull ParseTracker tracker = new ParseTracker();

    public HtmlReader(@NotNull Reader reader) {
        this.reader = reader;
    }

    /**
     * Check if {@link #read()} can be called.
     * @return {@code true} if {@link #read()} can be called without throwing an exception.
     * @throws IOException while reading.
     */
    public boolean canRead() throws IOException {
        if(pushBackIndex != 0) return true;
        int r = reader.read();
        if(r == -1) return false;
        if(r == '\n') tracker.nextLine();
        pushBack((char) r);
        return true;
    }

    /**
     *
     * @return the next char which was read. May read from the pushback-buffer.
     * @throws IOException while reading.
     * @throws EOFException if the end of the stream has been reached.
     */
    public char read() throws IOException {
        if(pushBackIndex != 0)
            return pushBackBuffer[--pushBackIndex];

        int r = reader.read();
        if(r == -1) throw new EOFException();
        if(r == '\n') tracker.nextLine();

        return (char) r;
    }

    /**
     * Reads until a char which is not a new line or a space.
     * @return the next char returned from {@link #read()}, which is nota new line or a space.
     * @throws IOException see {@link #read()}.
     */
    public char skipNewLinesAndSpaces() throws IOException {
        char r;
        //noinspection StatementWithEmptyBody
        while ((r = read()) == '\n' || r == ' ') {}
        return r;
    }

    public char skipNewLinesAndFollowingSpaces() throws IOException {
        char r = read();

        if(r == '\n') {
            //noinspection StatementWithEmptyBody
            while ((r = read()) == '\n') { }

            if(r == ' ') //noinspection StatementWithEmptyBody
                while ((r = read()) == ' ') { }

            if(r == '\n')
                return skipNewLinesAndFollowingSpaces();
        }

        return r;
    }

    /**
     * Reads until the next {@link #read()} would return a char which is not a new line or a space.
     * This method uses {@link #canRead()} before reading, to avoid throwing an {@link EOFException}.
     * @return {@code true} if {@link #read()} can be called without throwing an exception
     * @throws IOException while reading, but {@link EOFException} cannot be thrown.
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean availableSkipNewLinesAndSpaces() throws IOException {
        if(!canRead())
            return false;
        char r = 0;
        //noinspection StatementWithEmptyBody
        while (canRead() && ((r = read()) == '\n' || r == ' ')) {}

        if(r == '\n' || r == ' ')
            return false;

        pushBack(r);
        return true;
    }

    /**
     * Read until a char matching any of {@code c1}, {@code c2}, ... is read. Then returns
     * the read string without the last char. If the return type is a {@link BiResult}, the first
     * result will be the read string without the last char. The second result will be the last char.
     * @return as described above.
     * @throws IOException while reading
     */
    public @NotNull String readUntil(char c) throws IOException {
        StringBuilder sb = new StringBuilder();
        char r;
        while ((r = read()) != c) {
            sb.append(r);
        }

        return sb.toString();
    }

    /**
     * {@link #readUntil(char)} using an {@link HtmlUtils.Unescaper}.
     * @see #readUntil(char)
     */
    public @NotNull String readEscapedUntil(char c) throws IOException {
        HtmlUtils.Unescaper unescaper = new HtmlUtils.Unescaper();
        char r;
        while ((r = read()) != c) {
            unescaper.append(r);
        }

        return unescaper.getString();
    }

    /**
     * {@link #readUntil(char)} using an {@link HtmlUtils.Unescaper}.
     * @see #readUntil(char)
     */
    public @NotNull BiResult<String, Character> readEscapedUntil(char c1, char c2) throws IOException {
        HtmlUtils.Unescaper unescaper = new HtmlUtils.Unescaper();
        char r;
        while ((r = read()) != c1 && r != c2) {
            unescaper.append(r);
        }

        return new BiResult<>(unescaper.getString(), r) ;
    }

    /**
     * @see #readUntil(char)
     */
    public @NotNull BiResult<String, Character> readUntil(char c1, char c2) throws IOException {
        StringBuilder sb = new StringBuilder();
        char r;
        while ((r = read()) != c1 && r != c2) {
            sb.append(r);
        }

        return new BiResult<>(sb.toString(), r);
    }

    /**
     * @see #readUntil(char)
     */
    @SuppressWarnings("unused")
    public @NotNull BiResult<String, Character> readUntil(char c1, char c2, char c3) throws IOException {
        StringBuilder sb = new StringBuilder();
        char r;
        while ((r = read()) != c1 && r != c2 && r != c3) {
            sb.append(r);
        }

        return new BiResult<>(sb.toString(), r);
    }

    /**
     * @see #readUntil(char)
     */
    public @NotNull BiResult<String, Character> readUntil(char c1, char c2, char c3, char c4) throws IOException {
        StringBuilder sb = new StringBuilder();
        char r;
        while ((r = read()) != c1 && r != c2 && r != c3 && r != c4) {
            sb.append(r);
        }

        return new BiResult<>(sb.toString(), r);
    }

    /**
     * Skip {@code n} characters. If {@code n} chars cannot be skipped, because the stream ended, a {@link EOFException} will be thrown.
     * This method will not update the {@link #tracker}!
     * @param n amount of chars to skip.
     * @throws IOException while skipping.
     */
    public void skip(int n) throws IOException {
        if(pushBackIndex != 0) {
            if(pushBackIndex >= n) {
                pushBackIndex = pushBackIndex - n;
            } else /*if (pushBackIndex < n)*/ {
                n -= pushBackIndex;
                pushBackIndex = 0;
                skip(n);
            }
            return;
        }

        if(reader.skip(n) != n)
            throw new EOFException();
    }

    /**
     * Pushback given char {@code c}. A {@link #read()} right after this method call
     * will return {@code c}.
     * @param c char to pushback
     */
    public void pushBack(char c) {
        pushBackBuffer[pushBackIndex++] = c;
        if(c == '\n') tracker.previousLine();
    }

    /**
     * Will {@link #pushBack(char)} given char array in reverse order.
     * @param c array to pushback
     * @param len array length
     */
    public void pushBack(char[] c, int len) {
        for (int i = len - 1 ; i >= 0; i--) {
            if(c[i] == '\n') tracker.previousLine();
            pushBackBuffer[pushBackIndex++] = c[i];
        }
    }

    /**
     * Will {@link #pushBack(char)} given string in reverse order.
     * @param string string to pushback
     */
    public void pushBack(@NotNull String string) {
        for (int i = string.length() - 1; i >= 0; i--) {
            char c = string.charAt(i);
            if(c == '\n') tracker.previousLine();
            pushBackBuffer[pushBackIndex++] = c;
        }
    }

    /**
     * Read a string. e.g.: Reading {@code "test"}, {@code 'test'} or {@code test} will all return the string {@code test}.
     * This method will automatically convert escaped sequences back to the original characters.
     * @return the string read.
     * @throws IOException while reading.
     */
    public String readString() throws IOException {
        char r = read();

        if(r == '"') {
            return readEscapedUntil('"');
        } else if (r == '\'') {
            return readEscapedUntil('\'');
        } else {
            BiResult<String, Character> res = readEscapedUntil(' ', '>');
            pushBack(res.result2());
            return res.result1();
        }
    }

    /**
     * Get a new {@link AttributeReader}.
     */
    public @NotNull AttributeReader getAttributeReader(@NotNull HtmlParserState state) {
        return new AttributeReader(this, state);
    }

    /**
     * The tracker that tracks the reading line.
     */
    public @NotNull ParseTracker getTracker() {
        return tracker;
    }

    /**
     * Print the remaining readable content that fits in the {@link #pushBackBuffer} and
     * prints it to {@link System#out}.
     */
    @SuppressWarnings("unused") // debug method only
    public void debug() throws IOException {
        StringBuilder sb = new StringBuilder();
        char r;
        int i = 0;
        while (canRead() && pushBackIndex+i < pushBackBuffer.length) {
            r = read();
            i++;
            sb.append(r);
        }

        pushBack(sb.toString());
        System.out.println("Remaining:");
        for (String s : sb.toString().split("\n")) {
            System.out.println(s);
        }

    }

}
