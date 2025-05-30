/*
 * Copyright (c) 2023-2025 Linus Andera
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
 * Bitfield class using a long. It is not thread safe!
 * @param <V> field value
 */
@SuppressWarnings("unused")
public class LongBitfieldImpl<V extends LongBitFieldValue> implements LongBitfield<V> {

    private long value;

    public LongBitfieldImpl(@NotNull V flag) {
        this();
        set(flag);
    }

    public LongBitfieldImpl(@NotNull V flag1, @NotNull V flag2) {
        this();
        set(flag1, flag2);
    }

    public LongBitfieldImpl(@NotNull V flag1, @NotNull V flag2, @NotNull V flag3) {
        this();
        set(flag1, flag2, flag3);
    }

    public LongBitfieldImpl(@NotNull V flag1, @NotNull V flag2, @NotNull V flag3, @NotNull V flag4) {
        this();
        set(flag1, flag2, flag3, flag4);
    }

    @SafeVarargs
    public LongBitfieldImpl(@NotNull V @NotNull ... flags) {
        this();
        set(flags);
    }

    public LongBitfieldImpl(long value) {
        this.value = value;
    }

    public LongBitfieldImpl() {
        this.value = 0L;
    }

    /**
     * Get the value of this bitfield
     * @return bitfield as int
     */
    public long getValue() {
        return value;
    }

    public void replaceWith(long value) {
        this.value = value;
    }

    /**
     * reset this bitfield to 0
     */
    public void reset() {
        this.value = 0L;
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
    public boolean isSet(long flag) {
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
     * @param flags flag to set
     */
    @SafeVarargs
    public final void set(@NotNull V @NotNull... flags) {
        for(V flag : flags)
            value |= flag.getValue();
    }

    public void setFlag(long flag) {
        value |= flag;
    }

    public void unset(@NotNull V flag) {
        value &= ~flag.getValue();
    }

    public void unsetFlag(long flag) {
        value &= ~flag;
    }


}
