/*
 * Copyright (c) 2023 Linus Andera all rights reserved
 */

package de.linusdev.lutils.color;

import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public interface HSVColor extends Color {

    /**
     *
     * @return hue of this color in degree
     */
    double hue();

    /**
     *
     * @return saturation in percent
     */
    double saturation();

    /**
     *
     * @return value in percent
     */
    double value();

    /**
     * Alpha value of this color as double. Always between {@value RGBAColor#RGB_DOUBLE_MIN} (fully transparent) and {@value RGBAColor#RGB_DOUBLE_MAX} (opaque).
     * @return alpha value of this color
     */
    double alpha();

    @Override
    default @NotNull RGBAColor toRGBAColor() {
        //TODO: implement
        return null;
    }

    @Override
    default @NotNull HSVColor toHSVColor() {
        return this;
    }
}
