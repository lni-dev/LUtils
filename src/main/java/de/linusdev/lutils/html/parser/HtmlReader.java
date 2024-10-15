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

import de.linusdev.lutils.result.BiResult;
import org.jetbrains.annotations.NotNull;

import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;

public class HtmlReader {

    private final @NotNull Reader reader;

    private final char[] pushBackBuffer = new char[1024];
    private int pushBackIndex = 0;


    public HtmlReader(@NotNull Reader reader) {
        this.reader = reader;
    }

    public boolean canRead() throws IOException {
        if(pushBackIndex != 0) return true;
        int r = reader.read();
        if(r == -1) return false;
        pushBack((char) r);
        return true;
    }

    public char read() throws IOException {
        if(pushBackIndex != 0)
            return pushBackBuffer[--pushBackIndex];

        int r = reader.read();
        if(r == -1) throw new EOFException();

        return (char) r;
    }

    public char skipNewLines() throws IOException {
        char r;
        //noinspection StatementWithEmptyBody
        while ((r = read()) == '\n') {}
        return r;
    }

    public @NotNull String readUntil(char c, boolean allowEOF) throws IOException {
        StringBuilder sb = new StringBuilder();
        char r = 0;
        while (canRead() && (r = read()) != c) {
            sb.append(r);
        }

        if (r != c && !allowEOF)
            throw new EOFException();

        return sb.toString();
    }

    public @NotNull String readEscapedUntil(char c, boolean allowEOF) throws IOException {
        StringBuilder sb = new StringBuilder();
        char r = 0;
        while (canRead() && (r = read()) != c) {
            sb.append(r);
            //TODO: escaping!!!
        }

        if (r != c && !allowEOF)
            throw new EOFException();

        return sb.toString();
    }

    public @NotNull BiResult<String, Character> readUntil(char c1, char c2, boolean allowEOF) throws IOException {
        StringBuilder sb = new StringBuilder();
        char r = 0;
        while (canRead() && (r = read()) != c1 && r != c2) {
            sb.append(r);
        }

        if (r != c1 && r != c2 && !allowEOF)
            throw new EOFException();

        return new BiResult<>(sb.toString(), r);
    }

    public @NotNull BiResult<String, Character> readUntil(char c1, char c2, char c3, boolean allowEOF) throws IOException {
        StringBuilder sb = new StringBuilder();
        char r = 0;
        while (canRead() && (r = read()) != c1 && r != c2 && r != c3) {
            sb.append(r);
        }

        if (r != c1 && r != c2 && r != c3 && !allowEOF)
            throw new EOFException();

        return new BiResult<>(sb.toString(), r);
    }

    public @NotNull BiResult<String, Character> readUntil(char c1, char c2, char c3, char c4, boolean allowEOF) throws IOException {
        StringBuilder sb = new StringBuilder();
        char r = 0;
        while (canRead() && (r = read()) != c1 && r != c2 && r != c3 && r != c4) {
            sb.append(r);
        }

        if (r != c1 && r != c2 && r != c3 && r != c4 && !allowEOF)
            throw new EOFException();

        return new BiResult<>(sb.toString(), r);
    }


    public void skip(int n) throws IOException {
        if(pushBackIndex != 0) {
            if(pushBackIndex >= n) {
                pushBackIndex = pushBackIndex - n;
            } else if (pushBackIndex < n) {
                n -= pushBackIndex;
                pushBackIndex = 0;
                skip(n);
            }
            return;
        }

        if(reader.skip(n) != n)
            throw new EOFException();
    }

    public void pushBack(char c) {
        pushBackBuffer[pushBackIndex++] = c;
    }

    public void pushBack(char[] c, int offset, int len) {
        len += offset;
        for (int i = offset; i < len; i++) {
            pushBackBuffer[pushBackIndex++] = c[i];
        }
    }

    public String readString() throws IOException {
        char r = read();

        if(r == '"') {
            return readEscapedUntil('"', false);
        } else if (r == '\'') {
            return readEscapedUntil('\'', false);
        }

        throw new IOException("Illegal char '" + r + "'.");
    }

    public AttributeReader getAttributeReader() {
        return new AttributeReader(this);
    }


}
