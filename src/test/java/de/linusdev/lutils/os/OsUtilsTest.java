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