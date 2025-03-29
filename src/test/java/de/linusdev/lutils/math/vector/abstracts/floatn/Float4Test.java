/*
 * Copyright (c) 2025 Linus Andera
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.linusdev.lutils.math.vector.abstracts.floatn;

import de.linusdev.lutils.math.vector.Vector;
import de.linusdev.lutils.math.vector.array.floatn.ABFloat4;
import de.linusdev.lutils.math.vector.buffer.floatn.BBFloat4;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class Float4Test {

    private static Stream<Arguments> provideVectors() {
        return Stream.of(
                Arguments.of(BBFloat4.newAllocated(null), 1.3f, 4.5f, 22f, 3f),
                Arguments.of(new ABFloat4(), 6.7f, 7.1f, 10.5f, 34f),
                Arguments.of(BBFloat4.newAllocated(null).wzyx(), 6.3f, 1.5f, 1f, 7f)
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

        //FactorView
        vector.xyzw(x, y, z, w);
        Float4 factorised = vector.createFactorizedView(-1f, 2f, .5f, -2f);

        System.out.println("vector: " + vector);
        System.out.println(" vector.factorized_xyzw(-1f, 2f, .5f, -2f): " + factorised);
        assertTrue(Vector.equals(factorised, new float[] {x * -1f, y * 2f, z * .5f, w * -2f}, 0.0f));


    }
}