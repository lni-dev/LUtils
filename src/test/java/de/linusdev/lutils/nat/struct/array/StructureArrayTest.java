/*
 * Copyright (c) 2025-2026 Linus Andera
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

package de.linusdev.lutils.nat.struct.array;

import de.linusdev.lutils.math.vector.buffer.intn.BBInt1;
import org.junit.jupiter.api.Test;

import static de.linusdev.lutils.nat.memory.Allocators.allocManaged;
import static org.junit.jupiter.api.Assertions.*;

class StructureArrayTest {

    @Test
    void test() {

        StructureArray<BBInt1> array = allocManaged(StructureArray.newAllocatable(12, BBInt1.class, BBInt1::newUnallocated));

        array.enableCaching();

        assertNull(array.getOrNull(0));
        assertNotNull(array.get(0));
        assertNotNull(array.getOrNull(0));

        array.get(10).set(20);

        NativeArrayView<BBInt1> view = array.getView(10, 2);

        assertEquals(20, view.get(0).get());
        assertNotNull(view.get(1));

    }

    @Test
    void testNoCaching() {
        StructureArray<BBInt1> array = allocManaged(StructureArray.newAllocatable(12, BBInt1.class, BBInt1::newUnallocated));

        assertThrows(IllegalStateException.class, () -> array.getOrNull(0));
        BBInt1 a = array.get(0);
        BBInt1 b = array.get(0);

        assertNotSame(a, b);

        a.set(12);

        assertEquals(12, a.get());
        assertEquals(12, b.get());

        array.get(1, a);

        assertEquals(0, a.get());
        BBInt1 c = array.get(1);

        c.set(13);
        assertEquals(13, a.get());
        assertEquals(13, c.get());
        assertEquals(12, b.get());

    }

    @Test
    void testWithCaching() {
        StructureArray<BBInt1> array = allocManaged(StructureArray.newAllocatable(12, BBInt1.class, BBInt1::newUnallocated));

        array.enableCaching();

        assertDoesNotThrow(() -> array.getOrNull(0));
        BBInt1 a = array.get(0);
        BBInt1 b = array.get(0);

        assertSame(a, b);

        a.set(12);

        assertEquals(12, a.get());
        assertEquals(12, b.get());

        array.get(1, a);

        assertEquals(0, a.get());
        BBInt1 c = array.get(1);

        assertNotSame(a, c);

        c.set(13);
        assertEquals(13, a.get());
        assertEquals(13, c.get());
        assertEquals(13, b.get());

    }
}