/*
 * Copyright (c) 2025 Linus Andera
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

package de.linusdev.lutils.struct.info;

import de.linusdev.lutils.nat.abi.DefaultABIs;
import de.linusdev.lutils.nat.struct.info.StructureInfo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ABITest {

    @Test
    void MSVC_X64_calculateStructureLayout() {
        StructureInfo info;

        // https://learn.microsoft.com/en-us/cpp/build/x64-software-conventions?view=msvc-170#example-3
        info = DefaultABIs.MSVC_X64.calculateStructureLayout(
                false,
                new StructureInfo(1, false, 0, 1, 0), // c-char
                new StructureInfo(2, false, 0, 2, 0), // c-short
                new StructureInfo(1, false, 0, 1, 0), // c-char
                new StructureInfo(4, false, 0, 4, 0)  // c-int
        );

        assertEquals(12, info.getRequiredSize());
        assertEquals(4, info.getAlignment());
        assertArrayEquals(new int[] {0, 1, 1, 2, 0, 1, 3, 4, 0}, info.getSizes());

        // https://learn.microsoft.com/en-us/cpp/build/x64-software-conventions?view=msvc-170#example-2
        info = DefaultABIs.MSVC_X64.calculateStructureLayout(
                false,
                new StructureInfo(4, false, 0, 4, 0), // c-int
                new StructureInfo(8, false, 0, 8, 0), // c-double
                new StructureInfo(2, false, 0, 2, 0)  // c-short
        );

        assertEquals(24, info.getRequiredSize());
        assertEquals(8, info.getAlignment());
        assertArrayEquals(new int[] {0, 4, 4, 8, 0, 2, 6}, info.getSizes());

        // Compress
        info = DefaultABIs.MSVC_X64.calculateStructureLayout(
                true,
                new StructureInfo(4, false, 0, 4, 0), // c-int
                new StructureInfo(8, false, 0, 8, 0), // c-double
                new StructureInfo(2, false, 0, 2, 0)  // c-short
        );

        assertEquals(14, info.getRequiredSize());
        assertEquals(1, info.getAlignment());
        assertArrayEquals(new int[] {0, 4, 0, 8, 0, 2, 0}, info.getSizes());
    }
}