package de.linusdev.lutils.math.vector.abstracts.intn;

import de.linusdev.lutils.math.vector.buffer.intn.*;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class IntNTest {

    private static Stream<Arguments> provideBBVectors() {
        return Stream.of(
                Arguments.of(BBInt1.newAllocated(null), 1, 4 ),
                Arguments.of(BBInt2.newAllocated(null), 2, 8 ),
                Arguments.of(BBInt3.newAllocated(null), 3, 12),
                Arguments.of(BBInt4.newAllocated(null), 4, 16)
        );
    }

    @ParameterizedTest
    @MethodSource("provideBBVectors")
    public void testBufferBacked(@NotNull BBIntN vector, int memberCount, int size) {
        assertTrue(vector.isInitialised());

        assertTrue(vector.isBufferBacked());
        assertFalse(vector.isArrayBacked());
        assertFalse(vector.isView());
        assertFalse(vector.areComponentsUnsigned());

        assertEquals(memberCount, vector.getMemberCount());
        assertEquals(size, vector.getRequiredSize());

        for(int i = 0; i < memberCount; i++) {
            vector.put(i, i * i + 1);
            assertEquals(i * i + 1, vector.get(i));
        }

        System.out.println(vector);
    }

    @Test
    public void unsignedTest() {
        BBUInt1 uInt = BBUInt1.newAllocatable(null);
        uInt.allocate();

        assertTrue(uInt.areComponentsUnsigned());
    }
}