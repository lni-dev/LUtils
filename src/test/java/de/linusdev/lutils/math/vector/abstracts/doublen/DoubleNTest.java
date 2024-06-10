package de.linusdev.lutils.math.vector.abstracts.doublen;

import de.linusdev.lutils.math.vector.buffer.doublen.BBDouble1;
import de.linusdev.lutils.math.vector.buffer.doublen.BBDoubleN;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class DoubleNTest {

    private static Stream<Arguments> provideBBVectors() {
        return Stream.of(
                Arguments.of(BBDouble1.newAllocated(null), 1, 8 )
        );
    }

    @ParameterizedTest
    @MethodSource("provideBBVectors")
    public void testBufferBacked(@NotNull BBDoubleN vector, int memberCount, int size) {
        assertTrue(vector.isInitialised());

        assertTrue(vector.isBufferBacked());
        assertFalse(vector.isArrayBacked());
        assertFalse(vector.isView());

        assertEquals(memberCount, vector.getMemberCount());
        assertEquals(size, vector.getRequiredSize());

        for(int i = 0; i < memberCount; i++) {
            vector.put(i, i * i + 1);
            assertEquals(i * i + 1, vector.get(i));
        }

        System.out.println(vector);
    }

    @ParameterizedTest
    @MethodSource("provideBBVectors")
    public void testFillFromArray(DoubleN vector, int memberCount, int size) {

        double[] array = new double[memberCount];
        for(int i = 0; i < memberCount; i++) {
            array[i] = i * i + 1;
        }
        vector.fillFromArray(array);

        for(int i = 0; i < memberCount; i++) {
            assertEquals(i * i + 1, vector.get(i));
        }
    }

    @Test
    public void testView() {
        Double1 vector = BBDouble1.newAllocated(null);
        vector.set(0);
        Double1 factorView = vector.createFactorizedView(2.0);
        Double1 view = Double1.createView(vector, new int[]{0});

        assertEquals(0.0, factorView.get());
        assertEquals(0.0, view.get());
        vector.set(4);
        assertEquals(8.0, factorView.get());
        assertEquals(4.0, view.get());
    }
}