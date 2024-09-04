package de.linusdev.lutils.math.special;

import de.linusdev.lutils.math.VMath;
import de.linusdev.lutils.math.matrix.Matrix;
import de.linusdev.lutils.math.matrix.MatrixMemoryLayout;
import de.linusdev.lutils.math.matrix.array.floatn.ABFloat4x4;
import de.linusdev.lutils.math.vector.array.floatn.ABFloat3;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class CameraMatrixTest {

    @Test
    void test() {
        //TODO: test this properly
        ABFloat4x4 backing = new ABFloat4x4();
        backing.setMemoryLayout(MatrixMemoryLayout.COLUMN_MAJOR);
        CameraMatrix camera = new CameraMatrix(backing, new ABFloat4x4());


        camera.lookAt(new ABFloat3(1.4f, 5.3f, 1f));
        camera.calculateViewMatrix();

        System.out.println("backingMatrix: " + camera.backingMatrix());
        System.out.println("viewMatrix: " + camera.viewMatrix());
        System.out.println("inverse(backingMatrix): " + VMath.inverse(camera.backingMatrix(), new ABFloat4x4()));

        assertTrue(Matrix.equals(camera.viewMatrix(), VMath.inverse(camera.backingMatrix(), new ABFloat4x4()), 0.0001f));

        camera.position().xyz(5f, 12f, 13f);
        camera.lookAt(new ABFloat3(1.4f, 5.3f, 1f));
        camera.calculateViewMatrix();

        assertTrue(Matrix.equals(camera.viewMatrix(), VMath.inverse(camera.backingMatrix(), new ABFloat4x4()), 0.0001f));


        System.out.println("backingMatrix: " + camera.backingMatrix());


        System.out.println(Arrays.toString((float[])camera.backingMatrix.getArray()));

    }
}