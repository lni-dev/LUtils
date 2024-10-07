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

package de.linusdev.lutils.net.ws.control.writable;

import de.linusdev.lutils.net.ws.control.ControlFrame;
import de.linusdev.lutils.net.ws.control.WSStatusCode;
import de.linusdev.lutils.net.ws.frame.OpCodes;
import de.linusdev.lutils.net.ws.frames.writable.WriteableFrame;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class WritableCloseFrame implements WriteableFrame, ControlFrame {

    private final byte @Nullable [] body;

    @SuppressWarnings("unused")
    public WritableCloseFrame() {
        this(null, null);
    }

    public WritableCloseFrame(@Nullable WSStatusCode statusCode) {
        this(statusCode, null);
    }

    public WritableCloseFrame(@Nullable WSStatusCode statusCode, @Nullable String reason) {
        if(statusCode == null && reason != null)
            throw new IllegalArgumentException("A close frame cannot contain a reason string without a status code.");

        if(statusCode == null) {
            this.body = null;
            return;
        }

        if(reason == null) {
            this.body = new byte[] {
                    (byte) (statusCode.code() & 0xFF00 >>> 8),
                    (byte) (statusCode.code() & 0x00FF)
            };
            return;
        }

        byte[] stringBytes = reason.getBytes(StandardCharsets.UTF_8);
        this.body = new byte[stringBytes.length + 2];

        this.body[0] = (byte) (statusCode.code() & 0xFF00 >>> 8);
        this.body[1] = (byte) (statusCode.code() & 0x00FF);

        System.arraycopy(stringBytes, 0, this.body, 2, stringBytes.length);

        if(this.body.length > MAX_BODY_LENGTH)
            throw new IllegalArgumentException("A control frame body cannot be larger than " + MAX_BODY_LENGTH + "bytes.");
    }

    @Override
    public @NotNull OpCodes opcode() {
        return OpCodes.CLOSE;
    }

    @Override
    public int length() {
        return body == null ? 0 : body.length;
    }

    @Override
    public @Nullable InputStream stream() {
        return body == null ? null : new ByteArrayInputStream(body);
    }

}
