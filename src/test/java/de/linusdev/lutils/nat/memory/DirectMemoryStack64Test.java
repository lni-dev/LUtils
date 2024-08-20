package de.linusdev.lutils.nat.memory;

import de.linusdev.lutils.math.vector.buffer.intn.BBInt1;
import de.linusdev.lutils.math.vector.buffer.intn.BBUInt1;
import de.linusdev.lutils.nat.string.NullTerminatedUTF8String;
import org.junit.jupiter.api.Test;

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
        assertTrue(testStr.isInitialised());
        assertEquals("Test", testStr.get());
        assertEquals(stack.getAddress() + testStr.getRequiredSize(), stack.stackPointer);
        assertEquals(1, stack.currentStructCount());
        assertEquals("Test".length() + 1, stack.usedByteCount());
        stack.pop();

        assertEquals(0, stack.currentStructCount());
        assertEquals(0, stack.usedByteCount());
        assertEquals(stack.getAddress(), stack.stackPointer);
        assertTrue(stack.checkSafePoint());

        BBUInt1 bbuInt1 = stack.pushUnsignedInt();
        BBInt1 bbInt1 = stack.pushInt();
        NullTerminatedUTF8String str = stack.pushString("Some crazy string!");

        bbuInt1.set(11);
        assertEquals(11, bbuInt1.get());

        bbInt1.set(12);
        assertEquals(12, bbInt1.get());

        assertEquals("Some crazy string!", str.get());

        assertEquals(3, stack.currentStructCount());
        assertEquals(8 + "Some crazy string!".length() + 1, stack.usedByteCount());

        stack.pop();
        stack.pop();
        stack.pop();

    }
}