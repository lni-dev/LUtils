/*
 * Copyright (c) 2023 Linus Andera
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

package de.linusdev.lutils.math.matrix.abstracts.floatn;

import de.linusdev.lutils.math.matrix.Matrix;
import de.linusdev.lutils.math.vector.abstracts.floatn.Float2;
import de.linusdev.lutils.math.vector.abstracts.floatn.Float3;
import de.linusdev.lutils.math.vector.abstracts.floatn.Float4;
import de.linusdev.lutils.math.vector.abstracts.floatn.FloatN;
import org.jetbrains.annotations.NotNull;

public interface FloatMxN extends Matrix, FloatN {

    float get(int y, int x);

    void put(int y, int x, float value);

    default @NotNull Float2 createFloat2View(
            int y1, int x1,
            int y2, int x2
    ) {
        return Float2.createView(this, new int[]{
                positionToIndex(y1, x1),
                positionToIndex(y2, x2)
        });
    }

    default @NotNull Float3 createFloat3View(
            int y1, int x1,
            int y2, int x2,
            int y3, int x3
    ) {
        return Float3.createView(this, new int[]{
                positionToIndex(y1, x1),
                positionToIndex(y2, x2),
                positionToIndex(y3, x3)
        });
    }

    default @NotNull Float4 createFloat4View(
            int y1, int x1,
            int y2, int x2,
            int y3, int x3,
            int y4, int x4
    ) {
        return Float4.createView(this, new int[]{
                positionToIndex(y1, x1),
                positionToIndex(y2, x2),
                positionToIndex(y3, x3),
                positionToIndex(y4, x4)
        });
    }

}
