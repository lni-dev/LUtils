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

package de.linusdev.lutils.color.impl;

import de.linusdev.lutils.color.RGBAColor;

public class RGBAColorIntImpl implements RGBAColor {

    private final int red;
    private final int green;
    private final int blue;
    private final int alpha;

    public RGBAColorIntImpl(int red, int green, int blue, int alpha) {

        if(red < RGBAColor.RGB_INT_MIN || red > RGBAColor.RGB_INT_MAX
                || green < RGBAColor.RGB_INT_MIN || green > RGBAColor.RGB_INT_MAX
                || blue < RGBAColor.RGB_INT_MIN || blue > RGBAColor.RGB_INT_MAX
                || alpha < RGBAColor.RGB_INT_MIN || alpha > RGBAColor.RGB_INT_MAX) {
            throw new IllegalArgumentException("No color int value may be above "
                    + RGBAColor.RGB_INT_MAX + " or below " + RGBAColor.RGB_INT_MIN + ". r, g, b, a: "
                    + red + ", " + green + ", " + blue + ", " + alpha + ".");
        }

        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }

    @Override
    public int getRed() {
        return red;
    }

    @Override
    public int getGreen() {
        return green;
    }

    @Override
    public int getBlue() {
        return blue;
    }

    @Override
    public int getAlpha() {
        return alpha;
    }

    @Override
    public double red() {
        return ((double) red) / 255d;
    }

    @Override
    public double green() {
        return ((double) green) / 255d;
    }

    @Override
    public double blue() {
        return ((double) blue) / 255d;
    }

    @Override
    public double alpha() {
        return ((double) alpha) / 255d;
    }

    @Override
    public String toString() {
        return "(" + getRed() + ", " + getGreen() + ", " + getBlue() + ", " + getAlpha() + ")";
    }
}
