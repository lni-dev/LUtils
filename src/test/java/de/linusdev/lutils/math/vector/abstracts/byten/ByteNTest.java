package de.linusdev.lutils.math.vector.abstracts.byten;

import de.linusdev.lutils.math.vector.buffer.byten.BBByte1;
import de.linusdev.lutils.math.vector.buffer.byten.BBByteN;
import de.linusdev.lutils.math.vector.buffer.byten.BBUByte1;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ByteNTest {

    private static Stream<Arguments> provideBBVectors() {
        return Stream.of(
                Arguments.of(BBByte1.newAllocated(null), 1, 1 )
        );
    }

    @ParameterizedTest
    @MethodSource("provideBBVectors")
    public void testBufferBacked(@NotNull BBByteN vector, int memberCount, int size) {
        assertTrue(vector.isInitialised());

        assertTrue(vector.isBufferBacked());
        assertFalse(vector.isArrayBacked());
        assertFalse(vector.isView());
        assertFalse(vector.areComponentsUnsigned());

        assertEquals(memberCount, vector.getMemberCount());
        assertEquals(size, vector.getRequiredSize());

        for(byte i = 0; i < memberCount; i++) {
            vector.put(i, (byte) (i * i + 1));
            assertEquals(i * i + 1, vector.get(i));
        }

        System.out.println(vector);
    }

    @ParameterizedTest
    @MethodSource("provideBBVectors")
    public void testFillFromArray(ByteN vector, int memberCount, int size) {

        byte[] array = new byte[memberCount];
        for(int i = 0; i < memberCount; i++) {
            array[i] = (byte) (i * i + 1);
        }
        vector.fillFromArray(array);

        for(int i = 0; i < memberCount; i++) {
            assertEquals(i * i + 1, vector.get(i));
        }
    }

    @Test
    public void unsignedTest() {
        BBUByte1 vector = BBUByte1.newAllocatable(null);
        vector.allocate();

        assertTrue(vector.areComponentsUnsigned());
    }
}