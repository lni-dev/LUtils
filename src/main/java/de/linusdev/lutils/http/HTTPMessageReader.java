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

public class HTTPMessageReader implements AutoCloseable {

    private final @NotNull InputStream in;

    private int position;
    /**
     * <b>exclusive</b> limit to which {@link #buffer} is filled.
     */
    private int limit;
    private final byte[] buffer;
    private final ByteBuffer bufferObject;

    private final CharsetDecoder decoder;
    @SuppressWarnings("FieldCanBeLocal")
    private final char[] charBuffer;
    private final CharBuffer charBufferObject;
    /**
     * This is a view on {@link #charBufferObject} but with the capacity being 1 smaller.
     * That is so, we can safely flush the {@link #decoder} into {@link #charBufferObject} after decoding into {@link #decodeCharBufferObject}
     */
    @SuppressWarnings("JavadocDeclaration")
    private final CharBuffer decodeCharBufferObject;

    private final @NotNull CharRet charRet = new CharRet();
    private final @NotNull LineReader lineReader = new LineReader();

    public HTTPMessageReader(@NotNull InputStream in) {
        this.in = in;
        this.buffer = new byte[2048];
        this.bufferObject = ByteBuffer.wrap(buffer);

        this.decoder = StandardCharsets.UTF_8
                .newDecoder()
                .onMalformedInput(CodingErrorAction.REPORT)
                .onUnmappableCharacter(CodingErrorAction.REPORT);
        this.charBuffer = new char[512];
        this.charBufferObject = CharBuffer.wrap(charBuffer);
        this.decodeCharBufferObject = charBufferObject.subSequence(0, charBufferObject.capacity() - 1);

        this.charBufferObject.limit(0);
    }

    private void readToBuffer() throws IOException {
        position = 0;
        limit = in.read(buffer);
    }

    /**
     * untested!
     * @return next byte or -1 if no more bytes can be read
     * @throws IOException if occurred while reading
     */
    @SuppressWarnings("unused")
    public int read() throws IOException {
        if(position >= limit) readToBuffer();
        if(limit < 0) return -1;

        return Byte.toUnsignedInt(buffer[position++]);
    }

    /**
     *
     * @return {@code false} if nothing was read.
     */
    private boolean readToCharBuffer() throws IOException {
        if(position >= limit) readToBuffer();
        if(limit < 0) return false;

        // set input buffer limitations
        bufferObject.position(position);
        bufferObject.limit(limit);

        // clear char buffer and decode char buffer
        charBufferObject.clear();
        decodeCharBufferObject.clear();

        CoderResult result;
        do {
            // decode
            result = decoder.decode(bufferObject, decodeCharBufferObject, limit != buffer.length);

            // Check result

            // In case of a malformed input or an unmappable character, move the buffer as far as required.
            // add correction char(s) to the charBufferObject
            if(result.isMalformed() || result.isUnmappable()) {
                bufferObject.position(bufferObject.position() + result.length());
                if(result.length() == 1) charBufferObject.append((char) 0x003F);
                else if(result.length() == 2) charBufferObject.append((char) 0x003F).append((char) 0x003F);
                else if(result.length() == 3) charBufferObject.append((char) 0xFFFD);
                else /* result.length() == 4 */ charBufferObject.append((char) 0xFFFD).append((char) 0x003F);
            }


        } while (result.isError()); //if there was an error, decode the remaining input

        // Copy position and limit from the decode-char-buffer to the read-char-buffer
        charBufferObject.position(decodeCharBufferObject.position());
        charBufferObject.limit(decodeCharBufferObject.limit());

        // if end of input has been reached, make sure we read everything
        if(limit != buffer.length) {
            // Read the last char, in case the previous one was a high surrogate.
            charBufferObject.limit(charBufferObject.capacity());
            if(decoder.flush(charBufferObject).isOverflow())
                throw new InternalError("Decoder.flush() is trying to write more than 1 char!");
        }

        // flip to be able to read the buffer
        charBufferObject.flip();

        return true;
    }

