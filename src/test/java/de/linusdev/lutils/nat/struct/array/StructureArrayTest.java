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

package de.linusdev.lutils.nat.struct.array;

import de.linusdev.lutils.math.vector.buffer.intn.BBInt1;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StructureArrayTest {

    @Test
    void test() {

        StructureArray<BBInt1> array = StructureArray.newAllocated(12, BBInt1.class, BBInt1::newUnallocated);

        assertNull(array.getOrNull(0));
        assertNotNull(array.get(0));
        assertNotNull(array.getOrNull(0));

        array.get(10).set(20);

        NativeArrayView<BBInt1> view = array.getView(10, 2);

        assertEquals(20, view.get(0).get());
        assertNotNull(view.get(1));

    }
}