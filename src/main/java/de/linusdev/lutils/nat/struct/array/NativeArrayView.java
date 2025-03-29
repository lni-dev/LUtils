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

package de.linusdev.lutils.nat.struct.array;

import de.linusdev.lutils.nat.array.NativeArray;
import de.linusdev.lutils.nat.struct.abstracts.Structure;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.nio.ByteBuffer;

public class NativeArrayView<T extends Structure> implements NativeArray<T> {

    private final @NotNull NativeArray<T> original;

    private final @NotNull ByteBuffer backingBuffer;
    private final int offset;
    private final int length;

    /**
     *
     * @param original array to view on
     * @param backingBuffer {@link ByteBuffer} backing this view. This buffer must start at the location of
     *                                        {@code original.get(offset)} and end before {@code original.get(offset + length)}.
     * @param offset startIndex (inclusive) to the first element of type {@link T} this view views on.
     * @param length length for how many elements of type {@link T} this view views on.
     */
    public NativeArrayView(
            @NotNull NativeArray<T> original,
            @NotNull ByteBuffer backingBuffer,
            int offset,
            int length
    ) {
        this.original = original;
        this.backingBuffer = backingBuffer;
        this.offset = offset;
        this.length = length;
    }


    @Override
    public T get(@Range(from = 0, to = Integer.MAX_VALUE) int index) {
        return original.get(offset + index);
    }

    @Override
    public void set(int index, T item) {
        original.set(offset + index, item);
    }

    @Override
    public int length() {
        return length;
    }

    @Override
    public boolean isInitialised() {
        return original.isInitialised();
    }

    @Override
    public ByteBuffer getByteBuffer() {
        return backingBuffer;
    }

    @Override
    public int getRequiredSize() {
        return backingBuffer.capacity();
    }

    @Override
    public int getAlignment() {
        return original.getAlignment();
    }
}