    public static class CharRet {
        public boolean eof = false;
        public char character = 0;
        public boolean highSurrogate = false;

        public CharRet eof() {
            eof = true;
            return this;
        }
    }

    @SuppressWarnings("UnusedReturnValue")
    public @NotNull CharRet readChar() throws IOException {
        if(charBufferObject.remaining() == 0) {
            var a = readToCharBuffer();

            if(!a) {
                return charRet.eof();
            }
        }

        charRet.character = charBufferObject.get();

        // Move position of byte buffer according to the amount of bytes this utf-16 char would represent in utf-8
        // Set charRet.highSurrogate if the char is a high Surrogate (char requires 2 16-bit chars)
        if(charRet.highSurrogate) charRet.highSurrogate = false;
        else if(charRet.character < 0x0080) position += 1;
        else if(charRet.character < 0x0800) position += 2;
        else if(Character.isHighSurrogate(charRet.character)) {
            charRet.highSurrogate = true;
            position += 4;
        }
        else position += 3;

        return charRet;
    }

    /**
     * Reader that can read lines.
     * <br><br>
     *
     * If the line end is reached, the flag {@link #eol} will be {@code true}.<br>
     * If the file end is reached, the flag {@link #eof} will be {@code true}.
     */
    public class LineReader {
        /**
         * Indicates that no more can be read. Further read operations will return empty strings.
         */
        public boolean eof = false;

        /**
         * Indicates that the end of the line has been reached. Further read operations will read the next line.
         */
        public boolean eol = false;

        private void updateState(@NotNull StringBuilder sb) {
            // Update Class State
            this.eof = charRet.eof;
            this.eol = charRet.character == '\n';

            // Remove last char(s)
            sb.setLength(
                    sb.length() -
                            (eol && sb.length() >= 2 && sb.charAt(sb.length() - 2) == '\r' ? 2 : 1)
            );
        }

        /**
         * Reads until the first occurrence of the character {@code c}, the end of the line or the end of the file.
         * This method will set the {@link #eol} and {@link #eof} flags accordingly.
         * @param c the character to which to read to
         * @return the String read, not containing {@code c} or {@code \r\n}.
         */
        public @NotNull String readUntil(char c) throws IOException {
            StringBuilder sb = new StringBuilder();

            do {
                readChar();
                sb.append(charRet.character);
            } while (charRet.character != c && charRet.character != '\n' && !charRet.eof);

            updateState(sb);
            return sb.toString();
        }

        /**
         * Reads until the first occurrence of the character the end of the line or the end of the file.
         * This method will set the {@link #eol} and {@link #eof} flags accordingly.
         * @return the String read, not containing {@code \r\n}.
         */
        public @NotNull String readUntilLineFeed() throws IOException {
            StringBuilder sb = new StringBuilder();

            do {
                readChar();
                sb.append(charRet.character);
            } while (charRet.character != '\n' && !charRet.eof);

            updateState(sb);
            return sb.toString();
        }
    }

    public @NotNull LineReader getLineReader() {
        return lineReader;
    }

    public @NotNull InputStream getInputStreamForRemaining() {

        HTTPMessageReader this_ = this;

        return new InputStream() {
            @Override
            public int read() throws IOException {
                if(position < limit)
                    return Byte.toUnsignedInt(buffer[position++]);
                return in.read();
            }

            @Override
            public int read(byte @NotNull [] dst, int off, int len) throws IOException {
                int copyLen = 0;
                if(position < limit) {
                    copyLen = Math.min(len, limit - position);
                    System.arraycopy(buffer, position, dst, off, copyLen);
                    position += copyLen;
                }

                if(copyLen < len) {
                    //try to read further

                    int furtherReadLen = in.read(dst, off + copyLen, len - copyLen);

                    if(furtherReadLen == -1 && copyLen == 0)
                        return -1;

                    copyLen += Math.max(0, furtherReadLen);
                }

                return copyLen;
            }

            @Override
            public void close() throws IOException {
                this_.close();
            }
        };
    }

    @Override
    public void close() throws IOException {
        in.close();
    }

}
