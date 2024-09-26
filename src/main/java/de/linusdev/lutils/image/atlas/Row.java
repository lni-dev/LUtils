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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Row<ID> {

    private final @Nullable Row<ID> parentRow;

    private final int height;
    private int currentYOffset = 0;
    private final @NotNull List<@NotNull Row<ID>> subRows = new ArrayList<>();
    private @Nullable Row<ID> largestSubRow;

    private int minWidth = 0;
    private int currentMaxWidth;
    private int currentXOffset = 0;
    private final @NotNull List<@NotNull ImageRef<ID>> images = new ArrayList<>();

    public Row(int maxWidth, int height) {
        this.parentRow = null;
        this.height = height;
        this.currentMaxWidth = maxWidth;
    }

    public Row(@Nullable Row<ID> parentRow, int currentMaxWidth, @NotNull ImageRef<ID> firstImage) {
        this.parentRow = parentRow;
        this.height = firstImage.getHeight();
        this.currentMaxWidth = currentMaxWidth;

        add(firstImage, true);
    }

    protected void addToMinWidth(int add) {
        int oldMinWidth = minWidth;
        minWidth += add;
        if(parentRow != null)
            parentRow.onSubRowWidthUpdated(this, oldMinWidth);
    }

    protected void addToCurrentXOffset(int add) {
        currentXOffset += add;
        for (@NotNull Row<ID> subRow : subRows) {
            subRow.updateCurrentMaxWidth(currentMaxWidth - currentXOffset);
        }
    }

    protected void onSubRowWidthUpdated(@NotNull Row<ID> subRow, int oldMinWidth) {
        if(subRow == largestSubRow) {
            addToMinWidth(subRow.minWidth - oldMinWidth);
        } else if (largestSubRow == null) {
            largestSubRow = subRow;
            addToMinWidth(subRow.minWidth);
        } else if(largestSubRow.minWidth < subRow.minWidth) {
            addToMinWidth(subRow.minWidth - largestSubRow.minWidth);
            largestSubRow = subRow;
        }
    }

    protected void updateCurrentMaxWidth(int newMaxWidth) {
        currentMaxWidth = newMaxWidth;
        for (@NotNull Row<ID> subRow : subRows) {
            subRow.updateCurrentMaxWidth(currentMaxWidth - currentXOffset);
        }
    }

    protected boolean addSubRow(@NotNull ImageRef<ID> image) {
        if(currentYOffset + image.getHeight() > height)
            return false;

        if(image.getWidth() > currentMaxWidth - currentXOffset)
            return false;

        currentYOffset += image.getHeight();
        subRows.add(new Row<>(this, currentMaxWidth - currentXOffset, image));
        return true;
    }

    public boolean add(@NotNull ImageRef<ID> image, boolean tryRotate) {
        if(image.getHeight() > height) {
            if(!tryRotate || image.getHeight() == image.getWidth()) {
                return false;
            }

            image.rotate();
            if(add(image, false)) return true;
            image.rotate();
            return false;
        }


        if(image.getHeight() < height) {
            for (@NotNull Row<ID> subRow : subRows) {
                if(subRow.add(image, true))
                    return true;
            }

            return addSubRow(image);
        }

        if(minWidth + image.getWidth() > currentMaxWidth)
            return false;

        images.add(image);
        addToCurrentXOffset(image.getWidth());
        addToMinWidth(image.getWidth());

        return true;
    }

    public void fillAtlas(@NotNull Atlas<?, ID> atlas, int offsetX, int offsetY) throws IOException {

        int x = offsetX;
        int y = offsetY;
        for (@NotNull ImageRef<ID> imageRef : images) {
            imageRef.storeTo(atlas, x, y);
            x += imageRef.getWidth();
        }

        for (@NotNull Row<ID> subRow : subRows) {
            subRow.fillAtlas(atlas, x, y);
            y += subRow.height;
        }
    }

    public int getCurrentHeight() {
        return images.isEmpty() ? currentYOffset : height;
    }

    public int getCurrentWidth() {
        return minWidth;
    }
}
