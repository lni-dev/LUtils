/*
 * Copyright (c) 2023 Linus Andera all rights reserved
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
        return new RGBAColorIntImpl(hex & 0xff000000, hex & 0x00ff0000, hex & 0x0000ff00, hex & 0x000000ff);
    }

    @Contract(value = "_, -> new", pure = true)
    static @NotNull RGBAColor ofRGB(int hex) {
        return new RGBAColorIntImpl(hex & 0x00ff0000, hex & 0x0000ff00, hex & 0x000000ff, 255);
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
