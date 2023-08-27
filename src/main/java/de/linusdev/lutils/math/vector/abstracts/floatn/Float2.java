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

import de.linusdev.lutils.math.vector.abstracts.vectorn.Vector2;
import org.jetbrains.annotations.NotNull;

public interface Float2 extends FloatN, Vector2 {

    default float x() {
        return get(0);
    }

    default float y() {
        return get(1);
    }

    default void x(float f) {
        put(0, f);
    }

    default void y(float f) {
        put(1, f);
    }

    default void xy(float x, float y) {
        put(0, x);
        put(1, y);
    }

    default Float2 yx() {
        return new Float2.View(this, new int[]{1, 0});
    }

    class View extends FloatN.View implements Float2 {
        protected View(@NotNull FloatN original, int @NotNull [] mapping) {
            super(original, mapping);
        }
    }

}
