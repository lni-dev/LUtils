package de.linusdev.lutils.nat.memory;

import de.linusdev.lutils.math.vector.buffer.intn.BBInt1;
import de.linusdev.lutils.math.vector.buffer.intn.BBUInt1;
import de.linusdev.lutils.nat.pointer.BBPointer64;
import de.linusdev.lutils.nat.pointer.BBTypedPointer64;
import de.linusdev.lutils.nat.string.NullTerminatedUTF8String;
import de.linusdev.lutils.nat.struct.array.StructureArray;
import de.linusdev.lutils.nat.struct.utils.BufferUtils;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static org.junit.jupiter.api.Assertions.*;

class DirectMemoryStack64Test {

    @Test
    void test() {
        DirectMemoryStack64 stack = new DirectMemoryStack64();
        assertTrue(stack.createSafePoint());

        assertEquals(DirectMemoryStack64.DEFAULT_MEMORY_SIZE, stack.memorySize());
        assertEquals(DirectMemoryStack64.ALIGNMENT, stack.getAlignment());
        assertTrue(stack.isInitialised());
        assertEquals(stack.getAddress(), stack.stackPointer);


        NullTerminatedUTF8String testStr = stack.push(NullTerminatedUTF8String.newAllocatable("Test"));
        assertFalse(stack.checkSafePoint());
        assertTrue(stack.createSafePoint()); // create a second safe point

        assertTrue(testStr.isInitialised());
        assertEquals("Test", testStr.get());
        assertEquals(stack.getAddress() + testStr.getRequiredSize(), stack.stackPointer);
        assertEquals(1, stack.currentStructCount());
        assertEquals("Test".length() + 1, stack.usedByteCount());

        stack.pushString("second");
        assertFalse(stack.checkSafePoint());
        stack.pop(); // second
        assertTrue(stack.checkSafePoint());


        stack.pop(); // testStr

        assertEquals(0, stack.currentStructCount());
        assertEquals(0, stack.usedByteCount());
        assertEquals(stack.getAddress(), stack.stackPointer);
        assertTrue(stack.checkSafePoint());

        BBUInt1 bbuInt1 = stack.pushUnsignedInt();
        BBInt1 bbInt1 = stack.pushInt();
        NullTerminatedUTF8String str = stack.pushString("Some crazy string!");
        StructureArray<BBInt1> array = stack.pushArray(1000, BBInt1.class, () -> BBInt1.newAllocatable(null));

        System.out.println(array.getRequiredSize());

        bbuInt1.set(11);
        assertEquals(11, bbuInt1.get());

        bbInt1.set(12);
        assertEquals(12, bbInt1.get());

        assertEquals("Some crazy string!", str.get());

        assertEquals(1000, array.length());

        assertEquals(4, stack.currentStructCount());
        assertEquals(8 + "Some crazy string!".length() + 1 + 4000 + 1 /*alignment*/, stack.usedByteCount());

        System.out.println(stack);

        stack.pop();
        stack.pop();
        stack.pop();
        stack.pop();

        ByteBuffer buf = stack.pushByteBuffer(100, 8);
        assertEquals(0, BufferUtils.getHeapAddress(buf) % 8);
        assertEquals(100, buf.capacity());
        assertEquals(ByteOrder.nativeOrder(), buf.order());

        stack.pop();

    }

    @Test
    void testEmptyCheckPoint() {
        DirectMemoryStack64 stack = new DirectMemoryStack64();
        assertTrue(stack.createSafePoint());
        assertTrue(stack.checkSafePoint());

        assertTrue(stack.createSafePoint());
        assertTrue(stack.createSafePoint());
        assertTrue(stack.checkSafePoint());
        assertTrue(stack.checkSafePoint());
    }

    @Test
    void testPointer() {
        DirectMemoryStack64 stack = new DirectMemoryStack64();

        BBPointer64 pointer64 = stack.pushPointer();
        pointer64.set(10L);
        assertEquals(10L, pointer64.get());

        BBTypedPointer64<BBInt1> typedPointer = stack.pushTypedPointer();
        typedPointer.set(11L);
        assertEquals(11L, typedPointer.get());

        stack.pop(); // typedPointer
        stack.pop(); // pointer64

    }

    @Test
    void testGetPointer() {
        DirectMemoryStack64 stack64 = new DirectMemoryStack64();

        assertNotEquals(0, stack64.getPointer());
    }
}