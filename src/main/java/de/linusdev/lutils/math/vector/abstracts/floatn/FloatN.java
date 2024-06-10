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

package de.linusdev.lutils.math.vector.abstracts.floatn;

import de.linusdev.lutils.math.elements.FloatElement;
import de.linusdev.lutils.math.vector.Vector;
import org.jetbrains.annotations.NotNull;

public interface FloatN extends Vector, FloatElement {

    /**
     * Get component at position {@code index}.
     * @param index index of the vector component to get
     * @return component value
     * @implNote No Error checking will be done by this method. Too large or small indices
     * may result in undefined behavior.
     */
    float get(int index);

    /**
     *  Set component at position {@code index} to {@code value}.
     * @param index index of the vector component to set
     * @param value value to set
     *  @implNote No Error checking will be done by this method. Too large or small indices
     *  may result in undefined behavior.
     */
    void put(int index, float value);

    @Override
    default @NotNull FloatN.View getAsView() {
        throw new UnsupportedOperationException("This vector is not a view on another vector.");
    }

    @SuppressWarnings("UnusedReturnValue")
    default @NotNull FloatN fillFromArray(float @NotNull [] data) {
        for(int x = 0; x < getMemberCount(); x++) {
            put(x, (data[x]));
        }

        return this;
    }

    abstract class View extends Vector.View<FloatN> implements FloatN {

        protected View(@NotNull FloatN original, int @NotNull [] mapping) {
            super(original, mapping);
        }

        @Override
        public float get(int index) {
            return original.get(mapping[index]);
        }

        @Override
        public void put(int index, float value) {
            original.put(mapping[index], value);
        }

        @Override
        public boolean hasFactor() {
            return false;
        }

        @Override
        public float @NotNull [] getFactor() {
            throw new UnsupportedOperationException("This view has no factor");
        }

        @Override
        public @NotNull FloatN.View getAsView() {
            return this;
        }

        @Override
        public String toString() {
            return Vector.toString(this, ELEMENT_TYPE_NAME, FloatN::get);
        }
    }

    abstract class FactorView extends View implements FloatN {

        /**
         * Used by {@link #isView() view vectors} to calculate the factor directly to the original vector if a view of
         * a view vector is created.
         * @param view the view vector, which the view should be created upon
         * @param mapping the mapping on the view vector
         * @param factor the factor on the view vector
         * @return factor directly to the original vector
         */
        static float @NotNull [] recalculateFactorToOriginal(
                @NotNull FloatN.View view,
                int @NotNull [] mapping,
                float @NotNull [] factor
        ) {
            float[] viewFactor = view.getFactor();
            float[] newFactor = new float[factor.length];

            for(int i = 0; i < mapping.length; i++)
                newFactor[i] = viewFactor[mapping[i]] * factor[i];

            return newFactor;
        }

        protected final float @NotNull [] factor;

        protected FactorView(@NotNull FloatN original, int @NotNull [] mapping, float @NotNull [] factor) {
            super(original, mapping);
            if(original.isView() && original.getAsView().hasFactor())
                this.factor = recalculateFactorToOriginal(original.getAsView(), mapping, factor);
            else
                this.factor = factor;

        }

        @Override
        public float get(int index) {
            return original.get(mapping[index]) * factor[index];
        }

        @Override
        public void put(int index, float value) {
            original.put(mapping[index], value / factor[index]);
        }

        @Override
        public boolean hasFactor() {
            return true;
        }

        @Override
        public float @NotNull [] getFactor() {
            return factor;
        }

    }

}
