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

package de.linusdev.lutils.nat.pointer;

import de.linusdev.lutils.nat.NativeParsable;
import de.linusdev.lutils.nat.struct.abstracts.Structure;
import de.linusdev.lutils.nat.struct.array.StructureArray;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Typed pointer, that points to a {@link NativeParsable}. Similar to {@code T*} in cpp.
 * @param <T> type of pointer
 */
public interface TypedPointer64<T extends NativeParsable> extends Pointer64 {

    static <T extends NativeParsable> @NotNull TypedPointer64<T> of(long pointer) {
        return new TypedPointer64Impl<>(pointer);
    }

    /**
     * Create a new {@link TypedPointer64}, whose {@link #get()} method will return the value of {@link NativeParsable#getPointer()}
     * or {@link #NULL_POINTER} if {@code obj} is {@code null}.
     */
    static <T extends NativeParsable> @NotNull TypedPointer64<T> of(@Nullable T obj) {
        return new TypedPointer64Impl<>(obj == null ? NULL_POINTER : obj.getPointer());
    }

    /**
     * Create a new {@link TypedPointer64}, whose {@link #get()} method will return the value of {@link NativeParsable#getPointer()}
     * or {@link #NULL_POINTER} if {@code obj} is {@code null}. Does not provide type safety.
     */
    static <T extends NativeParsable> @NotNull TypedPointer64<T> ofOther(@Nullable NativeParsable obj) {
        return new TypedPointer64Impl<>(obj == null ? NULL_POINTER : obj.getPointer());
    }

    /**
     * Create a new {@link TypedPointer64}, whose {@link #get()} method will return the value of {@link StructureArray#getPointer()}.
     * Thus, a {@link TypedPointer64} pointing to the first array element is returned. Which corresponds to a c-style array.
     */
    static <T extends Structure> @NotNull TypedPointer64<T> ofArray(@NotNull StructureArray<T> array) {
        return new TypedPointer64Impl<>(array.getPointer());
    }

    /**
     * Same as {@link #of(T) of(T)}
     */
    static <T extends NativeParsable> @NotNull TypedPointer64<T> ref(@Nullable T obj) {
        return of(obj);
    }

    /**
     * Sets this pointers value, so that the {@link #get()} method will return the value of {@link NativeParsable#getPointer()}
     * or {@link #NULL_POINTER} if {@code obj} is {@code null}.
     */
    default void set(@Nullable T obj) {
        set(obj == null ? NULL_POINTER : obj.getPointer());
    }

    /**
     * Cast {@link T} to {@link U}.
     * @return a new {@link TypedPointer64}, whose type is {@link U}.
     * @param <U> new pointer type
     */
    default <U extends NativeParsable> @NotNull TypedPointer64<U> cast() {
        return of(get());
    }
}
