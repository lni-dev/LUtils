package de.linusdev.lutils.math.vector.abstracts.floatn;

import de.linusdev.lutils.math.vector.array.floatn.ABFloat4;
import de.linusdev.lutils.math.vector.buffer.floatn.BBFloat1;
import de.linusdev.lutils.math.vector.buffer.floatn.BBFloat2;
import de.linusdev.lutils.math.vector.buffer.floatn.BBFloat3;
import de.linusdev.lutils.math.vector.buffer.floatn.BBFloat4;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class Float4Test {

    private static Stream<Arguments> provideVectors() {
        return Stream.of(
                Arguments.of(new BBFloat4(true), 1.3f, 4.5f, 22f, 3f),
                Arguments.of(new ABFloat4(), 6.7f, 7.1f, 10.5f, 34f),
                Arguments.of(new BBFloat4(true).wzyx(), 6.3f, 1.5f, 1f, 7f)
        );
    }

    @ParameterizedTest
    @MethodSource("provideVectors")
    void test(@NotNull Float4 vector, float x, float y, float z, float w) {
        vector.xyzw(x, y, z, w);

        assertEquals(x, vector.x());
        assertEquals(y, vector.y());
        assertEquals(z, vector.z());
        assertEquals(w, vector.w());

        assertEquals(w, vector.wzyx().x());
        assertEquals(z, vector.wzyx().y());
        assertEquals(y, vector.wzyx().z());
        assertEquals(x, vector.wzyx().w());



        vector.xyz(x, x, x);

        assertEquals(x, vector.x());
        assertEquals(x, vector.y());
        assertEquals(x, vector.z());
        assertEquals(w, vector.w());



        vector.wzyx().xyz(y, y, y);

        assertEquals(x, vector.x());
        assertEquals(y, vector.y());
        assertEquals(y, vector.z());
        assertEquals(y, vector.w());

        assertEquals(x, vector.xxx().x());
        assertEquals(x, vector.xxx().y());
        assertEquals(x, vector.xxx().z());



        vector.x(w);
        vector.w(x);
        vector.xyz(vector.xxx());

        assertEquals(w, vector.x());
        assertEquals(w, vector.y());
        assertEquals(w, vector.z());
        assertEquals(x, vector.w());

        vector.z(z);
        assertEquals(z, vector.z());

        vector.y(z);
        assertEquals(z, vector.y());

        vector.xyz().xyz(x, y, z);
        assertEquals(x, vector.x());
        assertEquals(y, vector.y());
        assertEquals(z, vector.z());
    }
}