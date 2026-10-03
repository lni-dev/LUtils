package de.linusdev.lutils.config;

import de.linusdev.lutils.result.BiResult;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CfgValueTest {

    private static final @NotNull CfgValue<String> TEST = new CfgValue<>("some-key", s->s);
    private static final @NotNull CfgValue<Integer> TEST2 = new CfgValue<>("k2", Integer::parseInt, integer -> new BiResult<>(integer < 1, "must be smaller than 1."));

    @Test
    void get() {

        assertNull(TEST.get());
        System.setProperty("some-key", "hallo");
        Configuration.load(TEST);
        assertEquals("hallo", TEST.get());

        System.setProperty("k2", "-10");
        Configuration.load(TEST2);
        assertEquals(-10, TEST2.get());

        System.setProperty("k2", "1");
        Configuration.load(TEST2);
        assertEquals(-10, TEST2.get());

        assertThrows(IllegalStateException.class, () -> TEST2.set("10"));

        try {
            TEST2.set("10");
        } catch (Throwable t) {
            assertEquals("Changing the config value 'k2' is not allowed. Reason: must be smaller than 1.", t.getMessage());
        }

    }
}