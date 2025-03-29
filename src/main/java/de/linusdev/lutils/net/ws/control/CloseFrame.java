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

package de.linusdev.lutils.net.ws.control;

import de.linusdev.lutils.net.ws.frame.AbstractFrame;
import de.linusdev.lutils.net.ws.frame.Frame;
import de.linusdev.lutils.net.ws.frame.OpCodes;
import de.linusdev.lutils.other.ByteUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.charset.StandardCharsets;

public class CloseFrame implements AbstractFrame, ControlFrame {

    private final @NotNull Frame frame;

    private final @Nullable Integer statusCode;
    private final @Nullable String reason;

    public CloseFrame(@NotNull Frame frame) {
        this.frame = frame;

        if(frame.length() == 0) {
            statusCode = null;
            reason = null;
            return;
        }

        if(frame.length() < 2)
            throw new IllegalStateException("Illegal payload length for a close frame.");

        byte[] payload = frame.getPayload();

        statusCode = ByteUtils.constructInt(payload[0], payload[1]);
        reason = frame.length() > 2 ? new String(payload, 2, frame.length() - 2, StandardCharsets.UTF_8) : null;

    }

    /**
     * Close status code.
     * @see WSStatusCode
     */
    public @Nullable WSStatusCode statusCode() {
        return statusCode == null ? null : WSStatusCode.of(statusCode);
    }

    /**
     * Close reason. May be NOT human-readable.
     */
    public @Nullable String reason() {
        return reason;
    }

    @Override
    public @NotNull OpCodes opcode() {
        return frame.opcode();
    }

    @Override
    public int length() {
        return frame.length();
    }

    @Override
    public boolean isFinal() {
        return frame.isFinal();
    }
}
