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

package de.linusdev.lutils.nat.struct.info;

import de.linusdev.lutils.nat.MemorySizeable;
import de.linusdev.lutils.nat.struct.abstracts.Structure;
import org.jetbrains.annotations.NotNull;

/**
 * Class, which holds information like alignment and size of a structure.
 */
public class StructureInfo implements MemorySizeable {

    /**
     * @see #getAlignment()
     */
    protected final int alignment;

    /**
     * @see #isCompressed()
     */
    protected final boolean compressed;

    /**
     * @see #getRequiredSize()
     */
    protected final int size;

    /**
     * array of item sizes. Always alternating between padding and item.
     * First and last element are always a padding. Minimum array length: 3.
     * <br><br>
     * Layout: <br>
     * [0]: pad, [1]: size, [2]: pad, [3]: size, [4]: pad, [5]: size, ...
     */
    protected final int @NotNull [] sizes;

    /**
     * Manually create {@link StructureInfo}.
     * @param alignment alignment of the structure
     * @param compressed whether the structure is compressed, see {@link #isCompressed()}
     * @param prePadding padding before the {@link Structure}
     * @param size size of the actual {@link Structure} (without any padding)
     * @param postPadding padding after the {@link Structure}
     */
    public StructureInfo(int alignment, boolean compressed, int prePadding, int size, int postPadding) {
        this.alignment = alignment;
        this.compressed = compressed;
        this.sizes = new int[]{prePadding, size, postPadding};
        this.size = prePadding + size + postPadding;
    }

    /**
     * Manually create {@link StructureInfo}.
     * @param alignment alignment of the structure
     * @param compressed whether the structure is compressed, see {@link #isCompressed()}
     * @param size size of the actual {@link Structure} (without any padding)
     * @param sizes array of item sizes. see {@link #sizes}
     */
    public StructureInfo(int alignment, boolean compressed, int size, int @NotNull [] sizes) {
        this.alignment = alignment;
        this.compressed = compressed;
        this.sizes = sizes;
        this.size = size;
    }

    @Override
    public int getRequiredSize() {
        return size;
    }

    @Override
    public int getAlignment() {
        return alignment;
    }

    /**
     * Whether this {@link StructureInfo} is compressed. Compressed means, it should ignore
     * the size of the individual elements when aligning the structure.
     * @return {@code true} if it is compressed.
     */
    public boolean isCompressed() {
        return compressed;
    }

    /**
     * @return {@link #sizes}
     */
    public int @NotNull [] getSizes() {
        return sizes;
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder(
                this.getClass().getSimpleName() + "("
                        + "alignment= " + alignment
                        + ", size=" + size
                        + ", compressed=" + compressed
                + ") {\n");

        for (int i = 0; i < sizes.length; i++) {
            if(i % 2 == 0 && sizes[i]  != 0) {
                ret.append("\tpadding: ").append(sizes[i]).append(" bytes\n");
            } else if(i % 2 == 1) {
                ret.append("\titem: ").append(sizes[i]).append(" bytes\n");
            }
        }

        return ret + "}";
    }
}
