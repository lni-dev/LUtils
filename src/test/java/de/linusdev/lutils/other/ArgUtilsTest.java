package de.linusdev.lutils.other;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ArgUtilsTest {

    @Test
    void requireGreater() {
        assertThrows(IllegalArgumentException.class, () -> ArgUtils.requireGreater(0, 0, "test"));
        assertDoesNotThrow(() -> ArgUtils.requireGreater(1, 0, "test"));

        assertThrows(IllegalArgumentException.class, () -> ArgUtils.requireGreater(0.0, 0.0, 1.0, "test"));
        assertDoesNotThrow(() -> ArgUtils.requireGreater(1, 0, 0.9, "test"));
    }

    @Test
    void requireGreaterOrEqual() {
        assertThrows(IllegalArgumentException.class, () -> ArgUtils.requireGreaterOrEqual(-1, 0, "test"));
        assertDoesNotThrow(() -> ArgUtils.requireGreaterOrEqual(0, 0, "test"));
    }

    @Test
    void requireLess() {
        assertThrows(IllegalArgumentException.class, () -> ArgUtils.requireLess(0, 0, "test"));
        assertDoesNotThrow(() -> ArgUtils.requireLess(-1, 0, "test"));    }

    @Test
    void requireLessOrEqual() {
        assertThrows(IllegalArgumentException.class, () -> ArgUtils.requireLessOrEqual(1, 0, "test"));
        assertDoesNotThrow(() -> ArgUtils.requireLessOrEqual(0, 0, "test"));
    }
}