package de.linusdev.lutils.other.str;

import org.jetbrains.annotations.NotNull;

public interface ConstructableString {

    /**
     * Construct the string filling all replace-keys with given {@code resolver}.
     */
    @NotNull String construct(@NotNull ConstructableString.Resolver resolver);

    /**
     * {@code true} if this {@link ConstructableString} is constant. If {@code false} is returned
     * the {@link ConstructableString} may be not constant or constant.
     */
    boolean isConstant();

    /**
     * Used by {@link ConstructableString constructable strings} to resolve values to keys contained
     * in these strings.
     */
    interface Resolver {
        /**
         * Return resolved value for given {@code key}.
         */
        @NotNull String resolve(@NotNull String key);
    }
}
