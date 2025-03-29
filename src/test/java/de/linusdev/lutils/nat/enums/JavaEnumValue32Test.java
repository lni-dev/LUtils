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

package de.linusdev.lutils.nat.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JavaEnumValue32Test {

    public enum TestEnum implements NativeEnumMember32 {
        A(1),
        B(2),
        C(3),
        ;

        private final int value;

        TestEnum(int value) {
            this.value = value;
        }

        @Override
        public int getValue() {
            return value;
        }
    }

    @Test
    void test() {
        EnumValue32<TestEnum> eVal = new JavaEnumValue32<>();

        assertEquals(0, eVal.get());

        eVal.set(1);

        assertEquals(1, eVal.get());
        assertEquals(TestEnum.A, eVal.get(TestEnum.class));

        eVal.set(TestEnum.C);

        assertEquals(3, eVal.get());
        assertEquals(TestEnum.C, eVal.get(TestEnum.class));

    }

    @Test
    void test2() {
        EnumValue32<TestEnum> eVal = new JavaEnumValue32<>(1);
        assertEquals(1, eVal.get());
        assertEquals(TestEnum.A, eVal.get(TestEnum.class));

    }

    @Test
    void test3() {
        EnumValue32<TestEnum> eVal = new JavaEnumValue32<>(TestEnum.A);
        assertEquals(1, eVal.get());
        assertEquals(TestEnum.A, eVal.get(TestEnum.class));

    }
}