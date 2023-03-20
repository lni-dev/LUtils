/*
 * Copyright (c) 2023 Linus Andera all rights reserved
 */

package de.linusdev.lutils.http;

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

    private int nextByte = -1;

    public HTTPRequestReader(@NotNull CharsetDecoder decoder, @NotNull InputStream stream) {
        this.decoder = decoder;
        this.stream = stream;
        this.bufBytes = ByteBuffer.allocate(1024);
        this.bufChars = CharBuffer.allocate(1024);
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
            bufBytes.put((byte) nextByte);
            bufBytes.limit(1);

            int byteCount = stream.read(bufBytes.array());

            if(byteCount != -1) {
                nextByte = stream.read();
                bufBytes.limit(byteCount + 1);
            } else {
                nextByte = -1;
            }

            return bufBytes.remaining();
        }

        return 0;
    }

    public void decodeChars() {
        CoderResult result = decoder.decode(bufBytes, bufChars, nextByte == -1);

        result.

    }


    public String readLine() throws IOException {
        StringBuilder str = new StringBuilder();

        boolean pcr = false;
        int r;
        char c;
        while((r = read()) != -1) {
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
}
