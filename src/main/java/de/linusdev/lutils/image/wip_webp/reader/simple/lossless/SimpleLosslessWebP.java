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

package de.linusdev.lutils.image.wip_webp.reader.simple.lossless;

import de.linusdev.lutils.image.wip_webp.reader.BitReader;
import de.linusdev.lutils.image.wip_webp.reader.WebPImageInfo;
import de.linusdev.lutils.image.wip_webp.reader.WebPReaderException;
import de.linusdev.lutils.image.wip_webp.reader.simple.lossless.transforms.PredictorTypeTransform;
import org.jetbrains.annotations.NotNull;

public class SimpleLosslessWebP {

    public static void read(@NotNull BitReader reader) throws WebPReaderException {
        if(reader.readByte() != (byte)0x2f)
            throw new WebPReaderException("1-byte signature 0x2f missing in RIFF header");

        int imageWidth = reader.readBitsToInt(14) + 1;
        int imageHeight = reader.readBitsToInt(14) + 1;
        boolean alphaIsUsed = reader.readBitsToInt(1) == 1;
        int versionNumber = reader.readBitsToInt(3);

        @NotNull WebPImageInfo info = new WebPImageInfo(imageWidth, imageHeight, alphaIsUsed, versionNumber);

        while (reader.readBitsToInt(1) == 1) {
            TransformType type = TransformType.of(reader.readBitsToInt(2));

            switch (type) {
                case PREDICTOR_TRANSFORM -> PredictorTypeTransform.read(reader, info);
                case COLOR_TRANSFORM -> {
                }
                case SUBTRACT_GREEN_TRANSFORM -> {
                }
                case COLOR_INDEXING_TRANSFORM -> {
                }
            }
        }
    }

}
