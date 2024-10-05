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

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;

public class Frame extends InputStream {

    private final boolean fin;
    private final boolean masked;
    private final byte[] maskKey;
    private final OpCodes opcode;
    private final int payloadLength;
    private final @NotNull InputStream in;

    private int index = 0;

    public Frame(boolean fin, boolean masked, byte[] maskKey, byte opcode, int payloadLength, @NotNull InputStream in) {
        this.fin = fin;
        this.masked = masked;
        this.maskKey = maskKey;
        this.opcode = OpCodes.ofByte(opcode);
        this.payloadLength = payloadLength;
        this.in = in;
    }

    @Override
    public int read() throws IOException {
        int read;
        if(index >= payloadLength || (read = in.read()) == -1)
            return -1;
        return (((byte) read) ^ maskKey[index++ % 4]) & 0xFF;
    }

    public boolean isFinal() {
        return fin;
    }

    public OpCodes getOpcode() {
        return opcode;
    }
}
