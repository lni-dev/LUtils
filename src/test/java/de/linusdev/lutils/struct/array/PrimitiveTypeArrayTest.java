package de.linusdev.lutils.struct.array;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PrimitiveTypeArrayTest {

    @Test
    public void test() {
        int length = 13;
        PrimitiveTypeArray<Float> pArray = new PrimitiveTypeArray<>(Float.class, length, true);

        assertEquals(pArray.getInfo().getAlignment(), Float.BYTES);
        assertEquals(pArray.getInfo().getRequiredSize(), Float.BYTES * length);
        assertArrayEquals(pArray.getInfo().getSizes(), new int[]{0, Float.BYTES * length, 0});

        pArray.set(3, 20.3f);

        assertEquals(20.3f, pArray.get(3));

        pArray.setFloat(4, 1f);

        assertEquals(20.3f, pArray.get(3));
        assertEquals(1f, pArray.get(4));

        System.out.println(pArray);
    }

}