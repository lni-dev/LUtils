package de.linusdev.lutils.struct.abstracts;

import de.linusdev.lutils.math.matrix.buffer.floatn.BBFloat4x4;
import de.linusdev.lutils.math.vector.buffer.floatn.BBFloat1;
import de.linusdev.lutils.math.vector.buffer.floatn.BBFloat2;
import de.linusdev.lutils.math.vector.buffer.floatn.BBFloat3;
import de.linusdev.lutils.math.vector.buffer.floatn.BBFloat4;
import de.linusdev.lutils.math.vector.buffer.intn.BBInt1;
import de.linusdev.lutils.math.vector.buffer.longn.BBLong1;
import de.linusdev.lutils.nat.abi.OverwriteChildABI;
import de.linusdev.lutils.nat.array.NativeFloat32Array;
import de.linusdev.lutils.nat.array.NativeFloat64Array;
import de.linusdev.lutils.nat.struct.abstracts.ComplexStructure;
import de.linusdev.lutils.nat.struct.annos.StructValue;
import de.linusdev.lutils.nat.struct.annos.StructureLayoutSettings;
import de.linusdev.lutils.nat.abi.ABI;
import de.linusdev.lutils.nat.abi.DefaultABIs;
import de.linusdev.lutils.nat.struct.annos.StructureSettings;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ComplexStructureTest {

    public static class TestStruct extends ComplexStructure {

        public final @StructValue(length = 5)
        NativeFloat32Array pArray = new NativeFloat32Array();

        public final @StructValue(length = 3)
        NativeFloat64Array dArray = new NativeFloat64Array();

        public TestStruct(boolean trackModifications, boolean allocateBuffer) {
            super(trackModifications);
            init(null, true);
            if(allocateBuffer)
                allocate();
        }
    }

    public static class Test2Struct extends ComplexStructure {

        public final @StructValue(5) BBFloat4 a = BBFloat4.newUnallocated();
        public final @StructValue(1) BBFloat1 b = BBFloat1.newUnallocated();
        public final @StructValue(2) BBFloat4 c = BBFloat4.newUnallocated();
        public final @StructValue(3) BBFloat2 d = BBFloat2.newUnallocated();
        public final @StructValue(4) BBFloat3 e = BBFloat3.newUnallocated();

        public final @StructValue(0) BBFloat4x4 f = BBFloat4x4.newUnallocated();

        public Test2Struct(boolean trackModifications, boolean allocateBuffer) {
            super(trackModifications);
            init(null, true);
            if(allocateBuffer)
                allocate();
        }
    }

    @StructureLayoutSettings(
            value = DefaultABIs.CVG4J_OPEN_CL,
            overwriteChildrenABI = OverwriteChildABI.FORCE_OVERWRITE
    )
    public static class TestOpenCLStruct extends ComplexStructure {

        public final @StructValue(5) BBFloat4 a = BBFloat4.newUnallocated();
        public final @StructValue(1) BBFloat1 b = BBFloat1.newUnallocated();
        public final @StructValue(2) BBFloat4 c = BBFloat4.newUnallocated();
        public final @StructValue(3) BBFloat2 d = BBFloat2.newUnallocated();
        public final @StructValue(4) BBFloat3 e = BBFloat3.newUnallocated();

        public final @StructValue(0) BBFloat4x4 f = BBFloat4x4.newUnallocated();
        public final @StructValue(6) TestOpenCLStruct2 g = new TestOpenCLStruct2(false, false);

        public TestOpenCLStruct(boolean trackModifications, boolean allocateBuffer) {
            super(trackModifications);
            init(null, true);
            if(allocateBuffer)
                allocate();
        }
    }

    @StructureLayoutSettings(value = DefaultABIs.MSVC_X64)
    public static class TestOpenCLStruct2 extends ComplexStructure {

        public final @StructValue(0) BBFloat1 b = BBFloat1.newUnallocated();
        public final @StructValue(1) BBFloat4 a = BBFloat4.newUnallocated();

        public TestOpenCLStruct2(boolean trackModifications, boolean allocateBuffer) {
            super(trackModifications);
            init(null, false);
            if(allocateBuffer)
                allocate();
        }
    }

    @StructureLayoutSettings(
            selectorMethodClass = Test3Struct.class,
            selectorMethodName = "abiSelectorTestMethod"
    )
    public static class Test3Struct extends ComplexStructure {

        public static ABI abiSelectorTestMethod() {
            return DefaultABIs.MSVC_X64;
        }

        public final @StructValue(0) BBInt1 a = BBInt1.newUnallocated();
        public final @StructValue(1) BBLong1 b = BBLong1.newUnallocated();
        public final @StructValue(2) BBInt1 c = BBInt1.newUnallocated();

        public Test3Struct(boolean trackModifications, boolean allocateBuffer) {
            super(trackModifications);
            init(null, true);
            if(allocateBuffer)
                allocate();
        }
    }

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
        assertEquals(12, testStruct.e.getRequiredSize());
        assertEquals(64, testStruct.f.getRequiredSize());

        assertEquals(0, testStruct.f.getOffset());
        assertEquals(64, testStruct.b.getOffset());
        assertEquals(68, testStruct.c.getOffset());
        assertEquals(84, testStruct.d.getOffset());
        assertEquals(92, testStruct.e.getOffset());
        assertEquals(104, testStruct.a.getOffset());

        System.out.println(testStruct);
    }

    @Test
    void testStructureLayoutSettings() {
        Test3Struct test3Struct = new Test3Struct(true, true);

        var info = test3Struct.getInfo();

        assertEquals(DefaultABIs.MSVC_X64, info.getABI());
        assertEquals(8, info.getAlignment());
        assertEquals(24, info.getRequiredSize());
        assertArrayEquals(new int[]{0, 4, 4, 8, 0, 4, 4}, info.getSizes());
    }
}