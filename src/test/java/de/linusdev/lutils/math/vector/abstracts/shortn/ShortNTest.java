/*
 * Copyright (c) 2025-2026 Linus Andera
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

package de.linusdev.lutils.math.vector.abstracts.shortn;

import de.linusdev.lutils.math.vector.buffer.shortn.BBShort1;
import de.linusdev.lutils.math.vector.buffer.shortn.BBShortN;
import de.linusdev.lutils.math.vector.buffer.shortn.BBUShort1;
import de.linusdev.lutils.nat.struct.abstracts.Structure;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static de.linusdev.lutils.nat.memory.Allocators.allocManaged;
import static org.junit.jupiter.api.Assertions.*;

class ShortNTest {

    private static Stream<Arguments> provideBBVectors() {
        return Stream.of(
                Arguments.of(BBShort1.newAllocatable(null), 1, 2 )
        );
    }

    @ParameterizedTest
    @MethodSource("provideBBVectors")
    public void testBufferBacked(@NotNull BBShortN vector, int memberCount, int size) {
        allocManaged(vector);
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
        allocManaged((Structure) vector);
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
        BBUShort1 vector = allocManaged(BBUShort1.newAllocatable(null));

        assertTrue(vector.areComponentsUnsigned());
    }
}