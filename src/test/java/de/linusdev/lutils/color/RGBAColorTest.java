/*
 * Copyright (c) 2023 Linus Andera all rights reserved
 */

package de.linusdev.lutils.color;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RGBAColorTest {

    @Test
    void colorInt() {
        RGBAColor color = Color.ofRgba(222, 100, 50, 245);

        assertEquals(222, color.getRed());
        assertEquals(100, color.getGreen() );
        assertEquals(50, color.getBlue());
        assertEquals(245, color.getAlpha());

        assertEquals(222d / 255d, color.red());
        assertEquals(100d / 255d, color.green());
        assertEquals(50d / 255d, color.blue());
        assertEquals(245d / 255d, color.alpha());
    }

    @Test
    void colorDouble() {
        RGBAColor color = Color.ofRgba(222d / 255d, 100d / 255d, 50d / 255d, 245d / 255d);

        assertEquals(222, color.getRed());
        assertEquals(100, color.getGreen() );
        assertEquals(50, color.getBlue());
        assertEquals(245, color.getAlpha());

        assertEquals(222d / 255d, color.red());
        assertEquals(100d / 255d, color.green());
        assertEquals(50d / 255d, color.blue());
        assertEquals(245d / 255d, color.alpha());
    }

    @Test
    void hex() {
        RGBAColor color = Color.ofRgba(222, 100, 50, 255);

        assertEquals(0xde6432ff, color.toRgbaHex());
        assertEquals(0xde6432, color.toRgbHex());
    }


    @Test
    void toHSVColor() {
        RGBAColor color = Color.ofRgba(222, 100, 50, 245);
        HSVColor hsv = color.toHSVColor();

        assertEquals(17, Math.round(hsv.hue()));
        assertEquals(77, Math.round(hsv.saturation()));
        assertEquals(87, Math.round(hsv.value()));
        assertEquals(245d/255d, hsv.alpha());
    }
}