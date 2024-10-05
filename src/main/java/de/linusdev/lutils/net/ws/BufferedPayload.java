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
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class BufferedPayload extends InputStream {

    private final @Nullable OpCodes opcode;
    private final @NotNull List<byte[]> content;
    private int listIndex = 0;
    private int index = 0;

    public BufferedPayload(@NotNull WebSocket parent) throws IOException {
        Frame first = parent.readFrame();
        if(first.isFinal())
            this.content = List.of(first.readAllBytes());
        else {
            this.content = new ArrayList<>();
            Frame frame = first;
            while (!frame.isFinal()) {
                content.add(frame.readAllBytes());
                frame = parent.readFrame();
            }
        }

        this.opcode = first.getOpcode();
    }

    @Override
    public int read() throws IOException {

        if(listIndex >= content.size())
            return -1;

        byte[] arr = content.get(listIndex);

        if(index >= arr.length) {
            listIndex++;
            index = 0;
            return read();
        }

        return arr[index] & 0xFF;
    }
}
