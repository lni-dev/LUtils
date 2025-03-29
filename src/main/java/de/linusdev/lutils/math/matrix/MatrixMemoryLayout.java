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

package de.linusdev.lutils.math.matrix;

import de.linusdev.lutils.math.matrix.abstracts.floatn.FloatMxN;
import org.jetbrains.annotations.NotNull;

/**
 * Either {@link #ROW_MAJOR} or {@link #COLUMN_MAJOR}. Default layout is {@link #ROW_MAJOR}.
 */
public enum MatrixMemoryLayout {

    /**
     * Memory layout of a 3x3 matrix is {@code [a11 a12 a13 a21 a22 a23 a31 a32 a33]},
     * where {@code ayx = get(y, x)}. This means that the row-vectors (from left to right)
     * are stored in memory like this: {@code [row1, row2, row3, ...]}
     */
    ROW_MAJOR {
        @Override
        public int positionToIndex(int width, int height, int y, int x) {
            return y * width + x;
        }

        @Override
        public void fillOfRowMajorArray(@NotNull FloatMxN matrix, float[] data) {
            for (int i = 0; i < data.length; i++) {
                matrix.put(i, data[i]);
            }
        }
    },

    /**
     * Memory layout of a 3x3 matrix: {@code [a11 a21 a31 a12 a22 a32 a13 a23 a33]},
     * where {@code ayx = get(y, x)}. This means that the column-vectors (from top to bottom)
     * are stored in memory like this: {@code [col1, col2, col3, ...]}
     */
    COLUMN_MAJOR {
        @Override
        public int positionToIndex(int width, int height, int y, int x) {
            return x * height + y;
        }

        @Override
        public void fillOfRowMajorArray(@NotNull FloatMxN matrix, float[] data) {
            for (int x = 0; x < matrix.getWidth(); x++) {
                for (int y = 0; y < matrix.getHeight(); y++) {
                    matrix.put(y, x, data[ROW_MAJOR.positionToIndex(matrix.getWidth(), matrix.getHeight(), y, x)]);
                }
            }
        }
    },

    ;

    /**
     * Index given position corresponds to in this memory layout.
     */
    public abstract int positionToIndex(int width, int height, int y, int x);

    /**
     * @param matrix matrix to fill.
     * @param data array with row major layout. (3x3 example): [a11 a12 a13 a21 a22 a23 a31 a32 a33]
     */
    public abstract void fillOfRowMajorArray(@NotNull FloatMxN matrix, float[] data);

}
