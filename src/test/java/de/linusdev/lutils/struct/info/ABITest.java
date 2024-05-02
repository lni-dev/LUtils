package de.linusdev.lutils.struct.info;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ABITest {

    @Test
    void MSVC_X64_calculateStructureLayout() {
        StructureInfo info;

        // https://learn.microsoft.com/en-us/cpp/build/x64-software-conventions?view=msvc-170#example-3
        info = DefaultABIs.MSVC_X64.calculateStructureLayout(
                false,
                new StructureInfo(1, false, 0, 1, 0), // c-char
                new StructureInfo(2, false, 0, 2, 0), // c-short
                new StructureInfo(1, false, 0, 1, 0), // c-char
                new StructureInfo(4, false, 0, 4, 0)  // c-int
        );

        assertEquals(12, info.getRequiredSize());
        assertEquals(4, info.getAlignment());
        assertArrayEquals(new int[] {0, 1, 1, 2, 0, 1, 3, 4, 0}, info.getSizes());

        // https://learn.microsoft.com/en-us/cpp/build/x64-software-conventions?view=msvc-170#example-2
        info = DefaultABIs.MSVC_X64.calculateStructureLayout(
                false,
                new StructureInfo(4, false, 0, 4, 0), // c-int
                new StructureInfo(8, false, 0, 8, 0), // c-double
                new StructureInfo(2, false, 0, 2, 0)  // c-short
        );

        assertEquals(24, info.getRequiredSize());
        assertEquals(8, info.getAlignment());
        assertArrayEquals(new int[] {0, 4, 4, 8, 0, 2, 6}, info.getSizes());

        // Compress
        info = DefaultABIs.MSVC_X64.calculateStructureLayout(
                true,
                new StructureInfo(4, false, 0, 4, 0), // c-int
                new StructureInfo(8, false, 0, 8, 0), // c-double
                new StructureInfo(2, false, 0, 2, 0)  // c-short
        );

        assertEquals(14, info.getRequiredSize());
        assertEquals(1, info.getAlignment());
        assertArrayEquals(new int[] {0, 4, 0, 8, 0, 2, 0}, info.getSizes());
    }
}