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
import de.linusdev.lutils.image.ImageSize;
import de.linusdev.lutils.image.ImageSupplier;
import de.linusdev.lutils.math.LMath;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AtlasBuilder<ID> {

    public final @NotNull List<ImageRef<ID>> sizes;

    public AtlasBuilder(int expectedImageCount) {
        this.sizes = new ArrayList<>(expectedImageCount);
    }

    public void add(@NotNull ImageRef<ID> ref) {
        sizes.add(ref);
    }

    private @NotNull ImageSize calcExpectedImageSize() {
        int sumWidth = 0;
        int sumHeight = 0;

        for (ImageSize size : sizes) {
            sumWidth += size.getWidth();
            sumHeight += size.getHeight();
        }

        int avgWidth = sumWidth / sizes.size();

        double imagesPerLine = Math.ceil(Math.sqrt(sizes.size()));
        int expectedWidth = (int) (avgWidth * imagesPerLine);

        // Increase image size based on image count. More images -> less likely to require more unused space
        double denominator = LMath.interpolate(
                4, 1000,
                2, 20,
                sizes.size()
        );

        expectedWidth += (int) (expectedWidth / denominator);
        return ImageSize.of(expectedWidth, sumHeight);
    }
    
    public <T extends Image> @NotNull Atlas<T, ID> build(@NotNull ImageSupplier<T> supplier) throws IOException {
        sizes.sort(Comparator.comparingInt(ImageSize::getArea).reversed());
        
        ImageSize expectedImageSize = calcExpectedImageSize();

        Row<ID> mainRow = new Row<>(expectedImageSize.getWidth(), expectedImageSize.getHeight());
        for (ImageRef<ID> ref : sizes) {
            if(ref.getHeight() > ref.getWidth()) ref.rotate();
            if(!mainRow.add(ref, true)) throw new Error();
        }

        T image = supplier.supply(mainRow.getCurrentWidth(), mainRow.getCurrentHeight());
        Atlas<T, ID> atlas = new Atlas<>(image, sizes.size());
        mainRow.fillAtlas(atlas, 0, 0);

        return atlas;
    }
}
