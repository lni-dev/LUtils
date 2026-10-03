/*
 * Copyright (c) 2026 Linus Andera
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

package de.linusdev.lutils.id3v2.tagrestrictions;

public enum TextEncodingRestriction {
    NONE(0b00),
    ISO_8859_1_ONLY(0b01),
    UTF_8_ONLY(0b10),
    UTF_16_ONLY(0b11);

    private final int value;

    TextEncodingRestriction(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static TextEncodingRestriction from(int bits) {
        for (TextEncodingRestriction r : values()) {
            if (r.value == bits) return r;
        }
        throw new IllegalArgumentException("Unknown text encoding restriction: " + bits);
    }
}