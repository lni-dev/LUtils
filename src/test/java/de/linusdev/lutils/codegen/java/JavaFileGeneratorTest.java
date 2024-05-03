package de.linusdev.lutils.codegen.java;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Test;

class JavaFileGeneratorTest {

    @Test
    void write() {

        JavaFileGenerator gen = new JavaFileGenerator(
                new JavaPackage("de.linusdev.test"),
                null
        );

        gen.setName("GeneratedTestClass");
        gen.setExtendedClass(JavaClass.ofClass(JavaImport.class));
        gen.setImplementedClasses(new JavaClass[]{gen, gen});
        var testStringVar = gen.addVariable(JavaClass.ofClass(String.class), "testString");

        testStringVar.setFinal(true);
        testStringVar.setVisibility(JavaVisibility.PUBLIC);
        testStringVar.addAnnotation(JavaClass.ofClass(Nullable.class));

        gen.addVariable(JavaClass.ofClass(int.class), "testPrimitiveInt");
        gen.addVariable(JavaClass.ofClass(int[].class), "testPrimitiveIntArray");
        gen.addVariable(JavaClass.ofClass(String[].class), "testStringArray");
        gen.addAnnotation(JavaClass.ofClass(NotNull.class)).setValue(JavaVariable.of(NotNull.class, "value"), JavaExpression.ofString("t\"est"));

        var method = gen.addMethod(JavaClass.ofClass(void.class), "testVoid");
        method.addParameter("firstParam", JavaClass.ofClass(String.class));
        method.addParameter("secondParam", JavaClass.ofClass(String.class)).addAnnotation(JavaClass.ofClass(NotNull.class));

        method = gen.addConstructor();
        method.setVisibility(JavaVisibility.PUBLIC);
        var param = method.addParameter("testParam", JavaClass.ofClass(String.class));
        method.body(body -> {
            JavaLocalVariable test = JavaUtils.createLocalVariable(JavaClass.ofClass(String.class), "test");

            body.addExpression(JavaExpression.declare(test));
            body.addExpression(JavaExpression.assign(test, param));
        });

        System.out.println(gen.writeToString());

    }
}