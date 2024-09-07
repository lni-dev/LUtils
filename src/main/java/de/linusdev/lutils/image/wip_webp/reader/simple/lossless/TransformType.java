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

package de.linusdev.lutils.image.wip_webp.reader.simple.lossless;

import de.linusdev.lutils.nat.enums.NativeEnumMember32;
import de.linusdev.lutils.other.UnknownConstantException;
import org.jetbrains.annotations.NotNull;

public enum TransformType implements NativeEnumMember32 {

    PREDICTOR_TRANSFORM     (0),
    COLOR_TRANSFORM         (1),
    SUBTRACT_GREEN_TRANSFORM(2),
    COLOR_INDEXING_TRANSFORM(3),

    ;

    public static @NotNull TransformType of(int integer) {
        for (TransformType value : values()) {
            if(value.getValue() == integer)
                return value;
        }

        throw new UnknownConstantException(integer);
    }

    private final int value;

    TransformType(int value) {
        this.value = value;
    }

    @Override
    public int getValue() {
        return value;
    }
}
