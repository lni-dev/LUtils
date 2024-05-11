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
        gen.setExtendedClass(JavaClass.ofClass(JavaImport.class).withGenerics(JavaClass.ofClass(Integer.class), JavaClass.ofClass(String.class)));
        gen.setImplementedClasses(new JavaClass[]{gen, gen});
        var testStringVar = gen.addVariable(JavaClass.ofClass(String.class), "testString");

        testStringVar.setFinal(true);
        testStringVar.setVisibility(JavaVisibility.PUBLIC);
        testStringVar.addAnnotation(JavaClass.ofClass(Nullable.class));

        gen.addVariable(JavaClass.ofClass(int.class), "testPrimitiveInt").setJavaDoc("Test Javadoc of variable");
        var testVarPIntArray = gen.addVariable(JavaClass.ofClass(int[].class), "testPrimitiveIntArray");
        gen.addGetter(testVarPIntArray);
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

        var subClass = gen.addSubClass(true);
        subClass.setName("TestSubClass");
        subClass.addVariable(JavaClass.ofClass(int.class), "testPrimitiveIntInSubClass");

        method = subClass.addMethod(JavaClass.ofClass(void.class), "testSubClassVoid");
        method.addParameter("firstParam", JavaClass.ofClass(String.class));
        method.addParameter("secondParam", JavaClass.ofClass(String.class)).addAnnotation(JavaClass.ofClass(NotNull.class));
        method.setJavaDoc("Test Javadoc.\nWOOOW");

        var subSubClass = subClass.addSubClass(true);
        subSubClass.setName("TestSubSubClass");

        System.out.println(gen.writeToString());

        System.out.println();
        System.out.println();

        gen = new JavaFileGenerator(JavaPackage.ofClass(String.class), JavaSourceGeneratorHelper.getDefault());
        gen.setType(JavaClassType.ENUM);
        gen.setVisibility(JavaVisibility.PUBLIC);
        gen.setName("TestEnum");
        gen.addEnumMember("TEST_1", JavaExpression.ofString("test"), JavaExpression.numberPrimitive(10.0f));
        gen.addEnumMember("TEST_2", JavaExpression.ofString("test2"), JavaExpression.numberPrimitive(20.0f));
        var m = gen.addEnumMember("TEST_3", JavaExpression.ofString("test3"), JavaExpression.numberPrimitive(30.0f));
        m.addAnnotation(JavaClass.ofClass(Deprecated.class));
        m.setJavaDoc("Test doc");

        System.out.println(gen.writeToString());

    }
}