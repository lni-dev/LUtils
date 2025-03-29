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

package de.linusdev.lutils.os;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;

import static org.junit.jupiter.api.Assertions.*;

class OsUtilsTest {

    @Test
    @EnabledOnOs(OS.LINUX)
    void testLinux() {
        assertEquals(OsType.LINUX, OsUtils.CURRENT_OS);
    }

    @Test
    @EnabledOnOs(OS.WINDOWS)
    void testWindows() {
        assertEquals(OsType.WINDOWS, OsUtils.CURRENT_OS);
    }

    @Test
    @EnabledOnOs(OS.LINUX)
    void testRequireOsTypeAnyOfLinux() {
        assertThrows(InvalidOsTypeError.class, () -> OsUtils.requireAnyOfOsTypes(OsType.WINDOWS));
        assertTrue(OsUtils.requireOsType(OsType.LINUX));
    }

    @Test
    @EnabledOnOs(OS.WINDOWS)
    void testRequireOsTypeAnyOfWindows() {
        assertThrows(InvalidOsTypeError.class, () -> OsUtils.requireAnyOfOsTypes(OsType.LINUX));
        assertTrue(OsUtils.requireOsType(OsType.WINDOWS));
    }

    @Test
    void testArch() {
        if(System.getProperty ("os.arch").equals("amd64")) {
            assertEquals(OsArchitectureType.AMD64, OsUtils.CURRENT_ARCH);
        }
    }

    @Test
    void testRequireOsArch() {
        if(System.getProperty ("os.arch").equals("amd64")) {
            assertThrows(InvalidOsArchError.class, () -> OsUtils.requireAnyOfOsArch(OsArchitectureType.X86));
            assertTrue(OsUtils.requireOsArch(OsArchitectureType.AMD64));
        } else {
            assertThrows(InvalidOsArchError.class, () -> OsUtils.requireAnyOfOsArch(OsArchitectureType.AMD64));
        }

    }
}