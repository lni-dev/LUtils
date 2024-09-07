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

package de.linusdev.lutils.color;

import de.linusdev.lutils.ansi.sgr.SGR;
import de.linusdev.lutils.ansi.sgr.SGRParameters;
import de.linusdev.lutils.interfaces.Simplifiable;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a {@link Color} as RGBA.<br>
 * R = red, G = green, B = blue, A = alpha / transparency.<br>
 */
@SuppressWarnings("unused")
public interface RGBAColor extends Simplifiable, Color {

    int RGB_INT_MIN = 0;
    int RGB_INT_MAX= 255;

    double RGB_DOUBLE_MIN = 0d;
    double RGB_DOUBLE_MAX= 1d;

    /**
     * Red value of this color as int. Always between {@value #RGB_INT_MIN} (no red) and {@value RGB_INT_MAX} (max red).
     * @return red value of this color
     */
    int getRed();

    /**
     * Green value of this color as int. Always between {@value #RGB_INT_MIN} (no green) and {@value RGB_INT_MAX} (max green).
     * @return green value of this color
     */
    int getGreen();

    /**
     * Blue value of this color as int. Always between {@value #RGB_INT_MIN} (no blue) and {@value RGB_INT_MAX} (max blue).
     * @return blue value of this color
     */
    int getBlue();

    /**
     * Alpha value of this color as int. Always between {@value #RGB_INT_MIN} (fully transparent) and {@value RGB_INT_MAX} (opaque).
     * @return alpha value of this color
     */
    int getAlpha();

    /**
     * Red value of this color as double. Always between {@value #RGB_DOUBLE_MIN} (no red) and {@value RGB_DOUBLE_MAX} (max red).
     * @return red value of this color
     */
    double red();

    /**
     * Green value of this color as double. Always between {@value #RGB_DOUBLE_MIN} (no green) and {@value RGB_DOUBLE_MAX} (max green).
     * @return green value of this color
     */
    double green();

    /**
     * Blue value of this color as double. Always between {@value #RGB_DOUBLE_MIN} (no blue) and {@value RGB_DOUBLE_MAX} (max blue).
     * @return blue value of this color
     */
    double blue();

    /**
     * Alpha value of this color as double. Always between {@value #RGB_DOUBLE_MIN} (fully transparent) and {@value RGB_DOUBLE_MAX} (opaque).
     * @return alpha value of this color
     */
    double alpha();


    /**
     * Returns a hex color with the format: {@code #rrggbbaa}.
     * @return this color as rgba hex.
     */
    default int toRGBAHex() {
        //    #             FF                   FF                 FF             FF
        return (getRed() << 24) + (getGreen() << 16) + (getBlue() << 8) + (getAlpha());
    }

    /**
     * Returns a hex color with the format: {@code #00rrggbb}.
     * @return this color as rgb hex.
     */
    default int toRGBHex() {
        //    #00           FF                  FF            FF
        return (getRed() << 16) + (getGreen() << 8) + (getBlue());
    }

    /**
     * Returns this color as {@link java.awt.Color}. Uses the int color values for the conversion.
     * @return {@link java.awt.Color} representing this {@link RGBAColor}.
     */
    default @NotNull java.awt.Color toJavaAwtColor() {
        return new java.awt.Color(getRed(), getGreen(), getBlue(), getAlpha());
    }

    default @NotNull SGR addToSgrAsForeground(@NotNull SGR sgr) {
        return sgr.add(SGRParameters.FOREGROUND_COLOR_24_BIT, String.valueOf(getRed()), String.valueOf(getGreen()), String.valueOf(getBlue()));
    }

    default @NotNull SGR addToSgrAsBackground(@NotNull SGR sgr) {
        return sgr.add(SGRParameters.BACKGROUND_COLOR_24_BIT, String.valueOf(getRed()), String.valueOf(getGreen()), String.valueOf(getBlue()));
    }

    @Override
    default @NotNull Integer simplify() {
        return getAlpha() == 255 ? toRGBHex() : toRGBAHex();
    }

    @Override
    default @NotNull RGBAColor toRGBAColor() {
        return this;
    }

    @Override
    default @NotNull HSVAColor toHSVAColor() {

        double r = red();
        double g = green();
        double b = blue();

        if(r == g && g == b) {
            return Color.ofHSVA(0, 0, r * 100d, alpha());
        } else if(r > g && r > b) {
            double min = Math.min(g, b);
            return Color.ofHSVA((60d * ((g - b) / (r - min))) % 360d, ((r - min) * 100d) / r, r * 100d, alpha());
        } else if(g > b) {
            double min = Math.min(r, b);
            return Color.ofHSVA((60d * ((b - r) / (g - min)) + 120d) % 360d, ((g - min) * 100d) / g, g * 100d, alpha());
        } else {
            double min = Math.min(r, g);
            return Color.ofHSVA((60d * ((r - g) / (b - min)) + 120d) % 360d, ((b - min) * 100d) / b, b * 100d, alpha());
        }
    }
}
