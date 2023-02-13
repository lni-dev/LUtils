/*
 * Copyright (c) 2023 Linus Andera all rights reserved
 */

package de.linusdev.lutils.ansi.sgr;

import org.jetbrains.annotations.NotNull;

public interface SGRParameter {

    /**
     * The identifier of this parameter.
     * @return code
     */
    int getIdentifier();

    /**
     * How many arguments this parameter requires. Negative numbers for an unknown amount.
     * @return required number of parameters or any negative number.
     */
    int argumentCount();

    /**
     * Constructs a string for this {@link SGRParameter} with given parameters.
     * @param delimiter the delimiter used to separate parameters. Usually {@link SGR#SGR_PARAMETER_DELIMITER}.
     * @param params parameters for this SGR parameter
     * @return constructed SGR parameter string
     * @throws IllegalArgumentException if {@link #argumentCount()} is zero or positive and length of given {@code params}
     * is not the same as {@link #argumentCount()}
     */
    default @NotNull String construct(@NotNull String delimiter, @NotNull String  @NotNull ... params) {
        if(argumentCount() >= 0 && params.length != argumentCount())
            throw new IllegalArgumentException("This SGR parameter requires  " + argumentCount() + " parameter(s).");

        StringBuilder sb = new StringBuilder();
        sb.append(getIdentifier());

        for(String param : params)
            sb.append(delimiter).append(param);

        return sb.toString();
    }

}
