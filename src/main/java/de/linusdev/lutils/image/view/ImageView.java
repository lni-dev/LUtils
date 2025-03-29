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

package de.linusdev.lutils.image.view;

import de.linusdev.lutils.image.Image;
import org.jetbrains.annotations.NotNull;

public class ImageView implements Image {

    private final @NotNull Image original;
    private final int offsetX;
    private final int offsetY;
    private final int width;
    private final int height;

    public ImageView(@NotNull Image original, int offsetX, int offsetY, int width, int height) {
        this.original = original;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.width = width;
        this.height = height;

        checks();
    }

    private void checks() {
        if(original.getWidth() - offsetX < width)
            throw new IllegalArgumentException("Original image width (" + original.getWidth()
                    + ") is too small for image view with offsetX=" + offsetX + " and width=" + width + ".");

        if(original.getHeight() - offsetY < height)
            throw new IllegalArgumentException("Original image height (" + original.getHeight()
                    + ") is too small for image view with offsetY=" + offsetY + " and height=" + height + ".");
    }

    @Override
    public int getPixelAsRGBA(int x, int y) {
        return original.getPixelAsRGBA(offsetX + x, offsetY + y);
    }

    @Override
    public void setPixelAsRGBA(int x, int y, int rgba) {
        original.setPixelAsRGBA(offsetX + x, offsetY + y, rgba);
    }

    @Override
    public boolean isReadOnly() {
        return original.isReadOnly();
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }
}
