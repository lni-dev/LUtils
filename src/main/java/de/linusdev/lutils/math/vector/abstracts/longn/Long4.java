/*
 * Copyright (c) 2023-2025 Linus Andera
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

package de.linusdev.lutils.math.vector.abstracts.longn;

import de.linusdev.lutils.math.vector.abstracts.vectorn.Vector4;

@SuppressWarnings("unused")
public interface Long4 extends LongN, Vector4 {

    @Override
    default int getMemberCount() {
        return 4;
    }

    default long x() {
        return get(0);
    }

    default long y() {
        return get(1);
    }

    default long z() {
        return get(2);
    }

    default long w() {
        return get(3);
    }

    default void x(long f) {
        put(0, f);
    }

    default void y(long f) {
        put(1, f);
    }

    default void z(long f) {
        put(2, f);
    }

    default void w(long f) {
        put(3, f);
    }
    
}
