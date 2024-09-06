package de.linusdev.lutils.math.special;

import de.linusdev.lutils.math.VMath;
import de.linusdev.lutils.math.matrix.Matrix;
import de.linusdev.lutils.math.matrix.array.floatn.ABFloat4x4;
import de.linusdev.lutils.math.vector.Vector;
import de.linusdev.lutils.math.vector.array.floatn.ABFloat3;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CameraMatrixTest {

    @Test
    void test() {
        ABFloat4x4 backing = new ABFloat4x4();
        CameraMatrix camera = new CameraMatrix(backing, new ABFloat4x4());


        camera.lookAt(new ABFloat3(1.4f, 5.3f, 1f));
        camera.calculateViewMatrix();

        assertTrue(Matrix.equals(camera.viewMatrix(), VMath.inverse(camera.backingMatrix(), new ABFloat4x4()), 0.0001f));

        camera.position().xyz(5f, 12f, 13f);
        camera.lookAt(new ABFloat3(1.4f, 5.3f, 1f));
        camera.calculateViewMatrix();

        assertTrue(Matrix.equals(camera.viewMatrix(), VMath.inverse(camera.backingMatrix(), new ABFloat4x4()), 0.0001f));

        camera.setWorldUp(VMath.normalize(new ABFloat3(1f, 2f, 3f), new ABFloat3()));
        
        assertTrue(Vector.equals(camera.worldUp, VMath.normalize(new ABFloat3(1f, 2f, 3f), new ABFloat3()), 0.0001f));

    }
}