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

package de.linusdev.lutils.math.vector.abstracts.intn;

import de.linusdev.lutils.math.vector.abstracts.vectorn.Vector4;

@SuppressWarnings("unused")
public interface Int4 extends IntN, Vector4 {

    default int x() {
        return get(0);
    }

    default int y() {
        return get(1);
    }

    default int z() {
        return get(2);
    }

    default int w() {
        return get(3);
    }

    default void x(int f) {
        put(0, f);
    }

    default void y(int f) {
        put(1, f);
    }

    default void z(int f) {
        put(2, f);
    }

    default void w(int f) {
        put(3, f);
    }
    
}
