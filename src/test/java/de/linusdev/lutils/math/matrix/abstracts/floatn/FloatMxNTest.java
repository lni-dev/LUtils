package de.linusdev.lutils.math.matrix.abstracts.floatn;

import de.linusdev.lutils.math.VMath;
import de.linusdev.lutils.math.matrix.array.floatn.ABFloat4x4;
import de.linusdev.lutils.math.matrix.buffer.floatn.BBFloat4x4;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FloatMxNTest {

    @Test
    public void test() {
        Float4x4 abFloat = new ABFloat4x4();
        Float4x4 bbFloat = BBFloat4x4.newAllocated(null);

        assertTrue(abFloat.isArrayBacked());
        assertTrue(bbFloat.isBufferBacked());
        assertFalse(abFloat.isBufferBacked());
        assertFalse(bbFloat.isArrayBacked());

        assertTrue(VMath.equals(abFloat, bbFloat, 0.f));
    }

}