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

package de.linusdev.lutils.other;

public class ByteUtils {

    /**
     * Constructs an 32-bit integer as follows: {@code 0x00_00_00_b1}.
     */
    public static int constructInt(byte b1) {
        return b1 & 0xFF;
    }

    /**
     * Constructs an 32-bit integer as follows: {@code 0x00_00_b1_b2}.
     */
    public static int constructInt(byte b1, byte b2) {
        return ((b1 & 0xFF) << 8) | (b2 & 0xFF);
    }

    /**
     * Constructs an 32-bit integer as follows: {@code 0x00_b1_b2_b3}.
     */
    public static int constructInt(byte b1, byte b2, byte b3) {
        return ((b1 & 0xFF) << 16) | ((b2 & 0xFF) << 8) | (b3 & 0xFF);
    }

    /**
     * Constructs an 32-bit integer as follows: {@code 0xb1_b2_b3_b4}.
     */
    public static int constructInt(byte b1, byte b2, byte b3, byte b4) {
        return ((b1 & 0xFF) << 24) | ((b2 & 0xFF) << 16) | ((b3 & 0xFF) << 8) | (b4 & 0xFF);
    }

}
