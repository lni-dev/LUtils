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

/**
 * @see SGR
 */
public enum SGRParameters implements SGRParameter {

    /**
     * All attributes off.<br>
     * <br>IntelliJ Console: supported
     */
    RESET(0),
    /**
     * bold font.<br>
     * <br>IntelliJ Console: not supported
     */
    BOLD(2),
    /**
     * faint or light font.<br>
     * <br>IntelliJ Console: not supported
     */
    FAINT(2),
    /**
     * italic font. (Not widely supported)<br>
     * <br>IntelliJ Console: supported
     */
    ITALIC(3),
    /**
     * Underline.<br>
     * <br>IntelliJ Console: not tested
     */
    UNDERLINE(4),
    /**
     * Blinking less than 150 times per minute.<br>
     * <br>IntelliJ Console: not supported
     */
    SLOW_BLINk(5),
    /**
     * Blinking more than 150 times per minute.<br>
     * <br>IntelliJ Console: not tested
     */
    RAPID_BLINK(6),
    /**
     * Swap foreground and background colors.<br>
     * <br>IntelliJ Console: not tested
     */
    INVERT(7),
    /**
     * Not widely supported.<br>
     * <br>IntelliJ Console: not tested
     */
    CONCEAL(8),
    /**
     * Strike through.<br>
     * <br>IntelliJ Console: not tested
     */
    STRIKE(9),
    /**
     * Default font.<br>
     * <br>IntelliJ Console: not tested
     */
    PRIMARY(10),
    /**
     * Gothic font.<br>
     * <br>IntelliJ Console: not tested
     */
    GOTHIC(20),

    /**
     * Doubly underlined.<br>
     * Support is exclusive with {@link #NOT_BOLD}.<br>
     * <br>IntelliJ Console: not tested
     */
    DOUBLY_UNDERLINED(21),
    /**
     * Removes {@link #BOLD}.<br>
     * Support is exclusive with {@link #DOUBLY_UNDERLINED}.<br>
     * <br>IntelliJ Console: not tested
     */
    NOT_BOLD(21),

    /**
     * Removes {@link #BOLD} and {@link #FAINT}. May reset color.<br>
     * <br>IntelliJ Console: not tested
     */
    NORMAL_FONT_WEIGHT(22),
    /**
     * Removes {@link #ITALIC}.<br>
     * <br>IntelliJ Console: not tested
     */
    NOT_ITALIC_OR_BLACK_LETTER(23),
    /**
     * Removes {@link #UNDERLINE}.<br>
     * <br>IntelliJ Console: not tested
     */
    NOT_UNDERLINED(24),
    /**
     * Removes {@link #SLOW_BLINk} and {@link #RAPID_BLINK}.<br>
     * <br>IntelliJ Console: not tested
     */
    NOT_BLINKING(25),
    /**
     * Not widely supported.<br>
     * <br>IntelliJ Console: not tested
     */
    PROPORTIONAL_SPACING(26),
    /**
     * Not {@link #INVERT}.<br>
     * <br>IntelliJ Console: not tested
     */
    NOT_REVERSED(27),
    /**
     * Not {@link #CONCEAL}.<br>
     * <br>IntelliJ Console: not tested
     */
    REVEAL(28),
    /**
     * Not {@link #STRIKE}.<br>
     * <br>IntelliJ Console: not tested
     */
    NOT_STRIKE(29),

    /**
     * Sets foreground color to black.<br>
     * <br>IntelliJ Console: not tested
     */
    FOREGROUND_BLACK(30),
    /**
     * Sets foreground color to red.<br>
     * <br>IntelliJ Console: supported
     */
    FOREGROUND_RED(31),
    /**
     * Sets foreground color to green.<br>
     * <br>IntelliJ Console: not tested
     */
    FOREGROUND_GREEN(32),
    /**
     * Sets foreground color to yellow.<br>
     * <br>IntelliJ Console: not tested
     */
    FOREGROUND_YELLOW(33),
    /**
     * Sets foreground color to blue.<br>
     * <br>IntelliJ Console: not tested
     */
    FOREGROUND_BLUE(34),
    /**
     * Sets foreground color to magenta.<br>
     * <br>IntelliJ Console: not tested
     */
    FOREGROUND_MAGENTA(35),
    /**
     * Sets foreground color to cyan.<br>
     * <br>IntelliJ Console: not tested
     */
    FOREGROUND_CYAN(36),
    /**
     * Sets foreground color to white.<br>
     * <br>IntelliJ Console: not tested
     */
    FOREGROUND_WHITE(37),

