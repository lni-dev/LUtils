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

package de.linusdev.lutils.bitfield;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public interface IntBitfield<V extends IntBitFieldValue> {

    /**
     * Creates a new bitfield with the same flags set as {@code toCopy}.
     */
    @Contract("_ -> new")
    static <V extends IntBitFieldValue> @NotNull IntBitfield<V> copy(@NotNull IntBitfield<V> toCopy) {
        return new IntBitfieldImpl<>(toCopy.getValue());
    }

    /**
     * Get the value of this bitfield
     * @return bitfield as int
     */
    int getValue();

    /**
     * Replace the current value of this bitfield with given {@code value}.
     * @param value new value for this bitfield.
     */
    void replaceWith(int value);

    /**
     * Replace the current value of this bitfield with given {@code flags.getValue()}. So that
     * the flags in this bitfield match the flags of given {@code flags}.
     * @param flags flags to replace current flags with.
     */
    default void replaceWith(@NotNull IntBitfield<V> flags) {
        replaceWith(flags.getValue());
    }

    /**
     * reset this bitfield to 0 (not flags set)
     */
    @Contract("-> this")
    default IntBitfield<V> reset() {
        replaceWith(0);
        return this;
    }

    /**
     * checks if given flag is set.
     * @param flag flag to check
     * @return {@code true} if given flag is set in this bitfield. If {@code flag} is {@code 0} or not set in this
     * bitfield, {@code false} is returned.
     */
    default boolean isSet(int flag) {
        return flag != 0 && (getValue() & flag) == flag;
    }

    /**
     * checks if given flag is set.
     * @param flag flag to check
     * @return {@code true} if given flag is set in this bitfield.
     */
    default boolean isSet(@NotNull V flag) {
        return isSet(flag.getValue());
    }

    /**
     * Checks if all of given {@code flags} are set.
     * @param flags flags to check if they are set
     * @return {@code true} if all flags set in {@code flags} are set in this bitfield.
     */
    default boolean isSet(@NotNull IntBitfield<V> flags) {
        return isSet(flags.getValue());
    }

    /**
     * Set all bits/flags in given {@code flag}.
     * @param flag flag(s) to set
     */
    default void setFlag(int flag) {
        replaceWith(getValue() | flag);
    }

    /**
     * Sets given flag(s) to 1.
     * @param flag flag to set
     */
    default void set(@NotNull V flag) {
        setFlag(flag.getValue());
    }

    /**
     * Sets given flag(s) to 1.
     * @param flag1 flag to set
     */
    default void set(@NotNull V flag1, @NotNull V flag2) {
        setFlag(flag1.getValue());
        setFlag(flag2.getValue());
    }

    /**
     * Sets given flag(s) to 1.
     * @param flag1 flag to set
     */
    default void set(@NotNull V flag1, @NotNull V flag2, @NotNull V flag3) {
        setFlag(flag1.getValue());
        setFlag(flag2.getValue());
        setFlag(flag3.getValue());
    }

    /**
     * Sets given flag(s) to 1.
     * @param flag1 flag to set
     */
    default void set(@NotNull V flag1, @NotNull V flag2, @NotNull V flag3, @NotNull V flag4) {
        setFlag(flag1.getValue());
        setFlag(flag2.getValue());
        setFlag(flag3.getValue());
        setFlag(flag4.getValue());
    }

    /**
     * Sets given flag(s) to 1.
     * @param flags flags to set
     */
    default void set(@NotNull V @NotNull [] flags) {
        for(V flag : flags)
            setFlag(flag.getValue());
    }

    /**
     * Sets given flag to 0.
     * @param flag flag to set
     */
    default void unset(@NotNull V flag) {
        unsetFlag(flag.getValue());
    }

    /**
     * removes all flags contained in given {@code flag}. This means, all bits that are set in given {@code flag}
     * will be {@code 0} in {@link #getValue()} directly after this method call.
     * @param flag flag to remove
     */
    default void unsetFlag(int flag) {
        replaceWith(getValue() & ~flag);
    }

    /**
     *
     * @param enumClass enum class to supply {@code values} array.
     * @return {@link List} of flags set.
     */
    default @NotNull List<@NotNull V> toList(@NotNull Class<V> enumClass) {
        ArrayList<V> list = new ArrayList<>(Integer.bitCount(getValue()));

        for (V c : enumClass.getEnumConstants())
            if(isSet(c)) list.add(c);

        return list;
    }

    /**
     * And operation with given {@code flags}
     * @return {@code this & flags}
     */
    @Contract("_ -> this")
    default @NotNull IntBitfield<V> and(int flags) {
        replaceWith(getValue() & flags);
        return this;
    }

    /**
     * And operation with given {@code flags}
     * @return {@code this & flags}
     */
    @Contract("_ -> this")
    default @NotNull IntBitfield<V> and(@NotNull IntBitfield<V> flags) {
        and(flags.getValue());
        return this;
    }

    /**
     * Or operation with given {@code flags}
     * @return {@code this | flags}
     */
    @Contract("_ -> this")
    default @NotNull IntBitfield<V> or(int flags) {
        replaceWith(getValue() | flags);
        return this;
    }

    /**
     * Or operation with given {@code flags}
     * @return {@code this | flags}
     */
    @Contract("_ -> this")
    default @NotNull IntBitfield<V> or(@NotNull IntBitfield<V> flags) {
        or(flags.getValue());
        return this;
    }

}
