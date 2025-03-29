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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RGBAColorTest {

    @Test
    void colorInt() {
        RGBAColor color = Color.ofRGBA(222, 100, 50, 245);

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
        RGBAColor color = Color.ofRGBA(222d / 255d, 100d / 255d, 50d / 255d, 245d / 255d);

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
        RGBAColor color = Color.ofRGBA(222, 100, 50, 255);

        assertEquals(0xde6432ff, color.toRGBAHex());
        assertEquals(0xde6432, color.toRGBHex());

        color = Color.ofRGBA(0x01020304);

        assertEquals(1, color.getRed());
        assertEquals(2, color.getGreen() );
        assertEquals(3, color.getBlue());
        assertEquals(4, color.getAlpha());
    }


    @Test
    void toHSVColor() {
        RGBAColor color = Color.ofRGBA(222, 100, 50, 245);
        HSVAColor hsv = color.toHSVAColor();

        assertEquals(17, Math.round(hsv.hue()));
        assertEquals(77, Math.round(hsv.saturation()));
        assertEquals(87, Math.round(hsv.value()));
        assertEquals(245d/255d, hsv.alpha());
    }

    @Test
    void ofCssDefinition() {
        RGBAColor color = Color.ofCssColorDefinition("rgba(222, 100, 50, 245)").toRGBAColor();

        assertEquals(222, color.getRed());
        assertEquals(100, color.getGreen());
        assertEquals(50, color.getBlue());
        assertEquals(245, color.getAlpha());

        color = Color.ofCssColorDefinition("rgb(222, 100, 50)").toRGBAColor();

        assertEquals(222, color.getRed());
        assertEquals(100, color.getGreen());
        assertEquals(50, color.getBlue());

        color = Color.ofCssColorDefinition("#de6432").toRGBAColor();

        assertEquals(222, color.getRed());
        assertEquals(100, color.getGreen());
        assertEquals(50, color.getBlue());

        color = Color.ofCssColorDefinition("#de6432ff").toRGBAColor();

        assertEquals(222, color.getRed());
        assertEquals(100, color.getGreen());
        assertEquals(50, color.getBlue());
        assertEquals(255, color.getAlpha());
    }
}