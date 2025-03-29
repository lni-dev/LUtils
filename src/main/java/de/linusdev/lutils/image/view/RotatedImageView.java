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

@SuppressWarnings("SuspiciousNameCombination")
public class RotatedImageView implements Image {

    private final @NotNull Image original;


    public RotatedImageView(@NotNull Image original) {
        this.original = original;
    }

    @Override
    public int getPixelAsRGBA(int x, int y) {
        return original.getPixelAsRGBA(y, x);
    }

    @Override
    public void setPixelAsRGBA(int x, int y, int rgba) {
        original.setPixelAsRGBA(y, x, rgba);
    }

    @Override
    public boolean isReadOnly() {
        return original.isReadOnly();
    }

    @Override
    public int getWidth() {
        return original.getHeight();
    }

    @Override
    public int getHeight() {
        return original.getWidth();
    }
}
