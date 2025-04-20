package de.linusdev.lutils.optional;

import de.linusdev.lutils.optional.impl.BasicContainer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ContainerTest {

    @Test
    void requireNotNull() {

        BasicContainer<String> con = new BasicContainer<>(null, true, "test");
        BasicContainer<String> con1 = new BasicContainer<>(null, true, null);

        assertTrue(con.exists());
        assertFalse(con.isNull());
        assertDoesNotThrow(() -> con.requireNotNull());
        assertDoesNotThrow(() -> con.requireExists());

        assertTrue(con1.exists());
        assertTrue(con1.isNull());
        assertThrows(NullPointerException.class, con1::requireNotNull);
        assertDoesNotThrow(() -> con1.requireExists());

    }
}