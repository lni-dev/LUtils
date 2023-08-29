package de.linusdev.lutils.struct.abstracts;

import de.linusdev.lutils.struct.generator.Language;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StructureTest {

    @Test
    void generateStructCode() {
        System.out.println(Structure.generateStructCode(Language.OPEN_CL, ComplexStructureTest.Test2Struct.class));


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
                "} Test2;", Structure.generateStructCode(Language.OPEN_CL, ComplexStructureTest.Test2Struct.class));
    }
}