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
import de.linusdev.lutils.math.matrix.abstracts.floatn.Float4x4;
import de.linusdev.lutils.math.matrix.array.floatn.ABFloat4x4;
import de.linusdev.lutils.math.matrix.buffer.floatn.BBFloat4x4;
import de.linusdev.lutils.math.vector.Vector;
import de.linusdev.lutils.math.vector.array.floatn.ABFloat3;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class CameraMatrixTest {

    @Test
    void test() {
        Float4x4 backing = BBFloat4x4.newAllocated(null);
        CameraMatrix camera = new CameraMatrix(backing, BBFloat4x4.newAllocated(null));


        camera.lookAt(new ABFloat3(1.4f, 5.3f, 1f));
        camera.calculateViewMatrix();

        assertTrue(Matrix.equals(camera.viewMatrix(), VMath.inverse(camera.backingMatrix(), new ABFloat4x4()), 0.0001f));

        camera.position().xyz(5f, 12f, 13f);
        camera.lookAt(new ABFloat3(1.4f, 5.3f, 1f));
        camera.calculateViewMatrix();

        assertTrue(Matrix.equals(camera.viewMatrix(), VMath.inverse(camera.backingMatrix(), new ABFloat4x4()), 0.0001f));

        camera.setWorldUp(VMath.normalize(new ABFloat3(1f, 2f, 3f), new ABFloat3()));
        
        assertTrue(Vector.equals(camera.worldUp, VMath.normalize(new ABFloat3(1f, 2f, 3f), new ABFloat3()), 0.0001f));

        camera.calculateViewMatrix();
        System.out.println(camera.backingViewMatrix);

    }
}