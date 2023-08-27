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

import de.linusdev.lutils.math.vector.Vector;
import org.jetbrains.annotations.NotNull;

public interface FloatN extends Vector {

    @NotNull String ELEMENT_TYPE_NAME = "float";
    int ELEMENT_SIZE = Float.BYTES;

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
    @NotNull
    default FloatN getOriginal() {
        throw new UnsupportedOperationException();
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
        public String toString() {
            return Vector.toString(this, ELEMENT_TYPE_NAME, FloatN::get);
        }
    }
}
