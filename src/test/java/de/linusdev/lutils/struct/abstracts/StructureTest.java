package de.linusdev.lutils.struct.abstracts;

import de.linusdev.lutils.math.vector.buffer.intn.BBInt2;
import de.linusdev.lutils.math.vector.buffer.longn.BBLong1;
import de.linusdev.lutils.nat.struct.abstracts.Structure;
import de.linusdev.lutils.nat.struct.generator.Language;
import de.linusdev.lutils.nat.struct.info.StructureInfo;
import de.linusdev.lutils.nat.struct.utils.SSMUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StructureTest {

    @Test
    void unionWith() {
        BBInt2 int2 = BBInt2.newAllocated(null);
        BBLong1 long1 = Structure.unionWith(BBLong1.newAllocatable(null), int2);

        int2.x(0x1001);

        assertEquals(0x1001, int2.x());
        assertEquals(0, int2.y());
        assertEquals(0x1001, long1.get());

        int2.y(0x0001);

        assertEquals(0x1001, int2.x());
        assertEquals(0x0001, int2.y());
        assertEquals(0x0001_0000_1001L, long1.get());

        long1.set(0x0010_0000_0111L);

        assertEquals(0x0111, int2.x());
        assertEquals(0x0010, int2.y());
        assertEquals(0x0010_0000_0111L, long1.get());
    }

    @Test
    void generateStructCodeOfOpenCLStruct() {
        String gen = Structure.generateStructCode(Language.OPEN_CL, ComplexStructureTest.TestOpenCLStruct.class);
        System.out.println(gen);

        StructureInfo info = SSMUtils.getInfo(
                ComplexStructureTest.TestOpenCLStruct.class,
                null, null,
                null, null,
                null, null
        );

        assertEquals("typedef struct __attribute__((packed)) {\n" +
                "    float f[16];\n" +
                "    float1 b;\n" +
                "    int2 padding0;\n" +
                "    int padding1;\n" +
                "    float4 c;\n" +
                "    float2 d;\n" +
                "    int2 padding2;\n" +
                "    float3 e;\n" +
                "    float4 a;\n" +
                "    TestOpenCLStruct2 g;\n" +
                "} TestOpenCL;", gen);


        StructureInfo msvcStructInfo = SSMUtils.getInfo( // generated with MSVC
                ComplexStructureTest.TestOpenCLStruct2.class,
                null, null,
                null, null,
                null, null
        );

        assertEquals(20, msvcStructInfo.getRequiredSize()); // MSVC Size, but in OPENCL it must be 32

        assertEquals(144 + 32, info.getRequiredSize());
        assertEquals(16, info.getAlignment());


    }
}