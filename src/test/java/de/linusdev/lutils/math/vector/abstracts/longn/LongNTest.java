package de.linusdev.lutils.math.vector.abstracts.longn;

import de.linusdev.lutils.math.vector.buffer.longn.*;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class LongNTest {
    private static Stream<Arguments> provideBBVectors() {
        return Stream.of(
                Arguments.of(BBLong1.newAllocated(null), 1, 8 ),
                Arguments.of(BBLong2.newAllocated(null), 2, 16 ),
                Arguments.of(BBLong3.newAllocated(null), 3, 24),
                Arguments.of(BBLong4.newAllocated(null), 4, 32)
        );
    }

    @ParameterizedTest
    @MethodSource("provideBBVectors")
    public void testBufferBacked(@NotNull BBLongN vector, int memberCount, int size) {
        assertTrue(vector.isInitialised());

        assertTrue(vector.isBufferBacked());
        assertFalse(vector.isArrayBacked());
        assertFalse(vector.isView());
        assertFalse(vector.areComponentsUnsigned());

        assertEquals(memberCount, vector.getMemberCount());
        assertEquals(size, vector.getRequiredSize());

        for(int i = 0; i < memberCount; i++) {
            vector.put(i, (long) i * i + 1);
            assertEquals((long) i * i + 1, vector.get(i));
        }

        System.out.println(vector);
    }

    @Test
    public void unsignedTest() {
        BBULong1 uLong = BBULong1.newAllocatable(null);
        uLong.allocate();

        assertTrue(uLong.areComponentsUnsigned());
    }
}