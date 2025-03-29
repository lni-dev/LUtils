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

package de.linusdev.lutils.nat.size;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SizeTest {

    @Test
    void test() {
        Size size = new Size(1024);

        assertEquals(1024L, size.get());
        assertEquals(1024L, size.getAsInt());
        assertEquals("1KiB bytes", size.toString());

        size = new Size(1, ByteUnits.KiB);

        assertEquals(1024L, size.get());
        assertEquals(1024L, size.getAsInt());
        assertEquals("1KiB bytes", size.toString());
    }

    @Test
    void test2() {
        Size size = new Size(32432434343L);

        assertEquals(32432434343L, size.get());
        assertThrows(AssertionError.class, size::getAsInt);
        assertEquals("30GiB bytes", size.toString());

        Size size2 = new Size(1, ByteUnits.PiB);

        assertEquals(1125899906842624L, size2.get());
        assertThrows(AssertionError.class, size2::getAsInt);
        assertEquals("1PiB bytes", size2.toString());
    }
}