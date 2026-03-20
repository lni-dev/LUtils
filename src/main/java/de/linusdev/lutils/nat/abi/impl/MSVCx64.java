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
import de.linusdev.lutils.nat.abi.ABI;
import de.linusdev.lutils.nat.abi.Types;
import de.linusdev.lutils.nat.struct.info.ArrayInfo;
import de.linusdev.lutils.nat.struct.info.StructureInfo;
import de.linusdev.lutils.nat.struct.info.UnionInfo;
import org.jetbrains.annotations.NotNull;

/**
 * @see <a href="https://learn.microsoft.com/en-us/cpp/build/x64-software-conventions?view=msvc-170">MSVC x64 software conventions</a>
 */
public class MSVCx64 implements ABI, Types {

    public final static @NotNull MSVCx64 INSTANCE = new MSVCx64();

    private final @NotNull MemorySizeable POINTER64 = MemorySizeable.of(8);

    private MSVCx64() { }

    @Override
    public @NotNull String identifier() {
        return "MSVC x64";
    }

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

    @Override
    public @NotNull StructureInfo calculateStructureLayout(
            boolean compress,
            @NotNull MemorySizeable @NotNull ... children
    ) {

        int alignment = 1;
        long[] sizes = new long[children.length * 2 + 1];
        int padding = 0;
        long position = 0;
        int offset;

        for(int i = 0; i < children.length; ) {
            MemorySizeable child = children[i];

            if ((position % child.getAlignment()) != 0 && !compress) {
                offset = Math.toIntExact((child.getAlignment() - (position % child.getAlignment())));
                position += offset;
                padding += Math.toIntExact(offset);
                continue;
            }

            sizes[i * 2] = padding;
            sizes[i * 2 + 1] = child.getRequiredSize();
            position += child.getRequiredSize();
            padding = 0;
            alignment = Math.max(alignment, child.getAlignment());
            i++;
        }

        if(compress)
            alignment = 1;

        if(position % alignment != 0) {
            sizes[sizes.length - 1] = (alignment - (position % alignment));
            position += sizes[sizes.length - 1];
        }
        else sizes[sizes.length - 1] = 0;

        return new StructureInfo(this, alignment, compress, position, sizes);
    }

    @Override
    public @NotNull UnionInfo calculateUnionLayout(
            boolean compress,
            @NotNull MemorySizeable @NotNull ... children
    ) {

        int alignment = 1;
        int[] positions = new int[children.length];
        long size = 0;
        int postPadding = 0;

        for(int i = 0; i < children.length; i++) {
            MemorySizeable child = children[i];

            positions[i] = 0;
            alignment = Math.max(alignment, child.getAlignment());
            size = Math.max(size, child.getRequiredSize());
        }

        if(compress)
            alignment = 1;

        if(size % alignment != 0)
            postPadding = Math.toIntExact((alignment - (size % alignment)));

        return new UnionInfo(
                this,
                alignment, compress,
                0, size, postPadding,
                positions
        );
    }

    @Override
    public @NotNull ArrayInfo calculateArrayLayout(
            boolean compress,
            @NotNull MemorySizeable children,
            int length,
            long stride
    ) {
        int alignment = children.getAlignment();

        if(stride == -1) {
            stride = children.getRequiredSize();

            if(!compress && stride < alignment)
                stride = alignment;

            if(!compress && (stride % alignment) != 0) {
                stride += alignment - (stride % alignment);
            }
        }

        long finalStride = stride;
        return new ArrayInfo(
                this,
                alignment,
                compress,
                stride * length,
                new long[]{0, stride * length, 0},
                length,
                stride,
                index -> index * finalStride
        );
    }

    @Override
    public @NotNull Types types() {
        return this;
    }
}
