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

public enum TextFieldSizeRestriction {
    NONE(0b00),
    MAX_1024_BYTES(0b01),
    MAX_128_BYTES(0b10),
    MAX_30_BYTES(0b11);

    private final int value;

    TextFieldSizeRestriction(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static TextFieldSizeRestriction from(int bits) {
        for (TextFieldSizeRestriction r : values()) {
            if (r.value == bits) return r;
        }
        throw new IllegalArgumentException("Unknown text field size restriction: " + bits);
    }
}