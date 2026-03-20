/*
 * Copyright (c) 2026 Linus Andera
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

package de.linusdev.lutils.nat.abi.impl;

import de.linusdev.lutils.nat.MemorySizeable;
import de.linusdev.lutils.nat.NativeType;
import de.linusdev.lutils.nat.abi.ABI;
import de.linusdev.lutils.nat.abi.Types;
import de.linusdev.lutils.nat.struct.info.ArrayInfo;
import de.linusdev.lutils.nat.struct.info.StructureInfo;
import de.linusdev.lutils.nat.struct.info.UnionInfo;
import org.jetbrains.annotations.NotNull;

/**
 * Used for OPEN_CL, but structure code in OpenCL must be generated with {@link de.linusdev.lutils.nat.struct.generator.StructCodeGenerator StructCodeGenerator}.
 */
public class CustomOpenCL implements ABI, Types {

    public final static @NotNull CustomOpenCL INSTANCE = new CustomOpenCL();

    private final @NotNull MemorySizeable POINTER64 = MemorySizeable.of(8);

    private CustomOpenCL() {}

    @Override
    public @NotNull ABI getAbi() {
        return this;
    }

    @Override
    public @NotNull MemorySizeable integer() {
        return int32();
    }

    @Override
    public @NotNull MemorySizeable pointer() {
        return POINTER64;
    }

    /**
     * Returns the alignment of the biggest {@link MemorySizeable} with the biggest alignment in given array.
     * The size will be at least {@code min} and at most {@code max}.
     * @param min minimum size
     * @param max maximum size
     * @param vars array of {@link MemorySizeable}
     * @return clamp(min, max, biggestStruct.getRequiredSize())
     */
    @SuppressWarnings("SameParameterValue")
    private int getBiggestStructAlignment(int min, int max, @NotNull MemorySizeable @NotNull ... vars) {
        int biggest = min;
        for(MemorySizeable structure : vars)
            biggest = Math.max(biggest, structure.getAlignment());
        return Math.min(max, biggest);
    }

    @Override
    public @NotNull String identifier() {
        return "Custom OpenCL";
    }

    @Override
    public @NotNull StructureInfo calculateStructureLayout(
            boolean compress,
            @NotNull MemorySizeable @NotNull ... children
    ) {
        int alignment = getBiggestStructAlignment(4, 16, children);
        long[] sizes = new long[children.length * 2 + 1];
        int padding = 0;
        long position = 0;
        int offset;

        for(int i = 0; i < children.length; ) {
            MemorySizeable structure = children[i];
            if((position % alignment) == 0 || alignment - (position % alignment) >= structure.getRequiredSize()) {

                long itemSize = structure.getRequiredSize();
                int itemAlignment = structure.getAlignment();
                if(!compress && (position % itemAlignment) != 0) {
                    offset = Math.toIntExact((itemAlignment - (position % itemAlignment)));
                    position += offset;
                    padding += offset;
                    continue;
                }

                sizes[i * 2] = padding;
                sizes[i * 2 + 1] = itemSize;
                position += itemSize;
                padding = 0;
                i++;
            } else {
                offset = Math.toIntExact((alignment - (position % alignment)));
                position += offset;
                padding += offset;
            }
        }

        if(position % alignment != 0) {
            sizes[sizes.length - 1] = (alignment - (position % alignment));
            position += sizes[sizes.length - 1];
        }
        else sizes[sizes.length - 1] = 0;

        return new StructureInfo(this, alignment, compress, position, sizes);
    }

    @Override
    public @NotNull UnionInfo calculateUnionLayout(boolean compress, @NotNull MemorySizeable @NotNull ... children) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public @NotNull ArrayInfo calculateArrayLayout(boolean compress, @NotNull MemorySizeable children, int length, long stride) {
        return MSVCx64.INSTANCE.calculateArrayLayout(
                compress,
                children,
                length,
                stride
        );
    }

    @Override
    public @NotNull ArrayInfo calculateVectorLayout(@NotNull NativeType componentType, int length) {
        // OpenCL's specification states:
        // "A built-in data type that is not a power of two bytes in size must be aligned to the next larger
        // power of two. This rule applies to built-in types only, not structs or unions."
        // "For 3-component vector data types, the size of the data type is 4 * sizeof(component). This means
        // that a 3-component vector data type will be aligned to a 4 * sizeof(component) boundary."

        int componentSize = Math.toIntExact(componentType.getMemorySizeable(this).getRequiredSize());
        int originalSize = componentSize * length;
        int size = originalSize;

        if( Integer.bitCount(size) != 1) {
            //expand size to the next power of 2
            size = Integer.highestOneBit(size) << 1;
        }

        return new ArrayInfo(
                this,
                size,
                false,
                size,
                new long[]{0, originalSize, size-originalSize},
                length,
                componentSize,
                index -> (long) index * componentSize
        );
    }

    @Override
    public @NotNull Types types() {
        return this;
    }
}
