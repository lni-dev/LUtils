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

package de.linusdev.lutils.color.creator;

import de.linusdev.lutils.color.Color;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.Random;
import java.util.function.IntFunction;

public class IndexedColorCreator implements ColorCreator {

    private final int count;
    private final @NotNull IntFunction<@NotNull Color> indexToColor;

    public IndexedColorCreator(int count, @NotNull IntFunction<@NotNull Color> indexToColor) {
        this.count = count;
        this.indexToColor = indexToColor;
    }

    @Override
    public @NotNull Color getRandomColor(@Nullable Random random) {
        if(random == null) random = new Random();
        return indexToColor.apply(random.nextInt(count));
    }

    @Override
    public @NotNull Iterator<Color> iterator() {
        return new Iterator<>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < count;
            }

            @Override
            public Color next() {
                return indexToColor.apply(index++);
            }
        };
    }
}
