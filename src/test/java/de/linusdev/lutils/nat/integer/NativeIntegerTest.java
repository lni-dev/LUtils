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

package de.linusdev.lutils.nat.integer;

import de.linusdev.lutils.nat.abi.DefaultABIs;
import de.linusdev.lutils.nat.struct.annos.SVWrapper;
import de.linusdev.lutils.nat.struct.annos.StructureLayoutSettings;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NativeIntegerTest {

    @StructureLayoutSettings(DefaultABIs.MSVC_X64)
    public static class TestMSVC_X64Layout{}

    @Test
    public void testMSVC_X64() {
        NativeInteger integer = NativeInteger.newAllocated(SVWrapper.overwriteLayout(TestMSVC_X64Layout.class));

        assertEquals(4, integer.getInfo().getRequiredSize());
        assertEquals(4, integer.getInfo().getAlignment());
        assertFalse(integer.getInfo().isCompressed());

        integer.set(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, integer.get());
        assertThrows(ArithmeticException.class, () -> integer.set(Long.MAX_VALUE));
    }

}