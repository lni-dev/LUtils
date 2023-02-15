/*
 * Copyright (c) 2023 Linus Andera all rights reserved
 */

package de.linusdev.lutils.color.impl;

import de.linusdev.lutils.color.HSVColor;

public class HSVColorImpl implements HSVColor {

    private final double hue;
    private final double saturation;
    private final double value;
    private final double alpha;

    public HSVColorImpl(double hue, double saturation, double value, double alpha) {
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
}
