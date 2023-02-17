/*
 * Copyright (c) 2023 Linus Andera all rights reserved
 */

package de.linusdev.lutils.color;

import org.jetbrains.annotations.NotNull;

/**
 * Represents a {@link Color} as HSVA.<br>
 * H = hue, S = saturation, V = value, A = alpha/transparency
 */
@SuppressWarnings("unused")
public interface HSVAColor extends Color {

    /**
     *
     * @return hue of this color in degree (from 0°, inclusive, to 360°, exclusive)
     */
    double hue();

    /**
     *
     * @return saturation in percent (from 0 to 100)
     */
    double saturation();

    /**
     *
     * @return value in percent (from 0 to 100)
     */
    double value();

    /**
     * Alpha value of this color as double. Always between {@value RGBAColor#RGB_DOUBLE_MIN} (fully transparent) and {@value RGBAColor#RGB_DOUBLE_MAX} (opaque).
     * @return alpha value of this color
     */
    double alpha();

    @Override
    default @NotNull RGBAColor toRGBAColor() {

        double hue = hue();
        double c = (value() / 100d) * (saturation() / 100d);
        double x = c * (1d - Math.abs(((hue / 60d) % 2) - 1));
        double m = (value() / 100d) - c;

        if (hue < 60d) {
            return Color.ofRGBA(c + m,x + m, 0 + m, alpha());
        } else if (hue < 120d) {
            return Color.ofRGBA(x + m,c + m, 0 + m, alpha());
        } else if (hue < 180d) {
            return Color.ofRGBA(0 + m,c + m, x + m, alpha());
        } else if (hue < 240d) {
            return Color.ofRGBA(0 + m,x + m, c + m, alpha());
        } else if (hue < 300d) {
            return Color.ofRGBA(x + m,0 + m, c + m, alpha());
        } else /* if (hue < 360d)*/ {
            return Color.ofRGBA(c + m,0 + m, x + m, alpha());
        }
    }

    @Override
    default @NotNull HSVAColor toHSVAColor() {
        return this;
    }
}
