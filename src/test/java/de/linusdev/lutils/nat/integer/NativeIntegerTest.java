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

package de.linusdev.lutils.nat.integer;

import de.linusdev.lutils.nat.abi.ABIs;
import org.junit.jupiter.api.Test;

import static de.linusdev.lutils.nat.memory.Allocators.allocManaged;
import static org.junit.jupiter.api.Assertions.*;

class NativeIntegerTest {

    @Test
    public void testMSVC_X64() {
        NativeInteger integer = allocManaged(NativeInteger.newAllocatable(ABIs.MSVC_X64));

        assertEquals(4, integer.getInfo().getRequiredSize());
        assertEquals(4, integer.getInfo().getAlignment());
        assertFalse(integer.getInfo().isCompressed());

        integer.set(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, integer.get());
        assertThrows(ArithmeticException.class, () -> integer.set(Long.MAX_VALUE));
    }

}