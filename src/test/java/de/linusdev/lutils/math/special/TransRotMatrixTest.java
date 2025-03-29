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

package de.linusdev.lutils.math.special;

import de.linusdev.lutils.math.VMath;
import de.linusdev.lutils.math.matrix.Matrix;
import de.linusdev.lutils.math.matrix.abstracts.floatn.Float3x3;
import de.linusdev.lutils.math.matrix.array.floatn.ABFloat3x3;
import de.linusdev.lutils.math.matrix.array.floatn.ABFloat4x4;
import de.linusdev.lutils.math.vector.array.floatn.ABFloat3;
import org.junit.jupiter.api.Test;

import static de.linusdev.lutils.math.LMath.PIf;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TransRotMatrixTest {

    @Test
    void test() {

        TransRotMatrix transRotMatrix = new TransRotMatrix(new ABFloat4x4());

        transRotMatrix.position().xyz(1, 2, 3);

        assertEquals(-1, transRotMatrix.backingMatrix.get(0, 3));
        assertEquals(-2, transRotMatrix.backingMatrix.get(1, 3));
        assertEquals(-3, transRotMatrix.backingMatrix.get(2, 3));
        assertEquals( 1, transRotMatrix.backingMatrix.get(3, 3));

        transRotMatrix.translation().xyz(2, 4, 6);

        assertEquals(2, transRotMatrix.backingMatrix.get(0, 3));
        assertEquals(4, transRotMatrix.backingMatrix.get(1, 3));
        assertEquals(6, transRotMatrix.backingMatrix.get(2, 3));
        assertEquals(1, transRotMatrix.backingMatrix.get(3, 3));

        transRotMatrix.setRotation(PIf * .32f, new ABFloat3(0, 0, 1));

        Float3x3 rotView = transRotMatrix.backingMatrix.createFloat3x3View(
                0, 0, 0, 1,0, 2,
                1, 0, 1, 1, 1, 2,
                2, 0, 2, 1, 2, 2
        );
        
        assertTrue(Matrix.equals(rotView, new float[] {
                0.5358267426f, -0.8443279266f, 0.00f,
                0.8443279266f, 0.5358267426f, 0.00f,
                0.00f, 0.00f, 1.00f,
        }, 0.0001f));


        transRotMatrix.setRotation(PIf * 1.1f, PIf * 0.3f, PIf * 0.1f);

        assertTrue(Matrix.equals(rotView, new float[] {
                -0.5590168834f,   0.0561286882f,  -0.8272542953f,
                -0.1816357374f,  -0.9817627668f,   0.0561283119f,
                -0.8090170622f,   0.1816356182f,   0.5590169430f,
        }, 0.0001f));

        transRotMatrix.addRotation(0.1f, 0.2f, 0.3f);

        assertTrue(Matrix.equals(rotView, VMath.rotationMatrix(PIf * 1.1f + 0.1f, PIf * 0.3f + 0.2f, PIf * 0.1f + 0.3f, new ABFloat3x3()), 0.0001f));

    }
}