/*
 * Copyright (c) 2023 Linus Andera all rights reserved
 */

package de.linusdev.lutils.bitfield;

import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class LongBitfield<V extends LongBitFieldValue> {

    private long value;

    public LongBitfield(long value) {
        this.value = value;
    }

    public LongBitfield() {
        this.value = 0L;
    }

    public void replaceWith(long value) {
        this.value = value;
    }

    public void reset() {
        this.value = 0L;
    }

    public boolean isSet(@NotNull V flag) {
        return (value & flag.getValue()) == flag.getValue();
    }

    public boolean isSet(long flag) {
        return (value & flag) == flag;
    }

    public void set(@NotNull V flag) {
        value |= flag.getValue();
    }

    public void set(@NotNull V flag1, @NotNull V flag2) {
        value |= flag1.getValue() | flag2.getValue();
    }

    public void set(@NotNull V flag1, @NotNull V flag2, @NotNull V flag3) {
        value |= flag1.getValue() | flag2.getValue() | flag3.getValue();
    }

    public void set(@NotNull V flag1, @NotNull V flag2, @NotNull V flag3, @NotNull V flag4) {
        value |= flag1.getValue() | flag2.getValue() | flag3.getValue() | flag4.getValue();
    }

    @SafeVarargs
    public final void set(@NotNull V @NotNull... flags) {
        for(V flag : flags)
            value |= flag.getValue();
    }

    public void set(long flag) {
        value |= flag;
    }

    public void unset(@NotNull V flag) {
        value &= ~flag.getValue();
    }

    public void unset(long flag) {
        value &= ~flag;
    }


}
