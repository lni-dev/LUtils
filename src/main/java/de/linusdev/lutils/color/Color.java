/*
 * Copyright (c) 2023-2025 Linus Andera
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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    @Contract(value = "_, -> new", pure = true)
    static @NotNull RGBAColor ofRGBA(@NotNull String hex) {
        if(hex.length() != 8)
            throw new IllegalArgumentException("Given hex '" + hex + "' is to long or to short. Must be 8 chars. It must not start with '0x'.");
        return ofRGBA((int)(long)Long.decode("0x" + hex));
    }

    @Contract(value = "_, -> new", pure = true)
    static @NotNull RGBAColor ofRGB(@NotNull String hex) {
        if(hex.length() != 6)
            throw new IllegalArgumentException("Given hex '" + hex + "' is to long or to short. Must be 6 chars. It must not start with '0x'.");
        return ofRGB((int)(long)Long.decode("0x" + hex));
    }

    @Contract(value = "_, _, _ -> new", pure = true)
    static @NotNull RGBAColor ofRGB(
            @Range(from = (long) RGBAColor.RGB_DOUBLE_MIN, to = (long) RGBAColor.RGB_DOUBLE_MAX) double r,
            @Range(from = (long) RGBAColor.RGB_DOUBLE_MIN, to = (long) RGBAColor.RGB_DOUBLE_MAX) double g,
            @Range(from = (long) RGBAColor.RGB_DOUBLE_MIN, to = (long) RGBAColor.RGB_DOUBLE_MAX) double b
    ) {
        return new RGBAColorDoubleImpl(r, g, b, 1d);
    }

    @Contract(value = "_, _, _, _ -> new", pure = true)
    static @NotNull RGBAColor ofRGBA(
            @Range(from = (long) RGBAColor.RGB_DOUBLE_MIN, to = (long) RGBAColor.RGB_DOUBLE_MAX) double r,
            @Range(from = (long) RGBAColor.RGB_DOUBLE_MIN, to = (long) RGBAColor.RGB_DOUBLE_MAX) double g,
            @Range(from = (long) RGBAColor.RGB_DOUBLE_MIN, to = (long) RGBAColor.RGB_DOUBLE_MAX) double b,
            @Range(from = (long) RGBAColor.RGB_DOUBLE_MIN, to = (long) RGBAColor.RGB_DOUBLE_MAX) double a
    ) {
        return new RGBAColorDoubleImpl(r, g, b, a);
    }

    @Contract(value = "_, _, _ -> new", pure = true)
    static @NotNull HSVAColor ofHSV(
            @Range(from = (long) HSVAColor.HSV_HUE_MIN, to = (long) HSVAColor.HSV_HUE_MAX)                  double h,
            @Range(from = (long) HSVAColor.HSV_SATURATION_MIN, to = (long) HSVAColor.HSV_SATURATION_MAX)    double s,
            @Range(from = (long) HSVAColor.HSV_VALUE_MIN, to = (long) HSVAColor.HSV_VALUE_MAX)              double v
    ) {
        return new HSVAColorImpl(h,s,v, 1d);
    }

    @Contract(value = "_, _, _, _ -> new", pure = true)
    static @NotNull HSVAColor ofHSVA(
            @Range(from = (long) HSVAColor.HSV_HUE_MIN, to = (long) HSVAColor.HSV_HUE_MAX)                  double h,
            @Range(from = (long) HSVAColor.HSV_SATURATION_MIN, to = (long) HSVAColor.HSV_SATURATION_MAX)    double s,
            @Range(from = (long) HSVAColor.HSV_VALUE_MIN, to = (long) HSVAColor.HSV_VALUE_MAX)              double v,
            @Range(from = (long) HSVAColor.HSV_ALPHA_MIN, to = (long) HSVAColor.HSV_ALPHA_MAX)              double a
    ) {
        return new HSVAColorImpl(h,s,v, a);
    }

    @NotNull Pattern CSS_RGB_PATTERN = Pattern.compile("rgba?\\( *(?<r>\\d+) *,? *(?<g>\\d+) *,? *(?<b>\\d+) *(,? *(?<a>\\d+) *)?\\)");
    @NotNull Pattern CSS_HEX_PATTERN = Pattern.compile("#(?<hex>[0-9a-fA-F]{6}([0-9a-fA-F]{2})?)");

    /**
     * Can decode css color definitions to a {@link Color} instance.
     * Allowed color definitions are:
     * <ul>
     *     <li>{@code rgb(<r>, <g>, <b>)}</li>
     *     <li>{@code rgba(<r>, <g>, <b>, <a>)}</li>
     *     <li>{@code #rrggbb}</li>
     *     <li>{@code #rrggbbaa}</li>
     * </ul>
     */
    static @NotNull Color ofCssColorDefinition(@NotNull String cssColorDef) {
        Color parsed;

        Matcher matcher = CSS_RGB_PATTERN.matcher(cssColorDef);

        if(matcher.find()) {
            if(matcher.group("a") != null) {
                return Color.ofRGBA(
                        Integer.parseInt(matcher.group("r")),
                        Integer.parseInt(matcher.group("g")),
                        Integer.parseInt(matcher.group("b")),
                        Integer.parseInt(matcher.group("a"))
                );
            } else {
                return Color.ofRGB(
                        Integer.parseInt(matcher.group("r")),
                        Integer.parseInt(matcher.group("g")),
                        Integer.parseInt(matcher.group("b"))
                );
            }
        }

        matcher = CSS_HEX_PATTERN.matcher(cssColorDef);

        if(matcher.find()) {
            if(matcher.group("hex").length() == 8)
                return Color.ofRGBA(matcher.group("hex"));
            return Color.ofRGB(matcher.group("hex"));
        }

        throw new IllegalArgumentException("Cannot decode css color definition '" + cssColorDef + "'.");
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
