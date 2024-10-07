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

package de.linusdev.lutils.net.ws.frames;

import de.linusdev.lutils.net.ws.frame.AbstractFrame;
import de.linusdev.lutils.net.ws.frame.Frame;
import de.linusdev.lutils.net.ws.frame.OpCodes;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;

public class TextFrame implements AbstractFrame {

    private final @NotNull Frame frame;

    public TextFrame(@NotNull Frame frame) {
        this.frame = frame;
    }

    public @NotNull String getText() {
        return new String(frame.getPayload(), StandardCharsets.UTF_8);
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
