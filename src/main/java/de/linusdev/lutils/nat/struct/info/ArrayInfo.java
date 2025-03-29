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

package de.linusdev.lutils.nat.struct.info;

import org.jetbrains.annotations.NotNull;

public class ArrayInfo extends StructureInfo {

    /**
     * The length of the array (Amount of elements inside the array).
     */
    protected final int length;

    /**
     * The stride (size of one element including padding) of the array or {@code -1}
     * if stride varies for different positions.
     */
    protected final int stride;

    /**
     * The position in bytes of each array element.
     * @see ArrayPositionFunction#position(int) position function
     */
    protected final @NotNull ArrayPositionFunction positions;

    /**
     *
     * @param alignment see {@link StructureInfo#StructureInfo(int, boolean, int, int[])}
     * @param compressed see {@link StructureInfo#StructureInfo(int, boolean, int, int[])}
     * @param size see {@link StructureInfo#StructureInfo(int, boolean, int, int[])}
     * @param sizes see {@link StructureInfo#StructureInfo(int, boolean, int, int[])}
     * @param length see {@link #length}
     * @param positions see {@link #positions}
     */
    public ArrayInfo(
            int alignment,
            boolean compressed,
            int size,
            int @NotNull [] sizes,
            int length,
            int stride,
            @NotNull ArrayPositionFunction positions
    ) {
        super(alignment, compressed, size, sizes);
        this.length = length;
        this.stride = stride;
        this.positions = positions;
    }

    /**
     * @see #length
     */
    public int getLength() {
        return length;
    }

    /**
     * @see #positions
     */
    public @NotNull ArrayPositionFunction getPositions() {
        return positions;
    }

    /**
     * @see #stride
     */
    public int getStride() {
        return stride;
    }

    /**
     * Functional interface with the function {@link #position(int)}.
     * @see #position(int)
     */
    @FunctionalInterface
    public interface ArrayPositionFunction {
        /**
         * @param index the index of the element, whose starting position shall be acquired.
         * @return starting position (in number of bytes) of element with given {@code index}.
         */
        int position(int index);
    }
}
