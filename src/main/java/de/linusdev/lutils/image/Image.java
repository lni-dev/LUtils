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

import de.linusdev.lutils.ansi.sgr.SGR;
import de.linusdev.lutils.color.Color;
import de.linusdev.lutils.color.RGBAColor;
import de.linusdev.lutils.image.java.JavaBackedImage;
import de.linusdev.lutils.image.view.ImageView;
import de.linusdev.lutils.image.view.RotatedImageView;
import org.jetbrains.annotations.NotNull;

import java.awt.image.BufferedImage;

/**
 * Pixels positions go from {@code 0} to {@code getWidth() - 1} and {@code 0} to {@code getHeight() - 1}
 * for x and y respectively. If positions outside of this range are passed, it may result in unexpected
 * behavior.
 */
public interface Image extends ImageSize{

    static void copy(@NotNull Image src, @NotNull Image dst) {
        if(dst.isReadOnly())
            throw new IllegalArgumentException("Destination image is read only.");

        if(dst.getWidth() < src.getWidth())
            throw new IllegalArgumentException("Destination image's width is too small");

        if(dst.getHeight() < src.getHeight())
            throw new IllegalArgumentException("Destination image's height is too small");

        for (int y = 0; y < src.getHeight(); y++) {
            for (int x = 0; x < src.getWidth(); x++) {
                dst.setPixelAsRGBA(x, y, src.getPixelAsRGBA(x, y));
            }
        }
    }

    static void copy(
            @NotNull Image src, int srcOffsetX, int srcOffsetY,
            @NotNull Image dst, int dstOffsetX, int dstOffsetY
    ) {
        int width = src.getWidth() - srcOffsetX;
        int height = src.getHeight() - srcOffsetY;

        if(dst.isReadOnly())
            throw new IllegalArgumentException("Destination image is read only.");

        if(dst.getWidth() - dstOffsetX < width)
            throw new IllegalArgumentException("Destination image's width is too small");

        if(dst.getHeight() - dstOffsetY < height)
            throw new IllegalArgumentException("Destination image's height is too small");

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                dst.setPixelAsRGBA(dstOffsetX + x, dstOffsetY + y, src.getPixelAsRGBA(srcOffsetX + x, srcOffsetY + y));
            }
        }
    }

    static boolean equals(@NotNull Image that, @NotNull Image other) {
        if(that.getWidth() != other.getWidth()) return false;
        if(that.getHeight() != other.getHeight()) return false;

        for (int y = 0; y < that.getHeight(); y++) {
            for (int x = 0; x < that.getWidth(); x++) {
                if(that.getPixelAsRGBA(x, y) != other.getPixelAsRGBA(x, y))
                    return false;
            }
        }

        return true;
    }

    static @NotNull String printable(@NotNull Image image) {
        StringBuilder ret = new StringBuilder();

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                RGBAColor color = image.getPixelAsRGBAColor(x, y);
                ret
                        .append(color.addToSgrAsBackground(new SGR()).construct())
                        .append("  ")
                        .append(SGR.reset());
            }
            ret.append("\n");
        }

        return ret.toString();
    }

    static @NotNull Image fill(@NotNull Image image, @NotNull Color color) {
        int rgba = color.toRGBAColor().toRGBAHex();
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
               image.setPixelAsRGBA(x, y, rgba);
            }
        }

        return image;
    }

    static @NotNull Image create(int width, int height) {
        return new JavaBackedImage(new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB));
    }

    /**
     * Get pixel as int in {@link PixelFormat#R8G8B8A8_SRGB R8G8B8A8_SRGB} format.
     */
    int getPixelAsRGBA(int x, int y);

    /**
     * Set pixel as int in {@link PixelFormat#R8G8B8A8_SRGB R8G8B8A8_SRGB} format.
     * @throws UnsupportedOperationException if {@link #isReadOnly()} is {@code true}
     */
    void setPixelAsRGBA(int x, int y, int rgba);

    default @NotNull RGBAColor getPixelAsRGBAColor(int x, int y) {
        return Color.ofRGBA(getPixelAsRGBA(x, y));
    }

    /**
     * {@code true} if this image is read only.
     */
    boolean isReadOnly();

    default @NotNull Image createView(int offsetX, int offsetY) {
        return new ImageView(this, offsetX, offsetY, getWidth() - offsetX, getHeight() - offsetY);
    }

    default @NotNull Image createView(int offsetX, int offsetY, int width, int height) {
        return new ImageView(this, offsetX, offsetY, width, height);
    }

    default @NotNull Image createRotatedView() {
        return new RotatedImageView(this);
    }

}
