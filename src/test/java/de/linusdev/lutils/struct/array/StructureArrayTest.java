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

package de.linusdev.lutils.struct.array;

import de.linusdev.lutils.nat.struct.annos.SVWrapper;
import de.linusdev.lutils.nat.struct.array.StructureArray;
import de.linusdev.lutils.struct.abstracts.ComplexStructureTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StructureArrayTest {

    @Test
    void test() {
        StructureArray<ComplexStructureTest.TestStruct> array = StructureArray.newAllocated(
                true,
                SVWrapper.of(10, ComplexStructureTest.TestStruct.class),
                null,
                () -> new ComplexStructureTest.TestStruct(true, false)
        );

        ComplexStructureTest.TestStruct element = array.get(0);
        assertTrue(element.isInitialised());

        assertEquals(0, element.getOffset());
        assertEquals(480, array.getRequiredSize());
        assertEquals(8, array.getAlignment());
        assertEquals(10, array.length());

        System.out.println(array);
    }
}