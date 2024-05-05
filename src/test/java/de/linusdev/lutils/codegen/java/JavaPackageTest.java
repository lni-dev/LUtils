package de.linusdev.lutils.codegen.java;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JavaPackageTest {

    @Test
    void extend() {
        JavaPackage javaPackage = new JavaPackage("test.abc");
        JavaPackage extended = javaPackage.extend("hello", "wow");

        assertEquals("test.abc.hello.wow", extended.getPackageString());

        JavaPackage javaPackage2 = new JavaPackage(new String[0]);
        JavaPackage extended2 = javaPackage2.extend("hello", "wow");

        assertEquals("hello.wow", extended2.getPackageString());
    }
}