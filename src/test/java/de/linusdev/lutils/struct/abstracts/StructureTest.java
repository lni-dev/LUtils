package de.linusdev.lutils.struct.abstracts;

import de.linusdev.lutils.nat.struct.abstracts.Structure;
import de.linusdev.lutils.nat.struct.generator.Language;
import de.linusdev.lutils.nat.struct.info.StructureInfo;
import de.linusdev.lutils.nat.struct.utils.SSMUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StructureTest {

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