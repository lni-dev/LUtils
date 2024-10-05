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

public enum OpCodes {

    CONTINUATION((byte) 0x00),
    TEXT_UTF8((byte) 0x01),
    BINARY((byte) 0x02),

    CLOSE((byte) 0x08),
    PING((byte) 0x09),
    PONG((byte) 0x0A),
    ;

    private final static OpCodes[] codes = new OpCodes[]{
            /*00*/ CONTINUATION,
            /*01*/ TEXT_UTF8,
            /*02*/ BINARY,
            /*03*/ null,
            /*04*/ null,
            /*05*/ null,
            /*06*/ null,
            /*07*/ null,
            /*08*/ CLOSE,
            /*09*/ PING,
            /*0A*/ PONG,
    };

    public static @Nullable OpCodes ofByte(byte b) {
        return codes[b&0xFF];
    }

    private final byte code;

    OpCodes(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return code;
    }
}
