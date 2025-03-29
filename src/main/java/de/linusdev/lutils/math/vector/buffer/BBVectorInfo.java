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

package de.linusdev.lutils.math.vector.buffer;

import de.linusdev.lutils.nat.NativeType;
import de.linusdev.lutils.nat.abi.ABI;
import de.linusdev.lutils.nat.struct.info.ArrayInfo;
import org.jetbrains.annotations.NotNull;

public class BBVectorInfo extends ArrayInfo {

    private final @NotNull NativeType type;

    public static @NotNull BBVectorInfo create(
            @NotNull ABI abi,
            int elementCount,
            @NotNull NativeType type
    ) {
        ArrayInfo arrayInfo = abi.calculateVectorLayout(
                type,
                elementCount
        );

        return new BBVectorInfo(
                arrayInfo.getAlignment(),
                arrayInfo.getRequiredSize(),
                arrayInfo.getSizes(),
                arrayInfo.getLength(),
                arrayInfo.getStride(),
                arrayInfo.getPositions(),
                type
        );
    }

    public BBVectorInfo(
            int alignment,
            int size,
            int @NotNull [] sizes,
            int length,
            int stride,
            @NotNull ArrayPositionFunction positions,
            @NotNull NativeType type
    ) {
        super(alignment, false, size, sizes, length, stride, positions);
        this.type = type;
    }

    public @NotNull NativeType getType() {
        return type;
    }
}
