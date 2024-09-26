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

import org.jetbrains.annotations.NotNull;

/**
 * Represents a {@link Color} as HSVA.<br>
 * H = hue, S = saturation, V = value, A = alpha/transparency
 */
@SuppressWarnings("unused")
public interface HSVAColor extends Color {

    double HSV_HUE_MIN = 0;
    double HSV_HUE_MAX = 360;
    double HSV_SATURATION_MIN = 0;
    double HSV_SATURATION_MAX = 100;
    double HSV_VALUE_MIN = 0;
    double HSV_VALUE_MAX = 100;
    double HSV_ALPHA_MIN = 0;
    double HSV_ALPHA_MAX = 1;

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
