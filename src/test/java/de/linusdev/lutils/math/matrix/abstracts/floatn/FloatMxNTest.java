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

package de.linusdev.lutils.math.matrix.abstracts.floatn;

import de.linusdev.lutils.math.matrix.array.floatn.ABFloat4x4;
import de.linusdev.lutils.math.matrix.buffer.floatn.BBFloat4x4;
import de.linusdev.lutils.math.vector.Vector;
import de.linusdev.lutils.math.vector.abstracts.floatn.Float2;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FloatMxNTest {

    @Test
    public void test() {
        Float4x4 abFloat = new ABFloat4x4();
        Float4x4 bbFloat = BBFloat4x4.newAllocated(null);

        assertTrue(abFloat.isArrayBacked());
        assertTrue(bbFloat.isBufferBacked());
        assertFalse(abFloat.isBufferBacked());
        assertFalse(bbFloat.isArrayBacked());

        assertTrue(Vector.equals(abFloat, bbFloat, 0.f));
    }

    @Test
    void createFloat2View() {

        Float4x4 mat = new ABFloat4x4();
        Float2 view = mat.createFloat2View(1, 1, 2,3);

        mat.put(1, 1, 10f);
        assertEquals(10f, mat.get(1, 1));
        assertEquals(10f, view.get(0));

        view.put(1, 35f);

        assertEquals(35f, mat.get(2, 3));
        assertEquals(35f, view.get(1));

    }

    @Test
    void createFloat3x3View() {
        Float4x4 mat = new ABFloat4x4();
        Float3x3 view = mat.createFloat3x3View(
                0,0,  0,1, 0,2,
                1,0, 1,1, 1,2,
                2,0, 2,1, 2,2
        );

        mat.put(2, 2, 10f);
        assertEquals(10f, view.get(2, 2));
    }
}