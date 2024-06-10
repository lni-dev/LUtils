package de.linusdev.lutils.math.vector.abstracts.shortn;

import de.linusdev.lutils.math.vector.buffer.shortn.BBShort1;
import de.linusdev.lutils.math.vector.buffer.shortn.BBShortN;
import de.linusdev.lutils.math.vector.buffer.shortn.BBUShort1;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ShortNTest {

    private static Stream<Arguments> provideBBVectors() {
        return Stream.of(
                Arguments.of(BBShort1.newAllocated(null), 1, 2 )
        );
    }

    @ParameterizedTest
    @MethodSource("provideBBVectors")
    public void testBufferBacked(@NotNull BBShortN vector, int memberCount, int size) {
        assertTrue(vector.isInitialised());

        assertTrue(vector.isBufferBacked());
        assertFalse(vector.isArrayBacked());
        assertFalse(vector.isView());
        assertFalse(vector.areComponentsUnsigned());

        assertEquals(memberCount, vector.getMemberCount());
        assertEquals(size, vector.getRequiredSize());

        for(short i = 0; i < memberCount; i++) {
            vector.put(i, (short) (i * i + 1));
            assertEquals(i * i + 1, vector.get(i));
        }

        System.out.println(vector);
    }

    @ParameterizedTest
    @MethodSource("provideBBVectors")
    public void testFillFromArray(ShortN vector, int memberCount, int size) {

        short[] array = new short[memberCount];
        for(int i = 0; i < memberCount; i++) {
            array[i] = (short) (i * i + 1);
        }
        vector.fillFromArray(array);

        for(int i = 0; i < memberCount; i++) {
            assertEquals(i * i + 1, vector.get(i));
        }
    }

    @Test
    public void unsignedTest() {
        BBUShort1 vector = BBUShort1.newAllocatable(null);
        vector.allocate();

        assertTrue(vector.areComponentsUnsigned());
    }
}