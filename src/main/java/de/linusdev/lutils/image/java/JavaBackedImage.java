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

package de.linusdev.lutils.image.java;

import de.linusdev.lutils.image.Image;
import de.linusdev.lutils.image.PixelFormat;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.io.OutputStream;

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
        image.setRGB(x, y, PixelFormat.R8G8B8A8_SRGB.toA8R8G8B8_SRGB(rgba));
    }

    @Override
    public boolean isReadOnly() {
        return !image.hasTileWriters();
    }

    /**
     * Writes this image to given stream.
     * @param formatName format to use. See {@link ImageIO#write(RenderedImage, String, OutputStream)}.
     * @param stream the stream to write to
     * @throws IOException while writing
     */
    public void write(@Nullable String formatName, @NotNull OutputStream stream) throws IOException {
        if(formatName == null)
            formatName = "png";
        ImageIO.write(image, formatName, stream);
    }

}
