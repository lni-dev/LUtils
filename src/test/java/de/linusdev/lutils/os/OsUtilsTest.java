package de.linusdev.lutils.os;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;

import static org.junit.jupiter.api.Assertions.*;

class OsUtilsTest {

    @Test
    @EnabledOnOs(OS.LINUX)
    void testLinux() {
        assertEquals(OSType.LINUX, OsUtils.CURRENT_OS);
    }

    @Test
    @EnabledOnOs(OS.WINDOWS)
    void testWindows() {
        assertEquals(OSType.WINDOWS, OsUtils.CURRENT_OS);
    }
}