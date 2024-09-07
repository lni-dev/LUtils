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

package de.linusdev.lutils.math.vector.abstracts.floatn;


import de.linusdev.lutils.math.vector.abstracts.vectorn.Vector4;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("SpellCheckingInspection")
public interface Float4 extends FloatN, Vector4 {

    default float x() {
        return get(0);
    }

    default float y() {
        return get(1);
    }

    default float z() {
        return get(2);
    }

    default float w() {
        return get(3);
    }

    default void x(float f) {
        put(0, f);
    }

    default void y(float f) {
        put(1, f);
    }

    default void z(float f) {
        put(2, f);
    }

    default void w(float f) {
        put(3, f);
    }

    default void xyz(float x, float y, float z) {
        put(0, x);
        put(1, y);
        put(2, z);
    }

    default void xyz(@NotNull Float3 other) {
        //first get then set, so it even works if other is a view vector on this
        float x = other.x();
        float y = other.y();
        float z = other.z();
        put(0, x);
        put(1, y);
        put(2, z);
    }

    default void xyzw(float x, float y, float z, float w) {
        put(0, x);
        put(1, y);
        put(2, z);
        put(3, w);
    }

    default Float3 xyz() {
        return Float3.createView(this, new int[]{0,1,2});
    }

    default Float3 xxx() {
        return Float3.createView(this, new int[]{0,0,0});
    }

    default Float4 wzyx() {
        return createView(this, new int[]{3,2,1,0});
    }

    default @NotNull Float4 createFactorizedView(float factorX, float factorY, float factorZ, float factorW) {
        return createView(this,
                new int[]{0, 1, 2, 3},
                new float[]{factorX, factorY, factorZ, factorW}
        );
    }

    static @NotNull Float4 createView(@NotNull FloatN original, int @NotNull [] mapping) {
        return createView(original, mapping, null);
    }

    static @NotNull Float4 createView(@NotNull FloatN original, int @NotNull [] mapping, float @Nullable [] factor) {
        if((original.isView() && original.getAsView().hasFactor()) || factor != null) {
            return new Float4.FactorView(original, mapping, factor == null ? new float[]{1f,1f,1f,1f} : factor);
        }

        return new Float4.View(original, mapping);
    }

    class View extends FloatN.View implements Float4 {
        private View(@NotNull FloatN original, int @NotNull [] mapping) {
            super(original, mapping);
        }
    }

    class FactorView extends FloatN.FactorView implements Float4 {
        private FactorView(@NotNull FloatN original, int @NotNull [] mapping, float @NotNull [] factor) {
            super(original, mapping, factor);
        }
    }
}
