package de.linusdev.lutils.struct.array;

import de.linusdev.lutils.struct.abstracts.ComplexStructureTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StructureArrayTest {

    @Test
    void test() {
        StructureArray<ComplexStructureTest.TestStruct> array = new StructureArray<>(
                true,
                true,
                ComplexStructureTest.TestStruct.class,
                10,
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