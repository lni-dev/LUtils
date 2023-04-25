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
