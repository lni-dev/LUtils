package de.linusdev.lutils.nat.memory;

import de.linusdev.lutils.nat.string.NullTerminatedUTF8String;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DirectMemoryStack64Test {

    @Test
    void test() {
        DirectMemoryStack64 stack = new DirectMemoryStack64();
        stack.createSafePoint();

        assertEquals(DirectMemoryStack64.DEFAULT_MEMORY_SIZE, stack.memorySize());
        assertEquals(DirectMemoryStack64.ALIGNMENT, stack.getAlignment());
        assertTrue(stack.isInitialised());
        assertEquals(stack.getAddress(), stack.stackPointer);


        NullTerminatedUTF8String testStr = stack.push(NullTerminatedUTF8String.newAllocatable("Test"));

        assertFalse(stack.checkSafePoint());
        assertTrue(testStr.isInitialised());
        assertEquals("Test", testStr.get());
        assertEquals(stack.getAddress() + testStr.getRequiredSize(), stack.stackPointer);

        stack.pop();

        assertEquals(stack.getAddress(), stack.stackPointer);
        assertTrue(stack.checkSafePoint());

    }
}