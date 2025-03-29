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

package de.linusdev.lutils.other;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UniqueIdentifierGeneratorTest {

    @Test
    void test() {

        var gen = new UniqueIdentifierGenerator("prefix-", 10);

        assertEquals(10, gen.getNextAsInt());
        assertEquals(11L, gen.getNext());
        assertEquals("prefix-12", gen.getNextAsString());

        gen = new UniqueIdentifierGenerator(15);
        assertEquals("15", gen.getNextAsString());
        assertEquals(16, gen.getCurrentNextIdWithoutIncrementing());

    }
}