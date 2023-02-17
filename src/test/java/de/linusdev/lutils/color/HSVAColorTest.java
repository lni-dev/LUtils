/*
 * Copyright (c) 2023 Linus Andera all rights reserved
 */

package de.linusdev.lutils.color;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HSVAColorTest {

    @Test
    void hue() {
        HSVAColor color = Color.ofHSVA(45d, 99d, 98d, 1d);

        assertEquals(45d, color.hue());
        assertEquals(99d, color.saturation());
        assertEquals(98d, color.value());
        assertEquals(1d, color.alpha());
    }

    @Test
    void toRGBAColor() {
        HSVAColor color = Color.ofHSVA(45d, 99d, 98d, 1d);
        RGBAColor conv = color.toRGBAColor();

        assertEquals(250, conv.getRed());
        assertEquals(188, conv.getGreen() );
        assertEquals(2, conv.getBlue());
        assertEquals(255, conv.getAlpha());

        assertEquals(Math.round(250d / 255d), Math.round(conv.red()));
        assertEquals(Math.round(188d / 255d), Math.round(conv.green()));
        assertEquals(Math.round(2d / 255d), Math.round(conv.blue()));
        assertEquals(Math.round(1d), Math.round(conv.alpha()));
    }
}