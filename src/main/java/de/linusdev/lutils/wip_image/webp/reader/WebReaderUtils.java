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

package de.linusdev.lutils.wip_image.webp.reader;

import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;

public interface WebReaderUtils {

    int RIFF_FOUR_CC_BYTE_LENGTH = 4;
    int RIFF_CHUNK_SIZE_BYTE_LENGTH = 4;
    int RIFF_CHUNK_HEADER = RIFF_FOUR_CC_BYTE_LENGTH + RIFF_CHUNK_SIZE_BYTE_LENGTH;

    int WEBP_FILE_HEADER_BYTE_LENGTH = 2 * RIFF_FOUR_CC_BYTE_LENGTH + RIFF_CHUNK_SIZE_BYTE_LENGTH;

    byte[] RIFF = new byte[] {0x52, 0x49, 0x46, 0x46};
    byte[] WEBP = new byte[] {0x57, 0x45, 0x42, 0x50};

    byte[] VP8 = new byte[] {0x56, 0x50, 0x38};

    static String convertChunkFourCC(byte @NotNull [] data, int offset) {
        return new String(data, offset, RIFF_FOUR_CC_BYTE_LENGTH, StandardCharsets.US_ASCII);
    }

    static boolean checkChunkFourCC(byte @NotNull [] data, int offset, byte @NotNull [] expected) {
        return data[offset] == expected[0]
                && data[offset + 1] == expected[1]
                && data[offset + 2] == expected[2]
                && data[offset + 3] == expected[3];

    }

    static boolean checkChunkThreeCC(byte @NotNull [] data, int offset, byte @NotNull [] expected) {
        return data[offset] == expected[0]
                && data[offset + 1] == expected[1]
                && data[offset + 2] == expected[2];
    }

    static byte getChunkFourCC(byte @NotNull [] data, int offset) {
        return data[offset + 3];
    }

}