    /**
     * Sets foreground color to given 8-bit color. First argument must be 5, second is the 8-bit color<br>
     * <br>IntelliJ Console: not tested
     */
    FOREGROUND_COLOR_8_BIT(38, 2) {
        @Override
        public @NotNull String construct(@NotNull String delimiter, @NotNull String @NotNull ... params) {
            if(params.length == 1)
                return super.construct(delimiter, "5", params[0]);
            return super.construct(delimiter, params);
        }
    },
    /**
     * Sets foreground color to given 24-bit color. First argument must be 2, second, third and fourth is red, green and blue respectively<br>
     * <br>IntelliJ Console: not tested
     */
    FOREGROUND_COLOR_24_BIT(38, 4) {
        @Override
        public @NotNull String construct(@NotNull String delimiter, @NotNull String @NotNull ... params) {
            if(params.length == 3)
                return super.construct(delimiter, "2", params[0], params[1], params[2]);
            return super.construct(delimiter, params);
        }
    },
    /**
     * Sets foreground color to the default foreground color<br>
     * <br>IntelliJ Console: not tested
     */
    FOREGROUND_DEFAULT_COLOR(39),

    /**
     * Sets background color to black.<br>
     * <br>IntelliJ Console: not tested
     */
    BACKGROUND_BLACK(40),
    /**
     * Sets background color to red.<br>
     * <br>IntelliJ Console: not tested
     */
    BACKGROUND_RED(41),
    /**
     * Sets background color to green.<br>
     * <br>IntelliJ Console: not tested
     */
    BACKGROUND_GREEN(42),
    /**
     * Sets background color to yellow.<br>
     * <br>IntelliJ Console: not tested
     */
    BACKGROUND_YELLOW(43),
    /**
     * Sets background color to blue.<br>
     * <br>IntelliJ Console: not tested
     */
    BACKGROUND_BLUE(44),
    /**
     * Sets background color to magenta.<br>
     * <br>IntelliJ Console: not tested
     */
    BACKGROUND_MAGENTA(45),
    /**
     * Sets background color to cyan.<br>
     * <br>IntelliJ Console: not tested
     */
    BACKGROUND_CYAN(46),
    /**
     * Sets background color to white.<br>
     * <br>IntelliJ Console: not tested
     */
    BACKGROUND_WHITE(47),
    /**
     * Sets background color to given 8-bit color. First argument must be 5, second is the 8-bit color<br>
     * <br>IntelliJ Console: not tested
     */
    BACKGROUND_COLOR_8_BIT(48, 2) {
        @Override
        public @NotNull String construct(@NotNull String delimiter, @NotNull String @NotNull ... params) {
            if(params.length == 1)
                return super.construct(delimiter, "5", params[0]);
            return super.construct(delimiter, params);
        }
    },
    /**
     * Sets background color to given 24-bit color. First argument must be 2, second, third and fourth is red, green and blue respectively<br>
     * <br>IntelliJ Console: not tested
     */
    BACKGROUND_COLOR_24_BIT(48, 4) {
        @Override
        public @NotNull String construct(@NotNull String delimiter, @NotNull String @NotNull ... params) {
            if(params.length == 3)
                return super.construct(delimiter, "2", params[0], params[1], params[2]);
            return super.construct(delimiter, params);
        }
    },
    /**
     * Sets background color to the default background color<br>
     * <br>IntelliJ Console: not tested
     */
    BACKGROUND_DEFAULT_COLOR(49),

    /**
     * Disables {@link #PROPORTIONAL_SPACING}.<br>
     * <br>IntelliJ Console: not tested
     */
    DISABLED_PROPORTIONAL_SPACING(50),
    /**
     * Framed.<br>
     * <br>IntelliJ Console: not tested
     */
    FRAMED(51),
    /**
     * Encircled.<br>
     * <br>IntelliJ Console: not tested
     */
    ENCIRCLED(52),
    /**
     * Overlined.<br>
     * <br>IntelliJ Console: not tested
     */
    OVERLINED(53),
    /**
     * Not {@link #FRAMED} nor {@link #ENCIRCLED}.<br>
     * <br>IntelliJ Console: not tested
     */
    NEITHER_FRAMED_NOR_ENCIRCLED(54),
    /**
     * Not {@link #OVERLINED}.<br>
     * <br>IntelliJ Console: not tested
     */
    NOT_OVERLINED(55),

    /**
     * Not in standard.<br>
     * <br>IntelliJ Console: not tested
     */
    SET_UNDERLINE_COLOR_8_BIT(58, 2) {
        @Override
        public @NotNull String construct(@NotNull String delimiter, @NotNull String @NotNull ... params) {
            if(params.length == 1)
                return super.construct(delimiter, "5", params[0]);
            return super.construct(delimiter, params);
        }
    },
    /**
     * Not in standard.<br>
     * <br>IntelliJ Console: not tested
     */
    SET_UNDERLINE_COLOR_24_BIT(58, 4) {
        @Override
        public @NotNull String construct(@NotNull String delimiter, @NotNull String @NotNull ... params) {
            if(params.length == 3)
                return super.construct(delimiter, "2", params[0], params[1], params[2]);
            return super.construct(delimiter, params);
        }
    },
    /**
     * Not in standard.<br>
     * <br>IntelliJ Console: not tested
     */
    DEFAULT_UNDERLINE_COLOR(59),

