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

package de.linusdev.lutils.result;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ResultTest {

    @Test
    void singleResultTest() {
        SingleResult<String> result = new SingleResult<>("test");

        assertEquals(1, result.count());
        assertEquals("test", result.result());
        assertEquals("test", result.get(0));
        assertThrows(IndexOutOfBoundsException.class, () -> result.get(1));
    }

    @Test
    void biResultTest() {
        BiResult<String, Integer> result = new BiResult<>("test", 10);

        assertEquals(2, result.count());
        assertEquals("test", result.result1());
        assertEquals(10, result.result2());
        assertEquals("test", result.get(0));
        assertEquals(10, result.get(1));
        assertThrows(IndexOutOfBoundsException.class, () -> result.get(2));
    }

    @Test
    void triResultTest() {
        TriResult<String, Float, Integer> result = new TriResult<>("test", 3.2f, 13);

        assertEquals(3, result.count());
        assertEquals("test", result.result1());
        assertEquals(3.2f, result.result2());
        assertEquals(13, result.result3());
        assertEquals("test", result.get(0));
        assertEquals(3.2f, result.get(1));
        assertEquals(13, result.get(2));
        assertThrows(IndexOutOfBoundsException.class, () -> result.get(3));
    }
}