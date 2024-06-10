package de.linusdev.lutils.math.vector.abstracts.floatn;

import de.linusdev.lutils.math.vector.array.floatn.*;
import de.linusdev.lutils.math.vector.buffer.floatn.*;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class FloatNTest {

    private static Stream<Arguments> provideBBVectors() {
        return Stream.of(
                Arguments.of(BBFloat1.newAllocated(null), 1, 4 ),
                Arguments.of(BBFloat2.newAllocated(null), 2, 8 ),
                Arguments.of(BBFloat3.newAllocated(null), 3, 12),
                Arguments.of(BBFloat4.newAllocated(null), 4, 16)
        );
    }

    @ParameterizedTest
    @MethodSource("provideBBVectors")
    public void testBufferBacked(@NotNull BBFloatN vector, int memberCount, int size) {
        assertTrue(vector.isInitialised());

        assertTrue(vector.isBufferBacked());
        assertFalse(vector.isArrayBacked());
        assertFalse(vector.isView());

        assertEquals(memberCount, vector.getMemberCount());
        assertEquals(size, vector.getRequiredSize());

        for(int i = 0; i < memberCount; i++) {
            vector.put(i, i * i + 1);
            assertEquals((float) (i * i +1), vector.get(i));
        }

        System.out.println(vector);
    }

    private static Stream<Arguments> provideABVectors() {
        return Stream.of(
                Arguments.of(new ABFloat1(), 1),
                Arguments.of(new ABFloat2(), 2),
                Arguments.of(new ABFloat3(), 3),
                Arguments.of(new ABFloat4(), 4)
        );
    }

    @ParameterizedTest
    @MethodSource("provideABVectors")
    public void testArrayBacked(@NotNull ABFloatN vector, int memberCount) {
        assertFalse(vector.isBufferBacked());
        assertTrue(vector.isArrayBacked());
        assertFalse(vector.isView());

        assertEquals(memberCount, vector.getMemberCount());

        for(int i = 0; i < memberCount; i++) {
            vector.put(i, i * i + 1);
            assertEquals((float) (i * i +1), vector.get(i));
        }

        System.out.println(vector);
    }

}