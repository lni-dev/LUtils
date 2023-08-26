package de.linusdev.lutils.struct.abstracts;

import de.linusdev.lutils.struct.annos.FixedLength;
import de.linusdev.lutils.struct.annos.StructValue;
import de.linusdev.lutils.struct.array.PrimitiveTypeArray;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ComplexStructureTest {

    @Test
    void test() {
        TestStruct testStruct = new TestStruct(true, true);

        assertFalse(testStruct.getInfo().isCompressed());

        assertTrue(testStruct.isInitialised());
        assertTrue(testStruct.pArray.isInitialised());
        assertTrue(testStruct.dArray.isInitialised());

        assertTrue(testStruct.getPointer() != 0L);

        assertEquals(0, testStruct.getOffset());
        assertEquals(0, testStruct.pArray.getOffset());
        assertEquals(testStruct.pArray.getRequiredSize() + 4, testStruct.dArray.getOffset());

        assertEquals(testStruct, testStruct.getMostParentStructure());
        assertEquals(testStruct, testStruct.pArray.getMostParentStructure());
        assertEquals(testStruct, testStruct.dArray.getMostParentStructure());

        System.out.println(testStruct);

    }

    public static class TestStruct extends ComplexStructure {

        public final @StructValue @FixedLength(value = 5, elementTypes = Float.class)
        PrimitiveTypeArray<Float> pArray = new PrimitiveTypeArray<>(Float.class, 5, false);

        public final @StructValue @FixedLength(value = 3, elementTypes = Double.class)
        PrimitiveTypeArray<Double> dArray = new PrimitiveTypeArray<>(Double.class, 3, false);

        public TestStruct(boolean trackModifications, boolean allocateBuffer) {
            super(trackModifications);
            init(allocateBuffer);
        }
    }
}