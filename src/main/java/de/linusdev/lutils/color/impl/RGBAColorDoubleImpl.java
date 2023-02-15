/*
 * Copyright (c) 2023 Linus Andera all rights reserved
 */

package de.linusdev.lutils.color.impl;

import de.linusdev.lutils.color.RGBAColor;

public class RGBAColorDoubleImpl implements RGBAColor {

    private final double red;
    private final double green;
    private final double blue;
    private final double alpha;

    public RGBAColorDoubleImpl(double red, double green, double blue, double alpha) {

        if(red < RGBAColor.RGB_DOUBLE_MIN || red > RGBAColor.RGB_DOUBLE_MAX
                || green < RGBAColor.RGB_DOUBLE_MIN || green > RGBAColor.RGB_DOUBLE_MAX
                || blue < RGBAColor.RGB_DOUBLE_MIN || blue > RGBAColor.RGB_DOUBLE_MAX
                || alpha < RGBAColor.RGB_DOUBLE_MIN || alpha > RGBAColor.RGB_DOUBLE_MAX) {
            throw new IllegalArgumentException("No color double value may be above "
                    + RGBAColor.RGB_DOUBLE_MAX + " or below " + RGBAColor.RGB_DOUBLE_MIN + ". r, g, b, a: "
                    + red + ", " + green + ", " + blue + ", " + alpha + ".");
        }

        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }

    @Override
    public int getRed() {
        return (int) Math.round(255d * red);
    }

    @Override
    public int getGreen() {
        return (int) Math.round(255d * green);
    }

    @Override
    public int getBlue() {
        return (int) Math.round(255d * blue);
    }

    @Override
    public int getAlpha() {
        return (int) Math.round(255d * alpha);
    }

    @Override
    public double red() {
        return red;
    }

    @Override
    public double green() {
        return green;
    }

    @Override
    public double blue() {
        return blue;
    }

    @Override
    public double alpha() {
        return alpha;
    }
}
