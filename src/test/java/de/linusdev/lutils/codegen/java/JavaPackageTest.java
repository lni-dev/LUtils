/*
 * Copyright (c) 2025 Linus Andera
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.linusdev.lutils.codegen.java;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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