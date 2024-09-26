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

package de.linusdev.lutils.image.atlas;

import de.linusdev.lutils.image.Image;
import org.jetbrains.annotations.NotNull;

public class AtlasImage<ID> implements Image {

    private final @NotNull Atlas<?, ID> atlas;

    private final ID id;

    private final boolean rotated;

    /**
     * Start of uv in the x coordinate. Already adjusted if image is {@link #rotated}.
     */
    private final double uvXStart;
    /**
     * Start of uv in the y coordinate. Already adjusted if image is {@link #rotated}.
     */
    private final double uvYStart;
    /**
     * End of uv in the x coordinate. Already adjusted if image is {@link #rotated}.
     */
    private final double uvXEnd;
    /**
     * End of uv in the y coordinate. Already adjusted if image is {@link #rotated}.
     */
    private final double uvYEnd;

    /**
     * Not adjusted for {@link #rotated rotation}.
     */
    private final int xStart;
    /**
     * Not adjusted for {@link #rotated rotation}.
     */
    private final int yStart;
    /**
     * Not adjusted for {@link #rotated rotation}.
     */
    private final int width;
    /**
     * Not adjusted for {@link #rotated rotation}.
     */
    private final int height;

    public AtlasImage(@NotNull Atlas<?, ID> atlas, ID id, boolean rotated, double uvXStart, double uvYStart, double uvXEnd, double uvYEnd, int xStart, int yStart, int width, int height) {
        this.atlas = atlas;
        this.id = id;
        this.rotated = rotated;
        this.uvXStart = uvXStart;
        this.uvYStart = uvYStart;
        this.uvXEnd = uvXEnd;
        this.uvYEnd = uvYEnd;
        this.xStart = xStart;
        this.yStart = yStart;
        this.width = width;
        this.height = height;
    }

    @Override
    public int getPixelAsRGBA(int x, int y) {
        return atlas.getPixelAsRGBA(xStart + x, yStart + y);
    }

    @Override
    public void setPixelAsRGBA(int x, int y, int rgba) {
        atlas.setPixelAsRGBA(xStart + x, yStart + y, rgba);
    }

    @Override
    public boolean isReadOnly() {
        return atlas.isReadOnly();
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    public double getUvXStart() {
        return uvXStart;
    }

    public double getUvXEnd() {
        return uvXEnd;
    }

    public double getUvYStart() {
        return uvYStart;
    }

    public double getUvYEnd() {
        return uvYEnd;
    }

    public boolean isRotated() {
        return rotated;
    }

    public ID getId() {
        return id;
    }
}
