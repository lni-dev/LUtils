/*
 * Copyright (c) 2023 Linus Andera
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

import org.jetbrains.annotations.NotNull;

/**
 * Bitfield class using an int. It is not thread safe!
 * @param <V> field value
 */
@SuppressWarnings("unused")
public class IntBitfield<V extends IntBitFieldValue> {

    private int value;

    public IntBitfield(@NotNull V flag) {
        this();
        set(flag);
    }

    public IntBitfield(@NotNull V flag1, @NotNull V flag2) {
        this();
        set(flag1, flag2);
    }

    public IntBitfield(@NotNull V flag1, @NotNull V flag2, @NotNull V flag3) {
        this();
        set(flag1, flag2, flag3);
    }

    public IntBitfield(@NotNull V flag1, @NotNull V flag2, @NotNull V flag3, @NotNull V flag4) {
        this();
        set(flag1, flag2, flag3, flag4);
    }

    @SafeVarargs
    public IntBitfield(@NotNull V @NotNull ... flags) {
        this();
        set(flags);
    }

    public IntBitfield(int value) {
        this.value = value;
    }

    public IntBitfield() {
        this.value = 0;
    }

    /**
     * Get the value of this bitfield
     * @return bitfield as int
     */
    public int getValue() {
        return value;
    }

    public void replaceWith(int value) {
        this.value = value;
    }

    /**
     * reset this bitfield to 0
     */
    public void reset() {
        this.value = 0;
    }

    /**
     * checks if given flag is set.
     * @param flag flag to check
     * @return {@code true} if given flag is set in this bitfield.
     */
    public boolean isSet(@NotNull V flag) {
        return (value & flag.getValue()) == flag.getValue();
    }

    /**
     * checks if given flag is set.
     * @param flag flag to check
     * @return {@code true} if given flag is set in this bitfield.
     */
    public boolean isSet(int flag) {
        return (value & flag) == flag;
    }

    /**
     * Sets given flag(s) to 1.
     * @param flag flag to set
     */
    public void set(@NotNull V flag) {
        value |= flag.getValue();
    }

    /**
     * Sets given flag(s) to 1.
     * @param flag1 flag to set
     */
    public void set(@NotNull V flag1, @NotNull V flag2) {
        value |= flag1.getValue() | flag2.getValue();
    }

    /**
     * Sets given flag(s) to 1.
     * @param flag1 flag to set
     */
    public void set(@NotNull V flag1, @NotNull V flag2, @NotNull V flag3) {
        value |= flag1.getValue() | flag2.getValue() | flag3.getValue();
    }

    /**
     * Sets given flag(s) to 1.
     * @param flag1 flag to set
     */
    public void set(@NotNull V flag1, @NotNull V flag2, @NotNull V flag3, @NotNull V flag4) {
        value |= flag1.getValue() | flag2.getValue() | flag3.getValue() | flag4.getValue();
    }

    /**
     * Sets given flag(s) to 1.
     * @param flags flags to set
     */
    @SafeVarargs
    public final void set(@NotNull V @NotNull... flags) {
        for(V flag : flags)
            value |= flag.getValue();
    }

    public void set(int flag) {
        value |= flag;
    }

    public void unset(@NotNull V flag) {
        value &= ~flag.getValue();
    }

    public void unset(int flag) {
        value &= ~flag;
    }


}

