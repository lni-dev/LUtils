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

import de.linusdev.lutils.math.vector.abstracts.vectorn.Vector1;

public interface Long1 extends LongN, Vector1 {

    @Override
    default int getMemberCount() {
        return 1;
    }

    default long get() {
        return get(0);
    }

    default void set(long f) {
        put(0, f);
    }
}
