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

package de.linusdev.lutils.nat.struct.abstracts;

import de.linusdev.lutils.math.vector.buffer.intn.BBInt1;
import de.linusdev.lutils.math.vector.buffer.intn.BBInt2;
import de.linusdev.lutils.math.vector.buffer.intn.BBInt4;
import de.linusdev.lutils.nat.struct.annos.StructValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ComplexUnionTest {

    public static class TestStruct extends ComplexStructure {

        public final @StructValue BBInt1 a = BBInt1.newUnallocated();
        public final @StructValue BBInt1 b = BBInt1.newUnallocated();
        public final @StructValue BBInt1 c = BBInt1.newUnallocated();

        public TestStruct() {
            super(false);
            init(null, true);
            allocate();
        }
    }

    public static class TestUnion1 extends ComplexUnion {

        public final @StructValue BBInt1 a = BBInt1.newUnallocated();
        public final @StructValue BBInt2 b = BBInt2.newUnallocated();
        public final @StructValue BBInt4 c = BBInt4.newUnallocated();

        public TestUnion1() {
            super(false);
            init(null, true);
            allocate();
        }
    }

    @Test
    void test() {
        TestUnion1 union = new TestUnion1();

        assertEquals(16, union.getRequiredSize());
        assertEquals(4, union.getAlignment());
        assertEquals(0, union.getOffset());

        union.a.set(67);

        assertEquals(67, union.a.get());
        assertEquals(67, union.b.get(0));
        assertEquals(67, union.c.get(0));

        union.c.x(10);
        union.c.y(30);

        assertEquals(10, union.a.get());
        assertEquals(10, union.b.get(0));
        assertEquals(10, union.c.get(0));

        assertEquals(30, union.b.get(1));
        assertEquals(30, union.c.get(1));
    }
}