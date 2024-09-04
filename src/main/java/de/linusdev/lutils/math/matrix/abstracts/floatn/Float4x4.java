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
import de.linusdev.lutils.math.matrix.abstracts.floatn.min.MinFloat3x3;
import de.linusdev.lutils.math.matrix.abstracts.floatn.min.MinFloat4x4;
import org.jetbrains.annotations.NotNull;

/**
 * @see Matrix
 */
public interface Float4x4 extends Float4xN, MinFloat3x3, MinFloat4x4 {
    int HEIGHT = 4;

    @Override
    default int getHeight() {
        return HEIGHT;
    }

    default @NotNull Float3x3 createFloat3x3View(
            int y00, int x00,    int y01, int x01,    int y02, int x02,

            int y10, int x10,    int y11, int x11,    int y12, int x12,

            int y20, int x20,    int y21, int x21,    int y22, int x22
    ) {
        return new Float3x3.View(this, new int[] {
                y00,x00,    y01,x01,    y02,x02,

                y10,x10,    y11,x11,    y12,x12,

                y20,x20,    y21,x21,    y22,x22
        }, true);
    }

    class View extends FloatMxN.View implements Float4x4 {
        protected View(@NotNull FloatMxN original, int @NotNull [] mapping, boolean matrixPosMapping) {
            super(original, mapping, matrixPosMapping);
        }
    }
}
