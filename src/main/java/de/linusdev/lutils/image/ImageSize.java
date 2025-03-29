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

package de.linusdev.lutils.image;

import org.jetbrains.annotations.NotNull;

public interface ImageSize {

    static @NotNull ImageSize of(int width, int height) {
        return new ImageSize() {
            @Override
            public int getWidth() {
                return width;
            }

            @Override
            public int getHeight() {
                return height;
            }
        };
    }

    /**
     * Width of the image.
     */
    int getWidth();

    /**
     * Height of the image.
     */
    int getHeight();

    /**
     * Area of the image.
     * @return {@code getWidth() * getHeight()}
     */
    default int getArea() {
        return getWidth() * getHeight();
    }

}
