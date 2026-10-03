/*
 * Copyright (c) 2026 Linus Andera
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

package de.linusdev.lutils.image;

import org.jetbrains.annotations.NotNull;

public enum ScaleType {
    NEAREST_NEIGHBOR {
        @Override
        @NotNull Image scale(@NotNull Image src, @NotNull Image dst) {
            int srcWidth = src.getWidth();
            int srcHeight = src.getHeight();

            for (int y = 0; y < dst.getHeight(); y++) {
                int srcY = (int) ((y + 0.5f) * srcHeight / dst.getHeight());
                srcY = Math.min(srcY, srcHeight - 1);

                for (int x = 0; x < dst.getWidth(); x++) {
                    int srcX = (int) ((x + 0.5f) * srcWidth / dst.getWidth());
                    srcX = Math.min(srcX, srcWidth - 1);
                    dst.setPixelAsRGBA(x, y, src.getPixelAsRGBA(srcX, srcY));
                }
            }

            return dst;
        }
    },
    ;

    abstract @NotNull Image scale(@NotNull Image src, @NotNull Image dst);
}
