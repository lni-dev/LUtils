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

        assertEquals(color.getRed(), 222);
        assertEquals(color.getGreen(), 100);
        assertEquals(color.getBlue(), 50);
        assertEquals(color.getAlpha(), 255);

        assertEquals(color.red(), 222d / 255d);
        assertEquals(color.green(), 100d / 255d);
        assertEquals(color.blue(), 50d / 255d);
        assertEquals(color.alpha(), 245d / 255d);
    }

    @Test
    void colorDouble() {
        RGBAColor color = Color.ofRgba(222d / 255d, 100d / 255d, 50d / 255d, 245d / 255d);

        assertEquals(color.getRed(), 222);
        assertEquals(color.getGreen(), 100);
        assertEquals(color.getBlue(), 50);
        assertEquals(color.getAlpha(), 255);

        assertEquals(color.red(), 222d / 255d);
        assertEquals(color.green(), 100d / 255d);
        assertEquals(color.blue(), 50d / 255d);
        assertEquals(color.alpha(), 245d / 255d);
    }

    @Test
    void hex() {
        RGBAColor color = Color.ofRgba(222, 100, 50, 245);
        assertEquals(0xde6332ff, color.toRgbaHex());
        assertEquals(0xde6332, color.toRgbHex());
    }


    @Test
    void toHSVColor() {
        RGBAColor color = Color.ofRgba(222, 100, 50, 245);
        HSVColor hsv = color.toHSVColor();
    }
}