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

package de.linusdev.lutils.struct.utils;

import de.linusdev.lutils.math.vector.buffer.floatn.BBFloat1;
import de.linusdev.lutils.nat.pointer.BBTypedPointer64;
import de.linusdev.lutils.nat.struct.annos.StructValue;
import de.linusdev.lutils.nat.struct.utils.SSMUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SSMUtilsTest {

    @Test
    void getNewUnallocatedMethod() throws NoSuchMethodException {

        var method = SSMUtils.getNewUnallocatedMethod(BBFloat1.class);
        assertEquals(BBFloat1.class.getMethod("newUnallocated") , method.getMethod());

        method = SSMUtils.getNewUnallocatedMethod(BBTypedPointer64.class);
        assertEquals(BBTypedPointer64.class.getMethod("newUnallocated1"), method.getMethod());
    }

    @Test
    void getNewAllocatableMethod() throws NoSuchMethodException {
        var method = SSMUtils.getNewAllocatableMethod(BBFloat1.class);
        assertEquals(BBFloat1.class.getMethod("newAllocatable", StructValue.class) , method.getMethod());

        method = SSMUtils.getNewAllocatableMethod(BBTypedPointer64.class);
        assertEquals(BBTypedPointer64.class.getMethod("newAllocatable1", StructValue.class), method.getMethod());
    }

    @Test
    void getNewAllocatedMethod() throws NoSuchMethodException {
        var method = SSMUtils.getNewAllocatedMethod(BBFloat1.class);
        assertEquals(BBFloat1.class.getMethod("newAllocated", StructValue.class) , method.getMethod());

        method = SSMUtils.getNewAllocatedMethod(BBTypedPointer64.class);
        assertEquals(BBTypedPointer64.class.getMethod("newAllocated1", StructValue.class), method.getMethod());
    }
}