    /**
     * Rarely supported.<br>
     * <br>IntelliJ Console: not tested
     */
    IDEOGRAM_UNDERLINE(60),
    /**
     * Rarely supported.<br>
     * <br>IntelliJ Console: not tested
     */
    RIGHT_SIDE_LINE(60),
    /**
     * Rarely supported.<br>
     * <br>IntelliJ Console: not tested
     */
    IDEOGRAM_DOUBLE_UNDERLINE(61),
    /**
     * Rarely supported.<br>
     * <br>IntelliJ Console: not tested
     */
    RIGHT_SIDE_DOUBLE_LINE(61),
    /**
     * Rarely supported.<br>
     * <br>IntelliJ Console: not tested
     */
    IDEOGRAM_OVERLINE(62),
    /**
     * Rarely supported.<br>
     * <br>IntelliJ Console: not tested
     */
    LEFT_SIDE_LINE(62),
    /**
     * Rarely supported.<br>
     * <br>IntelliJ Console: not tested
     */
    IDEOGRAM_DOUBLE_OVERLINE(63),
    /**
     * Rarely supported.<br>
     * <br>IntelliJ Console: not tested
     */
    LEFT_SIDE_DOUBLE_LINE(63),
    /**
     * Rarely supported.<br>
     * <br>IntelliJ Console: not tested
     */
    IDEOGRAM_STRESS_MARKING(64),
    /**
     * Rarely supported. Resets Ideogram and left/right side line<br>
     * <br>IntelliJ Console: not tested
     */
    NO_IDEOGRAM_ATTRIBUTES(65),

    /**
     * Rarely supported.<br>
     * <br>IntelliJ Console: not tested
     */
    SUPERSCRIPT(73),
    /**
     * Rarely supported.<br>
     * <br>IntelliJ Console: not tested
     */
    SUBSCRIPT(74),
    /**
     * Rarely supported. Not {@link #SUBSCRIPT} nor {@link #SUPERSCRIPT}<br>
     * <br>IntelliJ Console: not tested
     */
    NOT_SUPERSCRIPT_NOR_SUBSCRIPT(75),

    /**
     * Not in standard.<br>
     * <br>IntelliJ Console: not tested
     */
    FOREGROUND_BRIGHT_BLACK(90),
    /**
     * Not in standard.<br>
     * <br>IntelliJ Console: not tested
     */
    FOREGROUND_BRIGHT_RED(91),
    /**
     * Not in standard.<br>
     * <br>IntelliJ Console: not tested
     */
    FOREGROUND_BRIGHT_GREEN(92),
    /**
     * Not in standard.<br>
     * <br>IntelliJ Console: not tested
     */
    FOREGROUND_BRIGHT_YELLOW(93),
    /**
     * Not in standard.<br>
     * <br>IntelliJ Console: not tested
     */
    FOREGROUND_BRIGHT_BLUE(94),
    /**
     * Not in standard.<br>
     * <br>IntelliJ Console: not tested
     */
    FOREGROUND_BRIGHT_MAGENTA(95),
    /**
     * Not in standard.<br>
     * <br>IntelliJ Console: not tested
     */
    FOREGROUND_BRIGHT_CYAN(96),
    /**
     * Not in standard.<br>
     * <br>IntelliJ Console: not tested
     */
    FOREGROUND_BRIGHT_WHITE(97),

    BACKGROUND_BRIGHT_BLACK(100),
    /**
     * Not in standard.<br>
     * <br>IntelliJ Console: not tested
     */
    BACKGROUND_BRIGHT_RED(101),
    /**
     * Not in standard.<br>
     * <br>IntelliJ Console: not tested
     */
    BACKGROUND_BRIGHT_GREEN(102),
    /**
     * Not in standard.<br>
     * <br>IntelliJ Console: not tested
     */
    BACKGROUND_BRIGHT_YELLOW(103),
    /**
     * Not in standard.<br>
     * <br>IntelliJ Console: not tested
     */
    BACKGROUND_BRIGHT_BLUE(104),
    /**
     * Not in standard.<br>
     * <br>IntelliJ Console: not tested
     */
    BACKGROUND_BRIGHT_MAGENTA(105),
    /**
     * Not in standard.<br>
     * <br>IntelliJ Console: not tested
     */
    BACKGROUND_BRIGHT_CYAN(106),
    /**
     * Not in standard.<br>
     * <br>IntelliJ Console: not tested
     */
    BACKGROUND_BRIGHT_WHITE(107),

    ;

    private final int identifier;
    private final int argCount;

    SGRParameters(int identifier, int argCount) {
        this.identifier = identifier;
        this.argCount = argCount;
    }

    SGRParameters(int identifier) {
        this(identifier, 0);
    }

    @Override
    public int getIdentifier() {
        return identifier;
    }

    @Override
    public int argumentCount() {
        return argCount;
    }
}
