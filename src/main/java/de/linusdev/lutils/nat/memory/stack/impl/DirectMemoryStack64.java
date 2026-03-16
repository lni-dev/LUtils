/*
 * Copyright (c) 2024-2026 Linus Andera
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

package de.linusdev.lutils.nat.memory.stack.impl;

import de.linusdev.lutils.nat.abi.ABI;
import de.linusdev.lutils.nat.abi.DefaultABIs;
import de.linusdev.lutils.nat.memory.NativeMemBuffer;
import de.linusdev.lutils.nat.memory.stack.PopPoint;
import de.linusdev.lutils.nat.memory.stack.SafePoint;
import de.linusdev.lutils.nat.memory.stack.Stack;
import de.linusdev.lutils.nat.size.Size;
import de.linusdev.lutils.nat.struct.abstracts.Structure;
import de.linusdev.lutils.nat.struct.generator.StaticGenerator;
import de.linusdev.lutils.nat.struct.info.StructureInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class DirectMemoryStack64 extends Structure implements Stack {

    public static final int DEFAULT_MEMORY_SIZE = 1024 * 1024; // 1 MiB
    public static final int ALIGNMENT = 8;

    private final long address;
    protected final @NotNull StackPointerQueue stackPointers;

    public DirectMemoryStack64(@NotNull Size size) {
        this(size.getAsInt());
    }

    public DirectMemoryStack64() {
        this(DEFAULT_MEMORY_SIZE);
    }

    public DirectMemoryStack64(long size) {
        super(null);
        setInfo(new StructureInfo(DefaultABIs.MSVC_X64, ALIGNMENT, false, 0, size, 0));
        //TODO allocate
        this.address = getPointer();
        this.stackPointers = new StackPointerQueue(address);
    }

    @Override
    public <T extends Structure> T push(@NotNull T structure) {
        long stackPointer = stackPointers.getStackPointer();

        StructureInfo info = structure.getOrGenerateInfo();
        long size = info.getRequiredSize();
        int alignment = info.getAlignment();
        int alignmentFix = stackPointer % alignment == 0 ? 0 : (int) (alignment - (stackPointer % alignment));
        long offset = (stackPointer - address) + alignmentFix;

        nativeMem.fill(offset, size, (byte) 0);
        structure.claimMemory(nativeMem, offset);

        stackPointers.push(size + alignmentFix);

        return structure;
    }

    @Override
    public @NotNull NativeMemBuffer pushNativeMemBuffer(long size, int alignment) {
        long stackPointer = stackPointers.getStackPointer();
        int alignmentFix = stackPointer % alignment == 0 ? 0 : (int) (alignment - (stackPointer % alignment));

        return NativeMemBuffer.of(stackPointer + alignmentFix, size, nativeMem.byteOrder());
    }

    @Override
    public void pop() {
        stackPointers.pop();
    }

    @Override
    public @NotNull SafePoint safePoint() {
        return stackPointers.safePoint(this);
    }

    @Override
    public @NotNull PopPoint popPoint() {
        return stackPointers.popPoint(this);
    }

    @Override
    public long memorySize() {
        return nativeMem.size();
    }

    @Override
    public long usedByteCount() {
        return stackPointers.getStackPointer() - address;
    }

    @Override
    public int currentStructCount() {
        return stackPointers.size();
    }

    @Override
    public int getAlignment() {
        return ALIGNMENT;
    }

    public long getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return info();
    }

    // Only for completion's Sake. Cannot actually be called, as no unallocated Stack can be created.
    public static final StaticGenerator GENERATOR = new StaticGenerator() {

        @Override
        public @NotNull StructureInfo calculateInfo(@NotNull Class<?> selfClazz, @Nullable ABI abi, int @Nullable [] length, @NotNull Class<?> @Nullable [] elementTypes) {
            int size = length == null ? DEFAULT_MEMORY_SIZE : length[0];
            return new StructureInfo(DefaultABIs.DEFAULT, 8, false, size, new long[]{0, size, 0});
        }
    };
}
