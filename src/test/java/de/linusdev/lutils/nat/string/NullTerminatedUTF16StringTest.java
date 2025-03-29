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

package de.linusdev.lutils.nat.string;

import de.linusdev.lutils.nat.struct.annos.SVWrapper;
import org.junit.jupiter.api.Test;

import java.nio.BufferOverflowException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class NullTerminatedUTF16StringTest {
    @Test
    void test() {
        NullTerminatedUTF16String string = NullTerminatedUTF16String.newAllocated(SVWrapper.length(50));

        assertEquals(50, string.length());

        String test1 = "Test1234-+ÖÜ";
        string.set(test1);
        assertEquals(test1, string.get());

        String test2 = "a".repeat(50);
        assertThrows(BufferOverflowException.class, () -> string.set(test2));

        String test3 = "Test3";
        string.set(test3);
        assertEquals(test3, string.get());

        String test4 = "Test4";
        string.set(test4);
        assertEquals(test4, string.get());
    }
}