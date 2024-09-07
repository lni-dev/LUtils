/*
 * Copyright (c) 2023-2024 Linus Andera
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
 * Note: Parameters of get and put methods are in reverse order (y first, x second). This is so
 * that it matches the mathematical description of a matrix (it still starts at 0 though).
 */
public interface Matrix extends Vector {

    /**
     *
     * @param matrix {@link FloatMxN}
     * @param data a {@link MatrixMemoryLayout#ROW_MAJOR row major} matrix as float array
     * @param epsilon the maximum difference between each component. 0.0 for true equality
     * @return {@code true} if the difference of each component of {@code matrix} and {@code data} is equal or smaller than
     * epsilon.
     */
    static boolean equals(@NotNull FloatMxN matrix, float @NotNull [] data, float epsilon) {
        if((matrix.getWidth() * matrix.getHeight()) != data.length) {
            return false;
        }

        for(int y = 0; y < matrix.getHeight(); y++) {
            for(int x = 0; x < matrix.getWidth(); x++) {
                if(Math.abs(matrix.get(y, x) - data[(y * matrix.getWidth()) + x]) > epsilon)
                    return false;
            }
        }

        return true;
    }

    /**
     *
     * @param matrix {@link FloatMxN}
     * @param other {@link FloatMxN} to compare
     * @param epsilon the maximum difference between each component. 0.0 for true equality
     * @return {@code true} if the difference of each component of {@code matrix} and {@code data} is equal or smaller than
     * epsilon.
     */
    static boolean equals(@NotNull FloatMxN matrix, @NotNull FloatMxN other, float epsilon) {
        if(matrix.getWidth() != other.getWidth() || matrix.getHeight() != other.getHeight()) return false;

        for(int y = 0; y < matrix.getHeight(); y++) {
            for(int x = 0; x < matrix.getWidth(); x++) {
                if(Math.abs(matrix.get(y, x) - other.get(y, x)) > epsilon)
                    return false;
            }
        }

        return true;
    }

    /**
     * Checks if given {@code matrix} is an identity matrix. An identity matrix is a diagonal
     * matrix with every diagonal component being {@code 1.0}.
     * @param matrix matrix to check
     * @param epsilon the maximum difference between each component. 0.0 for a true identity
     * @return {@code true} if given {@code matrix} is an identity matrix.
     */
    static boolean isIdentity(@NotNull FloatMxN matrix, float epsilon) {
        for (int i = 0; i < matrix.getWidth(); i++) {
            for (int j = 0; j < matrix.getHeight(); j++) {
                if(i == j && Math.abs(matrix.get(i, j) - 1f) > epsilon) return false;
                if(i != j && Math.abs(matrix.get(i, j) - 0f) > epsilon) return false;
            }
        }

        return true;
    }

    @FunctionalInterface
    interface MatrixGetter<M extends Matrix> {
        @NotNull Object get(@NotNull M matrix, int y, int x);
    }

    static <M extends Matrix> @NotNull String toString(
            @NotNull M matrix,
            @NotNull String name,
            @NotNull MatrixGetter<M> getter
    ) {
        return Matrix.toString(matrix, name, getter, 2);
    }

    static <M extends Matrix> @NotNull String toString(
            @NotNull M matrix,
            @NotNull String name,
            @NotNull MatrixGetter<M> getter,
            int visibleDigits
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
                //noinspection MalformedFormatString
                sb.append(String.format(" % 10." + visibleDigits + "f ", ((Number) getter.get(matrix, y, x)).doubleValue()));
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
        return getMemoryLayout().positionToIndex(getWidth(), getHeight(), y, x);
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

    /**
     * Current {@link MatrixMemoryLayout}.
     * @throws UnsupportedOperationException if this matrix is a {@link View}.
     */
    @NotNull MatrixMemoryLayout getMemoryLayout();

    /**
     * Set the {@link MatrixMemoryLayout memory layout}. This method will not move
     * the actual data of the matrix. That means, that {@code get(y, x)} before this method call
     * may not equal to {@code get(y, x)} after this method call.
     */
    void setMemoryLayout(@NotNull MatrixMemoryLayout layout);

    @Override
    default boolean isView() {
        return false;
    }

    interface View extends Matrix {

        /**
         * Converts a matrix-position-based mapping to an index-based mapping (which is used by all views).
         * A matrix-position-based mapping is a mapping with the following layout:<br>
         * <pre>{@code
         * mapping = new int[]{
         *                      y0,x0  ,  y0,x1,
         *                      y1,x0  ,  y1,x1
         *                    }
         * }</pre>
         * which maps <br>
         * {@code view.get(0,0)} to {@code original.get(y0,x0)},<br>
         * {@code view.get(0,1)} to {@code original.get(y0,x1)},<br>
         * {@code view.get(1,0)} to {@code original.get(y1,x0)}, ...<br>
         * for a 2x2 matrix.
         *
         * @param original original matrix
         * @param mapping matrix pos mapping
         * @return index based mapping
         */
        static int @NotNull [] matrixPosMappingToIndexMapping(
                @NotNull Matrix original, int @NotNull [] mapping
        ) {
            int[] indexMapping = new int[mapping.length / 2];

            for (int i = 0; i < indexMapping.length; i++) {
                indexMapping[i] = original.positionToIndex(mapping[i * 2], mapping[i * 2 + 1]);
            }

            return indexMapping;
        }

        @Override
        default int positionToIndex(int y, int x) {
            return MatrixMemoryLayout.ROW_MAJOR.positionToIndex(getWidth(), getHeight(), y, x);
        }
    }
}
