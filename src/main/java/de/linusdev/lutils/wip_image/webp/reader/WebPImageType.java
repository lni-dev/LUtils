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

import static de.linusdev.lutils.wip_image.webp.reader.WebReaderUtils.*;

public enum WebPImageType {
    SIMPLE_LOSSY((byte) 0x20),
    SIMPLE_LOSSLESS((byte) 0x4C),
    EXTENDED((byte) 0x58),

    ;

    public static @NotNull WebPImageType of(byte[] data, int offset) throws WebPReaderException {
        if(!checkChunkThreeCC(data, offset, VP8))
            throw new WebPReaderException("Unknown webp type: " + convertChunkFourCC(data, offset));

        byte lastFourCC = getChunkFourCC(data, offset);

        if(lastFourCC == SIMPLE_LOSSY.lastFourCCByte) return SIMPLE_LOSSY;
        if(lastFourCC == SIMPLE_LOSSLESS.lastFourCCByte) return SIMPLE_LOSSLESS;
        if(lastFourCC == EXTENDED.lastFourCCByte) return EXTENDED;

        throw new WebPReaderException("Unknown webp type: " + convertChunkFourCC(data, offset));
    }

    private final byte lastFourCCByte;

    WebPImageType(byte lastFourCCByte) {
        this.lastFourCCByte = lastFourCCByte;
    }

    public byte getLastFourCCByte() {
        return lastFourCCByte;
    }
}
