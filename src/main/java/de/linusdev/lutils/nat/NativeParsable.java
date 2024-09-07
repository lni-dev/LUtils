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

package de.linusdev.lutils.nat;

import de.linusdev.lutils.nat.struct.utils.BufferUtils;

import java.nio.ByteBuffer;

public interface NativeParsable extends MemorySizeable {

    /**
     * Whether this {@link NativeParsable} is already backed by a {@link #getByteBuffer() buffer}.
     * @return {@code true} if initialised
     */
    boolean isInitialised();

    /**
     * The {@link ByteBuffer} containing the native data. May not be {@code null} if
     * {@link #isInitialised()} is {@code true}.
     * @return {@link ByteBuffer}
     */
    ByteBuffer getByteBuffer();

    /**
     * Get the pointer to the buffer of this NativeParsable as long.
     * @return pointer to {@link #getByteBuffer()}
     */
    default long getPointer() {
        ByteBuffer buffer = getByteBuffer();
        return buffer == null ? 0L : BufferUtils.getHeapAddress(buffer);
    }
}
