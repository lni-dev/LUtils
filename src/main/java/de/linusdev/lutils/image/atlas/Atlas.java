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

package de.linusdev.lutils.image.atlas;

import de.linusdev.lutils.image.Image;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Atlas<T extends Image, ID> implements Image, Iterable<AtlasImage<ID>> {

    private final @NotNull T backingImage;
    private final @NotNull List<@NotNull AtlasImage<ID>> images;

    public Atlas(@NotNull T backingImage, int imgCount) {
        this.backingImage = backingImage;
        this.images = new ArrayList<>(imgCount);
    }

    void addImage(@NotNull ImageRef<ID> ref, int offsetX, int offsetY) {

        double uvXStart = (double) offsetX / getWidth();
        double uvYStart = (double) offsetY / getHeight();
        double uvXEnd =  uvXStart + (double) ref.getWidth() / getWidth();
        double uvYEnd =  uvYStart + (double) ref.getHeight() / getHeight();

        AtlasImage<ID> added = new AtlasImage<>(this,
                ref.getId(),
                ref.isRotated(),
                ref.isRotated() ? uvYStart : uvXStart,
                ref.isRotated() ? uvXStart : uvYStart,
                ref.isRotated() ? uvYEnd : uvXEnd,
                ref.isRotated() ? uvXEnd : uvYEnd,
                offsetX, offsetY,
                ref.getWidth(),
                ref.getHeight()
        );

        images.add(added);
        ref.onAddedToAtlas(added);
    }

    @Override
    public int getPixelAsRGBA(int x, int y) {
        return backingImage.getPixelAsRGBA(x, y);
    }

    @Override
    public void setPixelAsRGBA(int x, int y, int rgba) {
        backingImage.setPixelAsRGBA(x, y, rgba);
    }

    @Override
    public boolean isReadOnly() {
        return backingImage.isReadOnly();
    }

    @Override
    public int getWidth() {
        return backingImage.getWidth();
    }

    @Override
    public int getHeight() {
        return backingImage.getHeight();
    }

    @SuppressWarnings("unused")
    public @NotNull T getBackingImage() {
        return backingImage;
    }

    @Override
    public @NotNull Iterator<AtlasImage<ID>> iterator() {
        return images.iterator();
    }
}
