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

package de.linusdev.lutils.nat;

import org.jetbrains.annotations.NotNull;

public interface EndianUtils {

    static int littleEndianBytesToInt(byte @NotNull [] bytes, int offset) {
        return littleEndianBytesToInt(bytes[offset], bytes[offset+1], bytes[offset+2], bytes[offset+3]);
    }

    static int littleEndianBytesToInt(byte b1, byte b2, byte b3, byte b4) {
        return (b1 & 0xFF) | ((b2 & 0xFF) << 8) | ((b3 & 0xFF) << 16) | ((b4 & 0xFF) << 24);
    }

    static int bigEndianBytesToInt(byte b1, byte b2, byte b3, byte b4) {
        return ((b1 & 0xFF) << 24) | ((b2 & 0xFF) << 16) | ((b3 & 0xFF) << 8) | (b4 & 0xFF);
    }

}
