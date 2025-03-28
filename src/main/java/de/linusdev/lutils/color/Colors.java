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

package de.linusdev.lutils.color;

import de.linusdev.lutils.color.creator.ColorCreator;
import de.linusdev.lutils.color.creator.IndexedColorCreator;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public interface Colors {

    @Contract(value = "_ -> new", pure = true)
    static @NotNull ColorCreator rainbow(int colorCount) {
        double step = (HSVAColor.HSV_HUE_MAX - 0.01) / (colorCount - 1);

        return new IndexedColorCreator(colorCount,
                (index) -> Color.ofHSV(index * step, HSVAColor.HSV_SATURATION_MAX, HSVAColor.HSV_VALUE_MAX)
        );
    }





}
