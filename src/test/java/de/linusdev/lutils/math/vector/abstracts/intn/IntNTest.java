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
                Arguments.of(new BBInt1(true), 1, 4 ),
                Arguments.of(new BBInt2(true), 2, 8 ),
                Arguments.of(new BBInt3(true), 3, 16),
                Arguments.of(new BBInt4(true), 4, 16)
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
        BBUInt1 uInt = new BBUInt1();
        uInt.allocate();

        assertTrue(uInt.areComponentsUnsigned());
    }
}