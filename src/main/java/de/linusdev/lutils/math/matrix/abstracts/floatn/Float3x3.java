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

package de.linusdev.lutils.math.matrix.abstracts.floatn;

import de.linusdev.lutils.math.matrix.abstracts.floatn.min.MinFloat3x3;
import org.jetbrains.annotations.NotNull;

public interface Float3x3 extends Float3xN, MinFloat3x3 {

    int HEIGHT = 3;

    @Override
    default int getHeight() {
        return HEIGHT;
    }

    class View extends FloatMxN.View implements Float3x3 {
        protected View(@NotNull FloatMxN original, int @NotNull [] mapping, boolean matrixPosMapping) {
            super(original, mapping, matrixPosMapping);
        }
    }
}
