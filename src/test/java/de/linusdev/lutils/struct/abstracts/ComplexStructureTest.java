package de.linusdev.lutils.struct.abstracts;

import de.linusdev.lutils.struct.annos.FixedLength;
import de.linusdev.lutils.struct.annos.StructValue;
import de.linusdev.lutils.struct.array.PrimitiveTypeArray;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ComplexStructureTest {

    @Test
    void test() {
        TestStruct testStruct = new TestStruct(true);

        assertFalse(testStruct.getInfo().isCompressed());

        System.out.println(testStruct);

    }

    public static class TestStruct extends ComplexStructure {

        public final @StructValue @FixedLength(value = 5, elementTypes = Float.class)
        PrimitiveTypeArray<Float> pArray = new PrimitiveTypeArray<>(Float.class, 5, false);

        public final @StructValue @FixedLength(value = 3, elementTypes = Double.class)
        PrimitiveTypeArray<Double> dArray = new PrimitiveTypeArray<>(Double.class, 3, false);

        public TestStruct(boolean trackModifications) {
            super(trackModifications);
            init(true);
        }
    }
}