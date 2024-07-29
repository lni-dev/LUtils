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