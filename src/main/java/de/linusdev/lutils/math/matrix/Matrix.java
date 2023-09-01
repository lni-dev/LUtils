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

package de.linusdev.lutils.math.matrix;

import de.linusdev.lutils.math.vector.Vector;
import org.jetbrains.annotations.NotNull;

/**
 * MxN Matrix
 * <br><br>
 * N: {@link #getWidth() Width} of the Matrix<br>
 * M: {@link #getHeight() Height}  of the Matrix<br>
 * <br><br>
 * parameters of get and put methods:<br>
 * x: from 0 (inclusive) to N (exclusive)<br>
 * y: from 0 (inclusive) to M (exclusive)<br>
 */
public interface Matrix extends Vector {

    @FunctionalInterface
    interface MatrixGetter<M extends Matrix> {
        @NotNull Object get(@NotNull M matrix, int y, int x);
    }

    static <M extends Matrix> @NotNull String toString(
            @NotNull M matrix,
            @NotNull String name,
            @NotNull MatrixGetter<M> getter
    ) {
        StringBuilder sb = new StringBuilder();

        sb
                .append(name)
                .append(matrix.getWidth())
                .append("x")
                .append(matrix.getHeight())
                .append(":\n[\n");

        for(int y = 0; y < matrix.getHeight(); y++) {
            for(int x = 0; x < matrix.getWidth(); x++) {
                sb.append(String.format(" % 10.2f ", ((Number) getter.get(matrix, y, x)).doubleValue()));
            }
            if(y != matrix.getHeight()-1)
                sb.append("\n");
        }

        sb.append("\n]");

        return sb.toString();
    }

    @Override
    default int getMemberCount() {
        return getWidth() * getHeight();
    }

    /**
     * @param y y-pos
     * @param x x-pos
     * @return index of given position in this {@link Matrix}
     */
    default int positionToIndex(int y, int x) {
        return y * getWidth() + x;
    }

    /**
     * For a NxM Matrix, return N.
     * @return width of the matrix
     */
    int getWidth();

    /**
     * For a NxM Matrix, return M.
     * @return height of the matrix
     */
    int getHeight();

    /**
     * Whether this matrix is array backed.
     * @return {@code true} if this matrix is array backed.
     */
    @Override
    boolean isArrayBacked();

    /**
     * Whether this vector is buffer backed.
     * @return {@code true} if this vector is buffer backed.
     */
    @Override
    boolean isBufferBacked();

    @Override
    default boolean isView() {
        return false;
    }
}
