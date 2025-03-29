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

package de.linusdev.lutils.image.wip_webp.reader.simple.lossless.transforms;

import de.linusdev.lutils.image.wip_webp.reader.BitReader;
import de.linusdev.lutils.image.wip_webp.reader.WebPImageInfo;
import org.jetbrains.annotations.NotNull;

public class PredictorTypeTransform {

    public static void read(@NotNull BitReader reader, @NotNull WebPImageInfo info) {
        int sizeBits = reader.readBitsToInt(3) + 2;
        int blockWidth = 1 << sizeBits;
        int blockHeight = 1 << sizeBits;
        int transformWidth = (info.imageWidth() + (1 << sizeBits) - 1) / (1 << sizeBits);

    }

}
