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

package de.linusdev.lutils.net.ws;

import de.linusdev.lutils.io.InputStreamUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Random;

public class WebSocket {

    private final @NotNull Random RANDOM = new Random();

    private final @NotNull Socket socket;
    private final boolean allowUnmaskedIncomingMessages;
    private final boolean maskOutgoingMessages;

    private final @NotNull InputStream in;
    private final @NotNull OutputStream out;

    public WebSocket(@NotNull Socket socket, boolean allowUnmaskedIncomingMessages, boolean maskOutgoingMessages) throws IOException {
        this.socket = socket;
        this.in = socket.getInputStream();
        this.out = socket.getOutputStream();
        this.allowUnmaskedIncomingMessages = allowUnmaskedIncomingMessages;
        this.maskOutgoingMessages = maskOutgoingMessages;
    }

    public boolean isAvailable() throws IOException {
        return in.available() > 0;
    }

    public @NotNull BufferedPayload readPayload() throws IOException {
        return new BufferedPayload(this);
    }

    @NotNull Frame readFrame() throws IOException {
        byte b = readByte();

        boolean fin = (b & 0x80) > 0;
        boolean rsv1 = (b & 0x40) > 0;
        boolean rsv2 = (b & 0x20) > 0;
        boolean rsv3 = (b & 0x10) > 0;
        byte opcode = (byte) (b & 0x0F);

        b = readByte();

        boolean mask = (b & 0x80) > 0;
        int payloadLength = readPayloadLength(b);
        byte[] maskingKey = mask ? readMaskingKey() : null;

        if(!allowUnmaskedIncomingMessages && !mask)
            throw new IOException("Unmasked payloads are not supported.");

        return new Frame(fin, mask, maskingKey, opcode, payloadLength, in);
    }

    public void writeFrame(@NotNull WriteablePayload payload) throws IOException {
        boolean fin = true;
        byte[] maskingKey = new byte[4];
        int payloadLength = payload.length();

        byte b = 0;
        if(fin) b |= (byte) (0x01 << 7);
        b |= payload.opcode().getCode();

        out.write(b & 0xFF);

        b = 0;
        if(maskOutgoingMessages) b |= (byte) (0x01 << 7);

        if(payloadLength >= 126) {
            if(payloadLength <= 65535) {
                b |= 126 & 0xFF;
                out.write(b);
            } else {
                b |= 127 & 0xFF;
                out.write(b);
                out.write((payloadLength & 0xFF000000) >>> 24);
                out.write((payloadLength & 0x00FF0000) >>> 16);
            }
            out.write((payloadLength & 0x0000FF00) >>> 8);
            out.write((payloadLength & 0x000000FF));
        } else {
            b |= (byte) payloadLength;
            out.write(b);
        }

        if(maskOutgoingMessages) {
            RANDOM.nextBytes(maskingKey);
            out.write(maskingKey[0]);
            out.write(maskingKey[1]);
            out.write(maskingKey[2]);
            out.write(maskingKey[3]);
        }

        if(payloadLength > 0) {
            try(InputStream in = payload.stream()) {
                if(in == null)
                    throw new IllegalStateException("Payload length is not 0, but no stream is given.");

                byte[] bytes = new byte[payloadLength];

                if(!InputStreamUtils.readUntilArrayIsFull(in, bytes)) {
                    throw new IllegalStateException("Payload length is " + payloadLength + ", but actual length was smaller.");
                }

                out.write(bytes);
            }

        }
    }


    private int readPayloadLength(byte b) throws IOException {
        int payloadLength = (b & 0x7F);

        if(payloadLength <= 125)
            return payloadLength;
        else if(payloadLength == 126)
            return (readByte() & 0xFF) | ((readByte() & 0xFF) << 8);
        else /*if(payloadLength == 127)*/
            return (readByte() & 0xFF)
                    | ((readByte() & 0xFF) << 8)
                    | ((readByte() & 0xFF) << 16)
                    | ((readByte() & 0xFF) << 24);

    }

    private byte[] readMaskingKey() throws IOException {
        return new byte[] {
                readByte(), readByte(), readByte(), readByte()
        };
    }

    private byte readByte() throws IOException {
        int i = in.read();
        if(i == -1) throw new IOException("Unexpected EOF");
        return (byte) i;
    }
}
