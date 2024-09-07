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

package de.linusdev.lutils.image;

import org.jetbrains.annotations.NotNull;

import java.awt.image.BufferedImage;

public class JavaBackedImage implements Image {

    private final @NotNull BufferedImage image;

    public JavaBackedImage(@NotNull BufferedImage image) {
        this.image = image;
    }

    @Override
    public int getWidth() {
        return image.getWidth();
    }

    @Override
    public int getHeight() {
        return image.getHeight();
    }

    @Override
    public int getPixelAsRGBA(int x, int y) {
        return PixelFormat.A8R8G8B8_SRGB.toR8G8B8A8_SRGB(image.getRGB(x, y));
    }

    @Override
    public void setPixelAsRGBA(int x, int y, int rgba) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isReadOnly() {
        return true;
    }

}
