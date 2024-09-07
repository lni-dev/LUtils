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

package de.linusdev.lutils.math.vector.abstracts.shortn;

import de.linusdev.lutils.math.elements.ShortElement;
import de.linusdev.lutils.math.vector.Vector;
import org.jetbrains.annotations.NotNull;

public interface ShortN extends Vector, ShortElement {

    /**
     * Get component at position {@code index}.
     * @param index index of the vector component to get
     * @return component value
     * @implNote No Error checking will be done by this method. Too large or small indices
     * may result in undefined behavior.
     */
    short get(int index);

    /**
     *  Set component at position {@code index} to {@code value}.
     * @param index index of the vector component to set
     * @param value value to set
     *  @implNote No Error checking will be done by this method. Too large or small indices
     *  may result in undefined behavior.
     */
    void put(int index, short value);

    @Override
    default @NotNull ShortN.View getAsView() {
        throw new UnsupportedOperationException("This vector is not a view on another vector.");
    }

    @SuppressWarnings("UnusedReturnValue")
    default @NotNull ShortN fillFromArray(short @NotNull [] data) {
        for(int x = 0; x < getMemberCount(); x++) {
            put(x, (data[x]));
        }

        return this;
    }

    abstract class View extends Vector.View<ShortN> implements ShortN {

        protected View(@NotNull ShortN original, int @NotNull [] mapping) {
            super(original, mapping);
        }

        @Override
        public short get(int index) {
            return original.get(mapping[index]);
        }

        @Override
        public void put(int index, short value) {
            original.put(mapping[index], value);
        }

        @Override
        public boolean hasFactor() {
            return false;
        }

        @Override
        public short @NotNull [] getFactor() {
            throw new UnsupportedOperationException("This view has no factor");
        }

        @Override
        public @NotNull ShortN.View getAsView() {
            return this;
        }

        @Override
        public String toString() {
            return Vector.toString(this, ELEMENT_TYPE_NAME, ShortN::get);
        }
    }

}
