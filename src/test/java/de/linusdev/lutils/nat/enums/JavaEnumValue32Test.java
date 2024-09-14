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