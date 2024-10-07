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

package de.linusdev.lutils.net.ws.frame;

import de.linusdev.lutils.net.ws.control.CloseFrame;
import de.linusdev.lutils.net.ws.frames.TextFrame;
import de.linusdev.lutils.net.ws.frames.writable.WriteableFrame;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayInputStream;
import java.io.InputStream;


public class Frame implements WriteableFrame {

    private final boolean fin;
    private final boolean rsv1;
    private final boolean rsv2;
    private final boolean rsv3;
    private final boolean masked;
    private final OpCodes opcode;
    private final int payloadLength;
    private final byte[] payload;

    public Frame(
            boolean fin, boolean rsv1, boolean rsv2, boolean rsv3, boolean masked, byte opcode,
            int payloadLength, byte[] payload
    ) {
        this.fin = fin;
        this.rsv1 = rsv1;
        this.rsv2 = rsv2;
        this.rsv3 = rsv3;
        this.masked = masked;
        this.opcode = OpCodes.ofByte(opcode);
        this.payloadLength = payloadLength;
        this.payload = payload;
    }

    @Override
    public boolean isFinal() {
        return fin;
    }


    public boolean getReserved1() {
        return rsv1;
    }

    public boolean getReserved2() {
        return rsv2;
    }

    public boolean getReserved3() {
        return rsv3;
    }

    public boolean wasMasked() {
        return masked;
    }

    @Override
    public @NotNull OpCodes opcode() {
        return opcode;
    }

    @Override
    public int length() {
        return payloadLength;
    }

    @Override
    public @Nullable InputStream stream() {
        return new ByteArrayInputStream(payload);
    }

    public @NotNull CloseFrame toCloseFrame() {
        assert opcode == OpCodes.CLOSE;
        return new CloseFrame(this);
    }

    public @NotNull TextFrame toTextFrame() {
        assert opcode == OpCodes.TEXT_UTF8;
        return new TextFrame(this);
    }

    public byte[] getPayload() {
        return payload;
    }
}
