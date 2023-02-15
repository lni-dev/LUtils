/*
 * Copyright (c) 2023 Linus Andera all rights reserved
 */

package de.linusdev.lutils.color;

import de.linusdev.lutils.ansi.sgr.SGR;
import de.linusdev.lutils.ansi.sgr.SGRParameters;
import me.linusdev.data.SimpleDatable;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a rgba color.<br>
 * r = red, g = green, b = blue, a = alpha / transparency.<br>
 */
@SuppressWarnings("unused")
public interface RGBAColor extends SimpleDatable, Color {

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
    default int toRgbaHex() {
        //    #             FF                   FF                 FF             FF
        return (getRed() << 24) + (getGreen() << 16) + (getBlue() << 8) + (getAlpha());
    }

    /**
     * Returns a hex color with the format: {@code #00rrggbb}.
     * @return this color as rgb hex.
     */
    default int toRgbHex() {
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
        return toRgbaHex();
    }

    @Override
    default @NotNull RGBAColor toRGBAColor() {
        return this;
    }

    @Override
    default @NotNull HSVColor toHSVColor() {

        double r = red();
        double g = green();
        double b = blue();

        if(r == g && g == b) {
            return Color.ofHsva(0, 0, r, alpha());
        } else if(r > g && r > b) {
            double min = Math.min(g, b);
            return Color.ofHsva(60 * (((g - b) / (r - min)) % 6), (r - min) / r, r, alpha());
        }

        //TODO: implement
        return null;
    }
}
