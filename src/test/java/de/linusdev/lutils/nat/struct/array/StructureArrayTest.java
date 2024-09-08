package de.linusdev.lutils.nat.struct.array;

import de.linusdev.lutils.math.vector.buffer.intn.BBInt1;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StructureArrayTest {

    @Test
    void test() {

        StructureArray<BBInt1> array = StructureArray.newAllocated(12, BBInt1.class, BBInt1::newUnallocated);

        assertNull(array.getOrNull(0));
        assertNotNull(array.get(0));
        assertNotNull(array.getOrNull(0));

        array.get(10).set(20);

        NativeArrayView<BBInt1> view = array.getView(10, 2);

        assertEquals(20, view.get(0).get());
        assertNotNull(view.get(1));

    }
}