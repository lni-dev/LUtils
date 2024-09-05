package de.linusdev.lutils.math.special;

import de.linusdev.lutils.math.matrix.array.floatn.ABFloat4x4;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TransRotMatrixTest {

    @Test
    void position() {
        //TODO: write test
        TransRotMatrix transRotMatrix = new TransRotMatrix(new ABFloat4x4());

        System.out.println(transRotMatrix.backingMatrix);

        transRotMatrix.position().xyz(1, 2, 3);

        System.out.println(transRotMatrix.backingMatrix);


    }
}