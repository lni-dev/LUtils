package de.linusdev.lutils.bitfield;

import org.jetbrains.annotations.NotNull;

public interface IntBitfield<V extends IntBitFieldValue> {

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
     * reset this bitfield to 0 (not flags set)
     */
    default void reset() {
        replaceWith(0);
    }

    /**
     * checks if given flag is set.
     * @param flag flag to check
     * @return {@code true} if given flag is set in this bitfield.
     */
    default boolean isSet(int flag) {
        return (getValue() & flag) == flag;
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
     * Set all bits/flags in given {@code flag}.
     * @param flag flag(s) to set
     */
    default void set(int flag) {
        replaceWith(getValue() | flag);
    }

    /**
     * Sets given flag(s) to 1.
     * @param flag flag to set
     */
    default void set(@NotNull V flag) {
        set(flag.getValue());
    }

    /**
     * Sets given flag(s) to 1.
     * @param flag1 flag to set
     */
    default void set(@NotNull V flag1, @NotNull V flag2) {
        set(flag1.getValue());
        set(flag2.getValue());
    }

    /**
     * Sets given flag(s) to 1.
     * @param flag1 flag to set
     */
    default void set(@NotNull V flag1, @NotNull V flag2, @NotNull V flag3) {
        set(flag1.getValue());
        set(flag2.getValue());
        set(flag3.getValue());
    }

    /**
     * Sets given flag(s) to 1.
     * @param flag1 flag to set
     */
    default void set(@NotNull V flag1, @NotNull V flag2, @NotNull V flag3, @NotNull V flag4) {
        set(flag1.getValue());
        set(flag2.getValue());
        set(flag3.getValue());
        set(flag4.getValue());
    }

    /**
     * Sets given flag(s) to 1.
     * @param flags flags to set
     */
    default void set(@NotNull V @NotNull [] flags) {
        for(V flag : flags)
            set(flag.getValue());
    }

    /**
     * Sets given flag to 0.
     * @param flag flag to set
     */
    default void unset(@NotNull V flag) {
        unset(flag.getValue());
    }

    /**
     * removes all flags contained in given {@code flag}. This means, all bits that are set in given {@code flag}
     * will be {@code 0} in {@link #getValue()} directly after this method call.
     * @param flag flag to remove
     */
    default void unset(int flag) {
        replaceWith(getValue() & ~flag);
    }

}
