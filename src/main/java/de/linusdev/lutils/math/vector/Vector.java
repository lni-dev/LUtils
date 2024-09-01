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

package de.linusdev.lutils.math.vector;

import de.linusdev.lutils.math.matrix.abstracts.floatn.FloatMxN;
import de.linusdev.lutils.math.vector.abstracts.floatn.FloatN;
import de.linusdev.lutils.math.vector.abstracts.intn.IntN;
import de.linusdev.lutils.math.vector.abstracts.longn.LongN;
import de.linusdev.lutils.nat.struct.abstracts.Structure;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;

public interface Vector {

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *                                                               *
     *                                                               *
     *                        To-String-Methods                      *
     *                                                               *
     *                                                               *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /**
     * {@link Object#toString() toString()} method for {@link Vector} subclasses.
     * @param vector the {@link Vector}
     * @param elementTypeName the element type name of the vector
     * @param getter getter to get an element at a specific index
     * @return nice string describing given {@code vector}
     * @param <T> {@link Vector} type.
     */
    static <T extends Vector> @NotNull String toString(
            @NotNull T vector,
            @NotNull String elementTypeName,
            @NotNull BiFunction<T, Integer, Object> getter
    ) {
        StringBuilder sb = new StringBuilder()
                .append(elementTypeName)
                .append(vector.getMemberCount())
                .append(vector.isView() ? "view" : "")
                .append("(")
                .append(getter.apply(vector, 0));

        for(int i = 1; i < vector.getMemberCount(); i++)
            sb.append(", ").append(getter.apply(vector, i));

        return sb.append(")").toString();
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *                                                               *
     *                                                               *
     *                         Equals-Methods                        *
     *                                                               *
     *                                                               *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /**
     *
     * @param vector {@link FloatN}
     * @param data a vector as float array
     * @param epsilon the maximum difference between each component. 0.0 for true equality
     * @return {@code true} if the difference of each component of {@code vector} and {@code data} is equal or smaller than
     * epsilon.
     */
    static boolean equals(@NotNull FloatN vector, float @NotNull [] data, float epsilon) {
        if(vector.getMemberCount() != data.length) return false;

        for(int i = 0; i < data.length; i++) {
            if(Math.abs(vector.get(i) - data[i]) > epsilon) {
                return false;
            }

        }

        return true;
    }

    /**
     *
     * @param vector {@link FloatN}
     * @param other {@link FloatN} to compare
     * @param epsilon the maximum difference between each component. 0.0 for true equality
     * @return {@code true} if the difference of each component of {@code vector} and {@code data} is equal or smaller than
     * epsilon.
     */
    static boolean equals(@NotNull FloatN vector, @NotNull FloatN other, float epsilon) {
        if(vector.getMemberCount() != other.getMemberCount()) return false;

        for(int i = 0; i < vector.getMemberCount(); i++) {
            if(Math.abs(vector.get(i) - other.get(i)) > epsilon)
                return false;
        }

        return true;
    }

    /**
     * @param vector {@link IntN}
     * @param other {@link IntN} to compare
     * @return {@code true} if each component of {@code vector} and {@code data} is equal.
     */
    static boolean equals(@NotNull IntN vector, @NotNull IntN other) {
        if(vector.getMemberCount() != other.getMemberCount()) return false;

        for(int i = 0; i < vector.getMemberCount(); i++) {
            if(vector.get(i) != other.get(i))
                return false;
        }

        return true;
    }

    /**
     * @param vector {@link LongN}
     * @param other {@link LongN} to compare
     * @return {@code true} if each component of {@code vector} and {@code data} is equal.
     */
    static boolean equals(@NotNull LongN vector, @NotNull LongN other) {
        if(vector.getMemberCount() != other.getMemberCount()) return false;

        for(int i = 0; i < vector.getMemberCount(); i++) {
            if(vector.get(i) != other.get(i))
                return false;
        }

        return true;
    }

    /**
     *
     * @param matrix {@link FloatMxN}
     * @param data a matrix as float array
     * @param epsilon the maximum difference between each component. 0.0 for true equality
     * @return {@code true} if the difference of each component of {@code matrix} and {@code data} is equal or smaller than
     * epsilon.
     */
    static boolean equals(@NotNull FloatMxN matrix, float @NotNull [] data, float epsilon) {
        if((matrix.getWidth() * matrix.getHeight()) != data.length) return false;

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

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     *                                                               *
     *                                                               *
     *                         Vector-Methods                        *
     *                                                               *
     *                                                               *
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /**
     * Count of components in this vector.
     * @return float count in this vector
     */
    int getMemberCount();

    /**
     * Whether the component values of this vector should be interpreted as unsigned values.
     * @return {@code true} if the component values are unsigned.
     */
    default boolean areComponentsUnsigned() {
        return false;
    }

    /**
     * Whether this vector is array backed.
     * @return {@code true} if this vector is array backed.
     */
    boolean isArrayBacked();

    /**
     * Whether this vector is buffer backed. If this method returns {@code true},
     * {@link #getStructure()} will not throw an {@link UnsupportedOperationException}.
     * @return {@code true} if this vector is buffer backed.
     */
    boolean isBufferBacked();

    /**
     *
     * @return this vector as {@link Structure}
     * @throws UnsupportedOperationException if this vector is not {@link #isBufferBacked() buffer backed}.
     */
    default @NotNull Structure getStructure() {
        throw new UnsupportedOperationException("This vector is not buffer backed.");
    }

    /**
     * Whether this vector is only a view onto another vector.
     * @return {@code true} if this vector is a view.
     */
    boolean isView();

    /**
     * Used to avoid unsafe casting.
     * @return the vector itself, but as {@link View}.
     * @throws UnsupportedOperationException if this vector is not a {@link #isView() view}.
     */
    default @NotNull View<?> getAsView() {
        throw new UnsupportedOperationException("This vector is not a view on another vector.");
    }

    /**
     *
     * @param <V> {@link FloatN}, {@link IntN} or {@link LongN}
     */
    abstract class View<V extends Vector> implements Vector {

        /**
         * Used by {@link #isView() view vectors} to calculate the mapping directly to the original vectors if a view of
         * a view vector is created.
         * @param view the view vector, which the view should be created upon
         * @param mapping the mapping on the view vector
         * @return mapping directly to the original vector
         */
        static int @NotNull [] recalculateMappingToOriginal(@NotNull View<?> view, int @NotNull [] mapping) {
            int[] viewMapping = view.getMapping();
            int[] newMapping = new int[mapping.length];

            for(int i = 0; i < mapping.length; i++)
                newMapping[i] = viewMapping[mapping[i]];

            return newMapping;
        }

        /**
         * Checks if the {@link #getMapping() mapping} of given view is special.<br>
         * A non-special mapping is the following: mapping[0]=0, mapping[1]=1, ...<br>
         * or generally speaking: mapping[i]=i.<br>
         * Everything else is a special mapping.
         * @see #getMapping()
         * @see #isMappingSpecial(int[], Vector) 
         * @param view {@link Vector}, which must be a {@link #isView() view}.
         * @return {@code true} if the mapping of {@code view} is special
         */
        public static boolean isMappingSpecial(@NotNull Vector view) {
            int[] mapping = view.getAsView().getMapping();
            for(int i = 0; i < mapping.length; i++) {
                if(i != mapping[i])
                    return true;
            }

            return false;
        }

        /**
         * Checks if the {@link #getMapping() mapping} of given {@code viewToCheck} is special compared to the given
         * {@code defaultMapping}.
         * A non-special mapping is the following: mapping[0]=defaultMapping[0], mapping[1]=defaultMapping[1], ...<br>
         * or generally speaking: mapping[i]=defaultMapping[i].<br>
         * Everything else is a special mapping.
         * @param defaultMapping the defaultMapping
         * @param viewToCheck {@link Vector}, which must be a {@link #isView() view}.
         * @return {@code true} if the mapping of {@code viewToCheck} is special
         */
        public static boolean isMappingSpecial(int @NotNull [] defaultMapping, @NotNull Vector viewToCheck) {
            int[] mapping = viewToCheck.getAsView().getMapping();
            for(int i = 0; i < mapping.length; i++) {
                if(defaultMapping[i] != mapping[i])
                    return true;
            }

            return defaultMapping.length != mapping.length;
        }

        protected final @NotNull V original;
        protected final int @NotNull [] mapping;

        protected View(@NotNull V original, int @NotNull [] mapping) {
            if(original.isView()) {
                mapping = recalculateMappingToOriginal(original.getAsView(), mapping);
                //noinspection unchecked: original and view must have the same element type
                original = (V) original.getAsView().getOriginal();
            }

            this.original = original;
            this.mapping = mapping;
        }

        /**
         *
         * @return the original vector this vector views to
         */
        @NotNull
        public V getOriginal() {
            return original;
        }

        /**
         * The returned mapping must always map to a non view vector.
         * @return the mapping to the original vector
         * @throws UnsupportedOperationException if this vector is not {@link #isView() a view on another vector}.
         */
        public int @NotNull [] getMapping() {
            return mapping;
        }

        @Override
        public boolean areComponentsUnsigned() {
            return original.areComponentsUnsigned();
        }

        @Override
        public boolean isArrayBacked() {
            return false;
        }

        @Override
        public boolean isBufferBacked() {
            return false;
        }

        @Override
        public boolean isView() {
            return true;
        }

        /**
         * @return {@code true} if this view has a factor when getting/setting values.
         */
        abstract public boolean hasFactor();

        /**
         * The factor for each component. The factor[i] is used for the i-component of this view vector,
         * not on the original.<br>
         * The returned array will always be of the same type as the vector (FloatN -> float[], ...).
         * @return factor array
         */
        public @NotNull Object getFactor() {
            throw new UnsupportedOperationException("This view has no factor");
        }

        @Override
        public @NotNull View<V> getAsView() {
            return this;
        }
    }

}
