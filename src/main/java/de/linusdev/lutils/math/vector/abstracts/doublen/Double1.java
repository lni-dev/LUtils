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

package de.linusdev.lutils.math.vector.abstracts.doublen;

import de.linusdev.lutils.math.vector.abstracts.vectorn.Vector1;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Double1 extends DoubleN, Vector1 {

    default double get() {
        return get(0);
    }

    default void set(double f) {
        put(0, f);
    }

    default @NotNull Double1 createFactorizedView(double factor) {
        return createView(this,
                new int[]{0, 1},
                new double[]{factor}
        );
    }

    static @NotNull Double1 createView(@NotNull DoubleN original, int @NotNull [] mapping) {
        return createView(original, mapping, null);
    }

    static @NotNull Double1 createView(@NotNull DoubleN original, int @NotNull [] mapping, double @Nullable [] factor) {
        if((original.isView() && original.getAsView().hasFactor()) || factor != null) {
            return new Double1.FactorView(original, mapping, factor == null ? new double[]{1f,1f} : factor);
        }

        return new Double1.View(original, mapping);
    }

    class View extends DoubleN.View implements Double1 {
        private View(@NotNull DoubleN original, int @NotNull [] mapping) {
            super(original, mapping);
        }
    }

    class FactorView extends DoubleN.FactorView implements Double1 {
        private FactorView(@NotNull DoubleN original, int @NotNull [] mapping, double @NotNull [] factor) {
            super(original, mapping, factor);
        }
    }
}
