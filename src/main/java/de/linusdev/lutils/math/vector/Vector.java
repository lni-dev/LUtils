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

import de.linusdev.lutils.math.vector.abstracts.floatn.FloatN;
import de.linusdev.lutils.math.vector.abstracts.intn.IntN;
import de.linusdev.lutils.math.vector.abstracts.longn.LongN;
import de.linusdev.lutils.struct.abstracts.Structure;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;

public interface Vector {

    /**
     * Used by {@link #isView() view vectors} to calculate the mapping directly to the original vectors if a view of
     * a view vector is created.
     * @param view the view vector, which the view should be created upon
     * @param mapping the mapping on the view vector
     * @return mapping directly to the original vector
     */
    static int @NotNull [] recalculateMappingToOriginal(@NotNull Vector view, int @NotNull [] mapping) {
        int[] viewMapping = view.getMapping();
        int[] newMapping = new int[mapping.length];

        for(int i = 0; i < mapping.length; i++)
            newMapping[i] = viewMapping[mapping[i]];

        return newMapping;
    }

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

    /**
     * Count of components in this vector.
     * @return float count in this vector
     */
    int getMemberCount();

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
     *
     * @return the original vector this vector views to
     * @throws UnsupportedOperationException if this vector is not {@link #isView() a view on another vector}.
     */
    default @NotNull Vector getOriginal() {
        throw new UnsupportedOperationException("This vector is not a view vector.");
    }

    /**
     * The returned mapping must always map to a non view vector.
     * @return the mapping to the original vector
     * @throws UnsupportedOperationException if this vector is not {@link #isView() a view on another vector}.
     */
    default int @NotNull [] getMapping() {
        throw new UnsupportedOperationException("This vector is not buffer backed.");
    }

    /**
     *
     * @param <V> {@link FloatN}, {@link IntN} or {@link LongN}
     */
    abstract class View<V extends Vector> implements Vector {
        protected final @NotNull V original;
        protected final int @NotNull [] mapping;

        protected View(@NotNull V original, int @NotNull [] mapping) {
            if(original.isView()) {
                mapping = Vector.recalculateMappingToOriginal(original, mapping);
                //noinspection unchecked: original and view must have the same element type
                original = (V) original.getOriginal();
            }

            this.original = original;
            this.mapping = mapping;
        }

        @NotNull
        @Override
        public V getOriginal() {
            return original;
        }

        @Override
        public int @NotNull [] getMapping() {
            return mapping;
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
    }

}