/*
 * Copyright (c) 2023 Linus Andera all rights reserved
 */

package de.linusdev.lutils.color.impl;

import de.linusdev.lutils.color.HSVAColor;

public class HSVAColorImpl implements HSVAColor {

    private final double hue;
    private final double saturation;
    private final double value;
    private final double alpha;

    public HSVAColorImpl(double hue, double saturation, double value, double alpha) {

        if(hue < 0d || hue >= 360d)
            throw new IllegalArgumentException("hue must be between 0 (inclusive) and 360 (exclusive).");

        if(saturation < 0d || saturation > 100d)
            throw new IllegalArgumentException("hue must be between 0 (inclusive) and 100 (inclusive).");

        if(value < 0d || value > 100d)
            throw new IllegalArgumentException("hue must be between 0 (inclusive) and 100 (inclusive).");

        if(alpha < 0d || alpha > 1d)
            throw new IllegalArgumentException("hue must be between 0 (inclusive) and 1 (inclusive).");

        this.hue = hue;
        this.saturation = saturation;
        this.value = value;
        this.alpha = alpha;
    }

    @Override
    public double hue() {
        return hue;
    }

    @Override
    public double saturation() {
        return saturation;
    }

    @Override
    public double value() {
        return value;
    }

    @Override
    public double alpha() {
        return alpha;
    }

    @Override
    public String toString() {
        return "(" + hue() + ", " + saturation() + ", " + value() + ", " + alpha() + ")";
    }
}
