/*
 * Copyright (c) 2024 Linus Andera
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

package de.linusdev.lutils.math.vector.abstracts.doublen;

import de.linusdev.lutils.math.elements.DoubleElement;
import de.linusdev.lutils.math.vector.Vector;
import org.jetbrains.annotations.NotNull;

public interface DoubleN extends Vector, DoubleElement {

    /**
     * Get component at position {@code index}.
     * @param index index of the vector component to get
     * @return component value
     * @implNote No Error checking will be done by this method. Too large or small indices
     * may result in undefined behavior.
     */
    double get(int index);

    /**
     *  Set component at position {@code index} to {@code value}.
     * @param index index of the vector component to set
     * @param value value to set
     *  @implNote No Error checking will be done by this method. Too large or small indices
     *  may result in undefined behavior.
     */
    void put(int index, double value);

    @Override
    default @NotNull DoubleN.View getAsView() {
        throw new UnsupportedOperationException("This vector is not a view on another vector.");
    }

    @SuppressWarnings("UnusedReturnValue")
    default @NotNull DoubleN fillFromArray(double @NotNull [] data) {
        for(int x = 0; x < getMemberCount(); x++) {
            put(x, (data[x]));
        }

        return this;
    }

    abstract class View extends Vector.View<DoubleN> implements DoubleN {

        protected View(@NotNull DoubleN original, int @NotNull [] mapping) {
            super(original, mapping);
        }

        @Override
        public double get(int index) {
            return original.get(mapping[index]);
        }

        @Override
        public void put(int index, double value) {
            original.put(mapping[index], value);
        }

        @Override
        public boolean hasFactor() {
            return false;
        }

        @Override
        public double @NotNull [] getFactor() {
            throw new UnsupportedOperationException("This view has no factor");
        }

        @Override
        public @NotNull DoubleN.View getAsView() {
            return this;
        }

        @Override
        public String toString() {
            return Vector.toString(this, ELEMENT_TYPE_NAME, DoubleN::get);
        }
    }

    abstract class FactorView extends DoubleN.View implements DoubleN {

        /**
         * Used by {@link #isView() view vectors} to calculate the factor directly to the original vector if a view of
         * a view vector is created.
         * @param view the view vector, which the view should be created upon
         * @param mapping the mapping on the view vector
         * @param factor the factor on the view vector
         * @return factor directly to the original vector
         */
        static double @NotNull [] recalculateFactorToOriginal(
                @NotNull DoubleN.View view,
                int @NotNull [] mapping,
                double @NotNull [] factor
        ) {
            double[] viewFactor = view.getFactor();
            double[] newFactor = new double[factor.length];

            for(int i = 0; i < mapping.length; i++)
                newFactor[i] = viewFactor[mapping[i]] * factor[i];

            return newFactor;
        }

        protected final double @NotNull [] factor;

        protected FactorView(@NotNull DoubleN original, int @NotNull [] mapping, double @NotNull [] factor) {
            super(original, mapping);
            if(original.isView() && original.getAsView().hasFactor())
                this.factor = recalculateFactorToOriginal(original.getAsView(), mapping, factor);
            else
                this.factor = factor;

        }

        @Override
        public double get(int index) {
            return original.get(mapping[index]) * factor[index];
        }

        @Override
        public void put(int index, double value) {
            original.put(mapping[index], value / factor[index]);
        }

        @Override
        public boolean hasFactor() {
            return true;
        }

        @Override
        public double @NotNull [] getFactor() {
            return factor;
        }

    }

}
