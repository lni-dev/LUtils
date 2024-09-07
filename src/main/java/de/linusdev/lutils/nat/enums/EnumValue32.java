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

package de.linusdev.lutils.nat.enums;

import de.linusdev.lutils.other.UnknownConstantException;
import org.jetbrains.annotations.Blocking;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface EnumValue32<M extends NativeEnumMember32> {

    int DEFAULT_VALUE = 0;

    /**
     * Checks if both enum values are equal.
     * @return {@code true} if other is an instance of {@link EnumValue32} and not {@code null} and {@code that.get() == other.get()}.
     */
    static boolean equals(
            @NotNull EnumValue32<?> that,
            @Nullable Object other
    ) {
        if(other instanceof EnumValue32<?> otherEnumValue)
            return equals(that, otherEnumValue);
        return false;
    }

    /**
     * Checks if both enum values are equal.
     * @return {@code true} if other is not {@code null} and {@code that.get() == other.get()}.
     */
    static boolean equals(
            @NotNull EnumValue32<?> that,
            @Nullable EnumValue32<?> other
    ) {
        return other != null && that.get() == other.get();
    }

    /**
     * Set this enum value to given {@code value}.
     * @param value value to set
     */
    void set(int value);

    /**
     * Set this enum value to {@link NativeEnumMember32#getValue()}
     * or {@value DEFAULT_VALUE} if {@code value} is {@code null}.
     * @param value value to set or {@code null}
     */
    default void set(@Nullable M value) {
        if(value == null) set(DEFAULT_VALUE);
        else set(value.getValue());
    }

    /**
     * Set this enum value to given {@code value.get()}.
     * @param value value to set
     */
    default void set(@NotNull EnumValue32<M> value) {
        set(value.get());
    }

    /**
     * Get this enum value as int.
     */
    int get();

    /**
     * Get this enum value as enum constant. Iterates through all enum constants of given {@code enumClass} and returns
     * the one, whose {@link NativeEnumMember32#getValue()} corresponds to the {@link #get() value} of this enum value
     * @param enumClass The class of the enum {@link M}.
     * @return {@link M} as described above.
     * @throws UnknownConstantException if no enum constant of given {@code enumClass} matches the {@link #get() value} of this enum value.
     */
    @Blocking
    default @NotNull M get(@NotNull Class<M> enumClass) {
        int val = get();
        for (M c : enumClass.getEnumConstants()) {
            if(c.getValue() == val)
                return c;
        }

        throw new UnknownConstantException(val);
    }
}
