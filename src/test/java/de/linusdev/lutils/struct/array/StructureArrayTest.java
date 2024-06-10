package de.linusdev.lutils.struct.array;

import de.linusdev.lutils.nat.struct.array.StructureArray;
import de.linusdev.lutils.struct.abstracts.ComplexStructureTest;
import de.linusdev.lutils.nat.struct.annos.SVWrapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StructureArrayTest {

    @Test
    void test() {
        StructureArray<ComplexStructureTest.TestStruct> array = StructureArray.newAllocated(
                true,
                SVWrapper.of(10, ComplexStructureTest.TestStruct.class),
                null,
                () -> new ComplexStructureTest.TestStruct(true, false)
        );

        ComplexStructureTest.TestStruct element = array.getOrCreate(0);
        assertTrue(element.isInitialised());

        assertEquals(0, element.getOffset());
        assertEquals(480, array.getRequiredSize());
        assertEquals(8, array.getAlignment());
        assertEquals(10, array.length());

        System.out.println(array);
    }
}