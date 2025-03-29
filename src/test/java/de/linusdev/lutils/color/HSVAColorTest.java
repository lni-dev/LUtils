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