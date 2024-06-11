package de.linusdev.lutils.nat.array;

import de.linusdev.lutils.nat.struct.annos.SVWrapper;
import de.linusdev.lutils.nat.struct.info.ArrayInfo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NativePrimitiveTypeArrayTest {

    @Test
    public void testFloat32Array() {
        NativeFloat32Array array = NativeFloat32Array.newAllocated(SVWrapper.length(10));

        assertEquals(10, array.length());
        assertEquals(10 * 4, array.getRequiredSize());

        array.set(0, 10.0f);
        assertEquals(10.0f, array.get(0));

        for (int i = 1; i < array.length(); i++) {
            array.setFloat(i, array.get(i-1) * 2.0f);
        }

        float v = 10.0f;
        for (int i = 0; i < array.length(); i++) {
            assertEquals(v, array.getFloat(i));
            v = v*2.0f;
        }
    }

    @Test
    public void testFloat64Array() {
        NativeFloat64Array array = NativeFloat64Array.newAllocated(SVWrapper.length(10));

        assertEquals(10, array.length());
        assertEquals(10 * 8, array.getRequiredSize());

        array.set(0, 10.0);
        assertEquals(10.0, array.get(0));

        for (int i = 1; i < array.length(); i++) {
            array.setFloat64(i, array.get(i-1) * 2.0);
        }

        double v = 10.0;
        for (int i = 0; i < array.length(); i++) {
            assertEquals(v, array.getFloat64(i));
            v = v*2.0;
        }
    }

    @Test
    public void testInt32Array() {
        NativeInt32Array array = NativeInt32Array.newAllocated(SVWrapper.length(10));

        assertEquals(10, array.length());
        assertEquals(10 * 4, array.getRequiredSize());

        array.set(0, 10);
        assertEquals(10, array.get(0));

        for (int i = 1; i < array.length(); i++) {
            array.setInt(i, array.get(i-1) * 2);
        }

        int v = 10;
        for (int i = 0; i < array.length(); i++) {
            assertEquals(v, array.getInt(i));
            v = v*2;
        }
    }

    @Test
    public void testInt8Array() {
        NativeInt8Array array = NativeInt8Array.newAllocated(SVWrapper.length(10));

        assertEquals(10, array.length());
        assertEquals(10, array.getRequiredSize());

        array.setInt8(0, (byte) 10);
        assertEquals(10, array.getInt8(0));

        for (int i = 1; i < array.length(); i++) {
            array.set(i, (byte) i);
        }

        assertEquals(10, array.getInt8(0));
        for (int i = 1; i < array.length(); i++) {
            assertEquals(i, array.getInt8(i));
        }
    }

    @Test
    void getPositions() {
        NativeInt32Array array = NativeInt32Array.newAllocated(SVWrapper.length(10));

        ArrayInfo.ArrayPositionFunction positionFunction = array.getPositions();

        for (int i = 1; i < array.length(); i++) {
            assertEquals( i*4 , positionFunction.position(i));
        }

    }
}