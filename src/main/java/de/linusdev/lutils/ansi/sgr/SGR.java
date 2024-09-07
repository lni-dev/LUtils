/*
 * Copyright (c) 2023-2024 Linus Andera
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

/**
 * Select Graphic Rendition for Consoles. This class can {@link #construct() construct} control sequences to set display
 * attributes of consoles and terminals.<br>
 * <br>
 * Example usage:
 * <pre>{@code SGR sgr = new SGR(SGRParameters.FOREGROUND_RED);
 * System.out.println(sgr.construct() + "Some text in red." + SGR.reset());
 * System.out.println("Normal text.")}</pre>
 *
 * This will give the following output:<br>
 * <pre>  <span style="color:red;">Some text in red.</span>
 * Normal text</pre>
 *
 * @see <a href="https://en.wikipedia.org/wiki/ANSI_escape_code#SGR_(Select_Graphic_Rendition)_parameters">Wikipedia</a>
 */
public class SGR {

    public static final String SGR_START_CODE = "\033[";
    public static final String SGR_PARAMETER_DELIMITER = ";";
    public static final String SGR_END_CODE = "m";

    public static final @NotNull String RESET_CONSTRUCTED = new SGR(SGRParameters.RESET).construct();

    public static @NotNull String reset() {
        return RESET_CONSTRUCTED;
    }

    private final @NotNull String delimiter;
    private final @NotNull StringBuilder sb;

    /**
     *
     * @param delimiter delimiter used to seperated arguments.
     */
    public SGR(@NotNull String delimiter) {
        this.delimiter = delimiter;
        this.sb = new StringBuilder(SGR_START_CODE);
    }

    /**
     * same as {@link #SGR(String)} , with the {@code delimiter} {@link #SGR_PARAMETER_DELIMITER}.
     */
    public SGR() {
        this(SGR_PARAMETER_DELIMITER);
    }
    
    /**
     *
     * @param delimiter delimiter used to seperated arguments.
     * @param sgrParam first {@link SGRParameter}
     * @param params parameters for {@code sgrParam}
     */
    public SGR(@NotNull String delimiter, @NotNull SGRParameter sgrParam, @NotNull String @NotNull ... params) {
        this.delimiter = delimiter;
        this.sb = new StringBuilder(SGR_START_CODE);
        sb.append(sgrParam.construct(delimiter, params));
    }

    /**
     * same as {@link #SGR(String, SGRParameter, String[])}, with the {@code delimiter} {@link #SGR_PARAMETER_DELIMITER}.
     */
    public SGR(@NotNull SGRParameter sgrParam, @NotNull String @NotNull ... params) {
        this(SGR_PARAMETER_DELIMITER, sgrParam, params);
    }

    /**
     * Adds given {@link SGRParameter} to this {@link SGR}.
     * @param sgrParam {@link SGRParameter} to add.
     * @param params parameters for {@code sgrParam}
     * @return this
     */
    public SGR add(@NotNull SGRParameter sgrParam, @NotNull String @NotNull ... params) {
        if(sb.length() > SGR_START_CODE.length()) 
            sb.append(delimiter);
        sb.append(sgrParam.construct(delimiter, params));
        return this;
    }

    /**
     * Constructs the control sequence.
     * @return the constructed control sequence
     */
    public @NotNull String construct() {
        return this.sb + SGR_END_CODE;
    }

}
