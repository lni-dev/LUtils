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

package de.linusdev.lutils.net.ws.frames.writable;

import de.linusdev.lutils.net.ws.frame.OpCodes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;

public class WritableEmptyFrame implements WriteableFrame {

    private final @NotNull OpCodes opCode;

    public WritableEmptyFrame(@NotNull OpCodes opCode) {
        this.opCode = opCode;
    }

    @Override
    public @NotNull OpCodes opcode() {
        return opCode;
    }

    @Override
    public int length() {
        return 0;
    }

    @Override
    public @Nullable InputStream stream() {
        return null;
    }
}
