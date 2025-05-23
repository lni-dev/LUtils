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

package de.linusdev.lutils.bitfield;

public enum SomeEnum implements IntBitFieldValue {

    NOTHING(0),
    A(1),
    B(1<<1),
    C(1<<2),
    D(1<<3),
    E(1<<4),

    ;

    private final int value;

    SomeEnum(int value) {
        this.value = value;
    }

    @Override
    public int getValue() {
        return value;
    }
}
