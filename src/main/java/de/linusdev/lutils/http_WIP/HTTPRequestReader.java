/*
 * Copyright (c) 2023 Linus Andera
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

package de.linusdev.lutils.http_WIP;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;

public class HTTPRequestReader {

    private final @NotNull CharsetDecoder decoder;
    private final @NotNull InputStream stream;
    
    private final ByteBuffer bufBytes;
    private final CharBuffer bufChars;

    private int nextByte = -2;

    public HTTPRequestReader(@NotNull CharsetDecoder decoder, @NotNull InputStream stream) {
        this.decoder = decoder;
        this.stream = stream;
        this.bufBytes = ByteBuffer.allocate(1024);
        this.bufChars = CharBuffer.allocate(1024);
        bufBytes.limit(0);
        bufChars.limit(0);
    }

    public HTTPRequestReader(@NotNull InputStream stream) {
        this(StandardCharsets.UTF_8.newDecoder()
                .onMalformedInput(CodingErrorAction.REPLACE)
                .onUnmappableCharacter(CodingErrorAction.REPLACE), stream);
    }

    public int readByte() throws IOException {

        if(bufBytes.remaining() == 0) {
            if(nextByte == -1 || readBytesToBuffer() == 0) return -1;
        }

        return bufBytes.get();
    }

    private int readBytesToBuffer() throws IOException {

        if(nextByte != -1) {
            bufBytes.position(0);
            if(nextByte > 0) {
                bufBytes.put((byte) nextByte);
                bufBytes.limit(1);
            } else {
                bufBytes.limit(0);
            }


            int byteCount = stream.read(bufBytes.array());

            if(byteCount != -1) {
                nextByte = stream.read();
                bufBytes.limit(byteCount + bufBytes.limit());
            } else {
                nextByte = -1;
            }

            return bufBytes.remaining();
        }

        return 0;
    }

    public void decodeChars() {

        bufChars.position(0);
        bufChars.limit(bufChars.capacity());
        int oldPos = bufBytes.position();
        CoderResult result = decoder.decode(bufBytes, bufChars, nextByte == -1);
        int read = bufBytes.position() - oldPos;
        bufBytes.position(oldPos);
        bufChars.limit(read);

        if(result.isUnderflow()){
            return;
        } else if(result.isOverflow()) {
            //TODO: does this happen
            throw new RuntimeException("yes");
        }

    }

    public int readChar() throws IOException {
        if(bufChars.remaining() == 0) {
            if(nextByte == -1 || readBytesToBuffer() == 0) return -1;
            else {
                decodeChars();
                if(bufChars.remaining() == 0) return -1;
            }
        }

        bufBytes.position(bufBytes.position() + 1);
        return bufChars.get();
    }


    public String readLine() throws IOException {
        StringBuilder str = new StringBuilder();

        boolean pcr = false;
        int r;
        char c;
        while((r = readChar()) != -1) {
            c = (char) r;

            if(pcr && c == '\n') {
                return str.toString();
            } else if (pcr) {
                pcr = false;
                str.append('\r');
            }

            if(c == '\r') pcr = true;
            else str.append(c);
        }

        if(pcr) str.append('\r');
        return str.toString();
    }

    public @NotNull InputStream getInputStreamForRemaining() {

        HTTPRequestReader this_ = this;

        return new InputStream() {
            @Override
            public int read() throws IOException {
                return this_.readByte();
            }
        };
    }
}
