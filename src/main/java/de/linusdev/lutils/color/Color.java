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

import de.linusdev.lutils.color.impl.HSVAColorImpl;
import de.linusdev.lutils.color.impl.RGBAColorDoubleImpl;
import de.linusdev.lutils.color.impl.RGBAColorIntImpl;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

@SuppressWarnings("unused")
public interface Color {

    RGBAColor BLACK = ofRGB(0, 0, 0);
    RGBAColor RED = ofRGB(255, 0, 0);
    RGBAColor GREEN = ofRGB(0, 255, 0);
    RGBAColor BLUE = ofRGB(0, 0, 255);
    RGBAColor WHITE = ofRGB(255, 255, 255);

    RGBAColor CYAN = ofRGB(17, 168, 205);
    RGBAColor ORANGE = ofRGB(250, 138, 10);

    RGBAColor DARK_GRAY = ofRGB(0x808080);
    RGBAColor LIGHT_GRAY = ofRGB(0xD3D3D3);
    RGBAColor GRAY = ofRGB(0xA9A9A9);

    @Contract(value = "_, _, _ -> new", pure = true)
    static @NotNull RGBAColor ofRGB(@Range(from = RGBAColor.RGB_INT_MIN, to = RGBAColor.RGB_INT_MAX) int r,
                                    @Range(from = RGBAColor.RGB_INT_MIN, to = RGBAColor.RGB_INT_MAX) int g,
                                    @Range(from = RGBAColor.RGB_INT_MIN, to = RGBAColor.RGB_INT_MAX)int b) {
        return new RGBAColorIntImpl(r, g, b, 255);
    }

    @Contract(value = "_, _, _, _ -> new", pure = true)
    static @NotNull RGBAColor ofRGBA(@Range(from = RGBAColor.RGB_INT_MIN, to = RGBAColor.RGB_INT_MAX) int r,
                                     @Range(from = RGBAColor.RGB_INT_MIN, to = RGBAColor.RGB_INT_MAX) int g,
                                     @Range(from = RGBAColor.RGB_INT_MIN, to = RGBAColor.RGB_INT_MAX) int b,
                                     @Range(from = RGBAColor.RGB_INT_MIN, to = RGBAColor.RGB_INT_MAX) int a) {
        return new RGBAColorIntImpl(r, g, b, a);
    }

    @Contract(value = "_, -> new", pure = true)
    static @NotNull RGBAColor ofRGBA(int hex) {
        return new RGBAColorIntImpl((hex & 0xff000000) >>> 24, (hex & 0x00ff0000) >>> 16, (hex & 0x0000ff00) >>> 8, hex & 0x000000ff);
    }

    @Contract(value = "_, -> new", pure = true)
    static @NotNull RGBAColor ofRGB(int hex) {
        return new RGBAColorIntImpl((hex & 0x00ff0000) >>> 16, (hex & 0x0000ff00) >>> 8, hex & 0x000000ff, 255);
    }

    @Contract(value = "_, _, _ -> new", pure = true)
    static @NotNull RGBAColor ofRGB(double r, double g, double b) {
        return new RGBAColorDoubleImpl(r, g, b, 1d);
    }

    @Contract(value = "_, _, _, _ -> new", pure = true)
    static @NotNull RGBAColor ofRGBA(double r, double g, double b, double a) {
        return new RGBAColorDoubleImpl(r, g, b, a);
    }

    @Contract(value = "_, _, _ -> new", pure = true)
    static @NotNull HSVAColor ofHSV(double h, double s, double v) {
        return new HSVAColorImpl(h,s,v, 1d);
    }

    @Contract(value = "_, _, _, _ -> new", pure = true)
    static @NotNull HSVAColor ofHSVA(double h, double s, double v, double a) {
        return new HSVAColorImpl(h,s,v, a);
    }

    /**
     * This {@link Color} converted to {@link RGBAColor}. If this is already a {@link RGBAColor}, this object itself
     * may be returned.
     * @return {@link RGBAColor}
     */
    @NotNull RGBAColor toRGBAColor();

    /**
     * This {@link Color} converted to {@link HSVAColor}. If this is already a {@link HSVAColor}, this object itself
     * may be returned.
     * @return {@link HSVAColor}
     */
    @NotNull HSVAColor toHSVAColor();

}
