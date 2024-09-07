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

package de.linusdev.lutils.math.matrix.buffer;

import de.linusdev.lutils.math.vector.buffer.BBVectorInfo;
import de.linusdev.lutils.nat.NativeType;
import de.linusdev.lutils.nat.struct.info.ArrayInfo;
import de.linusdev.lutils.nat.abi.ABI;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class BBMatrixInfo extends BBVectorInfo {

    @Contract("_, _, _, _ -> new")
    public static @NotNull BBMatrixInfo create(
            @NotNull ABI abi,
            int width, int height,
            @NotNull NativeType type
    ) {
        ArrayInfo arrayInfo = abi.calculateMatrixLayout(
                type,
                width,
                height
        );

        return new BBMatrixInfo(
                arrayInfo.getAlignment(),
                arrayInfo.getRequiredSize(),
                arrayInfo.getSizes(),
                arrayInfo.getLength(),
                arrayInfo.getStride(),
                arrayInfo.getPositions(),
                type,
                width,
                height
        );
    }

    protected final int width;
    protected final int height;

    public BBMatrixInfo(
            int alignment,
            int size,
            int @NotNull [] sizes,
            int length,
            int stride,
            @NotNull ArrayPositionFunction positions,
            @NotNull NativeType type,
            int width,
            int height
    ) {
        super(alignment, size, sizes, length, stride, positions, type);
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
