/*
 * Copyright (c) 2023 Linus Andera all rights reserved
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

