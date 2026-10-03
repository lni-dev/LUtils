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

package de.linusdev.lutils.id3v2;

import de.linusdev.lutils.bitfield.IntBitFieldValue;

public enum Id3v2HeaderFlags implements IntBitFieldValue {

    NOTHING(0),
    UNSYNCHRONISATION(1<<7),
    EXTENDED_HEADER(1<<6),
    EXPERIMENTAL(1<<5),
    FOOTER_PRESENT(1<<4),

    ;

    private final int value;

    Id3v2HeaderFlags(int value) {
        this.value = value;
    }

    @Override
    public int getValue() {
        return value;
    }
}