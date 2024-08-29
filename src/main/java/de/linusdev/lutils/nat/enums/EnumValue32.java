package de.linusdev.lutils.nat.enums;

import de.linusdev.lutils.other.UnknownConstantException;
import org.jetbrains.annotations.Blocking;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface EnumValue32<M extends NativeEnumMember32> {

    int DEFAULT_VALUE = 0;

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
