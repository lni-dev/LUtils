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

package de.linusdev.lutils.nat.pointer;

import de.linusdev.lutils.nat.NativeParsable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Pointer64 {

    long NULL_POINTER = 0L;

    /**
     * Ensures, that given {@code pointer} is not {@link #NULL_POINTER}.
     * @param pointer pointer to check.
     * @param message message contained in the exception.
     * @return {@code true}
     * @throws IllegalArgumentException if given pointer is a {@link #NULL_POINTER}.
     */
    @Contract()
    static boolean requireNotNull(long pointer, @NotNull String message) {
        if(pointer == NULL_POINTER)
            throw new IllegalArgumentException(message);
        return true;
    }

    static @NotNull Pointer64 of(long pointer) {
        return new Pointer64Impl(pointer);
    }

    /**
     * Create a new {@link Pointer64}, whose {@link #get()} method will return the value of {@link NativeParsable#getPointer()}
     * or {@link #NULL_POINTER} if {@code obj} is {@code null}.
     */
    static @NotNull Pointer64 of(@Nullable NativeParsable obj) {
        return new Pointer64Impl(refL(obj));
    }

    /**
     * This method will return the value of {@link NativeParsable#getPointer()}
     * or {@link #NULL_POINTER} if {@code obj} is {@code null}.
     * @return pointer to {@code obj} as long.
     */
    static long refL(@Nullable NativeParsable obj) {
        return obj == null ? NULL_POINTER : obj.getPointer();
    }

    long get();

    void set(long pointer);

    /**
     *
     * @return {@code true} if this pointer points to null (0), {@code false} otherwise
     */
    default boolean isNullPtr() {
        return get() == NULL_POINTER;
    }

}
