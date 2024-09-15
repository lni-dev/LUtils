/*
 * Copyright (c) 2024 Linus Andera
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

package de.linusdev.lutils.nat.size;

public enum ByteUnits {
    KiB(10),
    MiB(20),
    GiB(30),
    TiB(40),
    PiB(50)
    ;

    /**
     * Value, how many bytes this unit represents.
     */
    private final long value;
    /**
     * Exponent, so that {@link #value} is {@code Math.pow(2, exponent)}.
     */
    private final int exponent;

    ByteUnits(int exponent) {
        this.exponent = exponent;
        this.value = 1L << exponent;
    }

    public long getValue() {
        return value;
    }

    @SuppressWarnings("unused")
    public int getExponent() {
        return exponent;
    }
}
