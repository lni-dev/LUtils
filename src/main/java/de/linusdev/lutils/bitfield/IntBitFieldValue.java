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

public interface IntBitFieldValue {

    /**
     *
     * @param bitPos position of the bit, which is set. The first bit has the position 0.
     * @return int with given bit set.
     */
    static int bitPosToValue(int bitPos) {
        if(bitPos > 32)
            throw new UnsupportedOperationException("Integer only has 32 bits. bitPos " + bitPos + " is invalid.");
        return 1 << (bitPos);
    }

    int getValue();
}
