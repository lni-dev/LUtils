package de.linusdev.lutils.struct.abstracts;

import de.linusdev.lutils.math.matrix.buffer.floatn.BBFloat4x4;
import de.linusdev.lutils.math.vector.buffer.floatn.BBFloat1;
import de.linusdev.lutils.math.vector.buffer.floatn.BBFloat2;
import de.linusdev.lutils.math.vector.buffer.floatn.BBFloat3;
import de.linusdev.lutils.math.vector.buffer.floatn.BBFloat4;
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

    public static class Test2Struct extends ComplexStructure {

        public final @StructValue(5) BBFloat4 a = new BBFloat4(false);
        public final @StructValue(1) BBFloat1 b = new BBFloat1(false);
        public final @StructValue(2) BBFloat4 c = new BBFloat4(false);
        public final @StructValue(3) BBFloat2 d = new BBFloat2(false);
        public final @StructValue(4) BBFloat3 e = new BBFloat3(false);

        public final @StructValue(0) BBFloat4x4 f = new BBFloat4x4(false);

        public Test2Struct(boolean trackModifications, boolean allocateBuffer) {
            super(trackModifications);
            init(allocateBuffer);
        }
    }

    @Test
    void test2() {
        Test2Struct testStruct = new Test2Struct(true, true);

        assertFalse(testStruct.getInfo().isCompressed());
        assertTrue(testStruct.isInitialised());
        assertTrue(testStruct.getPointer() != 0L);
        assertEquals(0, testStruct.getOffset());

        assertEquals(16, testStruct.a.getRequiredSize());
        assertEquals(4, testStruct.b.getRequiredSize());
        assertEquals(16, testStruct.c.getRequiredSize());
        assertEquals(8, testStruct.d.getRequiredSize());
        assertEquals(16, testStruct.e.getRequiredSize());
        assertEquals(64, testStruct.f.getRequiredSize());

        assertEquals(0, testStruct.f.getOffset());
        assertEquals(128, testStruct.a.getOffset());

        System.out.println(testStruct);
    }
}