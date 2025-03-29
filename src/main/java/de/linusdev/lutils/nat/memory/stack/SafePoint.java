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

package de.linusdev.lutils.nat.memory.stack;

import de.linusdev.lutils.nat.struct.abstracts.Structure;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

/**
 * @see Stack#safePoint()
 */
public interface SafePoint extends Stack, AutoCloseable {

    @NotNull Stack getStack();

    /**
     * Check and discard this safe point. May only be called once.
     */
    @Override
    void close();

    @Override
    default <T extends Structure> T push(@NotNull T structure) {
        return getStack().push(structure);
    }

    @Override
    default void pop() {
        getStack().pop();
    }

    @Override
    @NotNull
    default SafePoint safePoint() {
        return getStack().safePoint();
    }

    @Override
    default @NotNull PopPoint popPoint() {
        return getStack().popPoint();
    }

    @Override
    default @NotNull ByteBuffer pushByteBuffer(int size, int alignment) {
        return getStack().pushByteBuffer(size, alignment);
    }

    @Override
    default long memorySize() {
        return getStack().memorySize();
    }

    @Override
    default long usedByteCount() {
        return getStack().usedByteCount();
    }

    @Override
    default int currentStructCount() {
        return getStack().currentStructCount();
    }

    @Override
    default boolean isInitialised() {
        return getStack().isInitialised();
    }

    @Override
    default ByteBuffer getByteBuffer() {
        return getStack().getByteBuffer();
    }

    @Override
    default int getRequiredSize() {
        return getStack().getRequiredSize();
    }

    @Override
    default int getAlignment() {
        return getStack().getAlignment();
    }
}
