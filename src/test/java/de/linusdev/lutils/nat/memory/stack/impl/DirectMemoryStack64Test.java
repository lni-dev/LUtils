/*
 * Copyright (c) 2025-2026 Linus Andera
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

package de.linusdev.lutils.nat.memory.stack.impl;

import de.linusdev.lutils.math.vector.buffer.intn.BBInt1;
import de.linusdev.lutils.math.vector.buffer.intn.BBInt2;
import de.linusdev.lutils.math.vector.buffer.intn.BBUInt1;
import de.linusdev.lutils.nat.memory.NativeMemBuffer;
import de.linusdev.lutils.nat.memory.stack.PopPoint;
import de.linusdev.lutils.nat.memory.stack.SafePointError;
import de.linusdev.lutils.nat.pointer.BBPointer64;
import de.linusdev.lutils.nat.pointer.BBTypedPointer64;
import de.linusdev.lutils.nat.size.Size;
import de.linusdev.lutils.nat.string.NullTerminatedUTF8String;
import de.linusdev.lutils.nat.struct.array.StructureArray;
import org.junit.jupiter.api.Test;

import java.nio.ByteOrder;

import static de.linusdev.lutils.nat.memory.Allocators.allocManaged;
import static de.linusdev.lutils.nat.pointer.Pointer64.refL;
import static org.junit.jupiter.api.Assertions.*;

class DirectMemoryStack64Test {

    @Test
    void test() {
        DirectMemoryStack64 stack = new DirectMemoryStack64(new Size(10000));

        assertEquals(10000, stack.memorySize());
        assertEquals(DirectMemoryStack64.ALIGNMENT, stack.getAlignment());
        assertTrue(stack.isInitialised());
        assertEquals(stack.getAddress(), stack.stackPointers.stackPointer);

        try(var ignored = stack.safePoint()) {
            NullTerminatedUTF8String testStr = stack.push(NullTerminatedUTF8String.newAllocatable(null, "Test"));
            assertTrue(testStr.isInitialised());
            assertEquals("Test", testStr.get());
            assertEquals(stack.getAddress() + testStr.getRequiredSize(), stack.stackPointers.stackPointer);
            assertEquals(1, stack.currentStructCount());
            assertEquals("Test".length() + 1, stack.usedByteCount());

            try(var ignored2 = stack.safePoint()) {
                stack.pushString("second");
                stack.pop(); // second
            }

            stack.pop(); // testStr
        }

        assertEquals(0, stack.currentStructCount());
        assertEquals(0, stack.usedByteCount());
        assertEquals(stack.getAddress(), stack.stackPointers.stackPointer);



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

        NativeMemBuffer buf = stack.pushNativeMemBuffer(100L, 8);
        assertEquals(0, buf.address() % 8);
        assertEquals(100, buf.size());
        assertEquals(ByteOrder.nativeOrder(), buf.byteOrder());

        stack.pop();
        stack.close();
    }

    @Test
    void testPopPoint() {
        DirectMemoryStack64 stack = new DirectMemoryStack64();
        assertEquals(stack.getAddress(), stack.stackPointers.stackPointer);
        try (var ignored = stack.safePoint()){
            try(PopPoint point = stack.popPoint()) {
                point.pushString("Test String");
                stack.pushString("Test String 1");
                System.out.println(stack.pushString("Test String 2"));
                stack.pop(); // pop one but not the others
            }
        }
        assertEquals(stack.getAddress(), stack.stackPointers.stackPointer);
        stack.close();

    }

    @Test
    void failPopPoint() {
        DirectMemoryStack64 stack = new DirectMemoryStack64();
        assertEquals(stack.getAddress(), stack.stackPointers.stackPointer);
        stack.pushString("some string");

        assertThrows(SafePointError.class, () -> {
            try(PopPoint point = stack.popPoint()) {
                point.pushString("Test String");
                point.pushString("Test String 1");
                System.out.println(stack.pushString("Test String 2"));
                point.pop(); // Test String
                point.pop(); // Test String 1
                point.pop(); // Test String 2
                point.pop(); // pop one too many
            }
        });
        stack.close();
    }

    @Test
    void failSafePoint() {
        DirectMemoryStack64 stack = new DirectMemoryStack64();
        assertThrows(SafePointError.class, () -> {
            try (var ignored = stack.safePoint()) {
                stack.pushString("Test!");
            }
        });

        assertThrows(SafePointError.class, () -> {
            try (var ignored = stack.safePoint()) {
                stack.pop();
            }
        });
        stack.close();
    }

    @Test
    void testEmptyCheckPoint() {
        DirectMemoryStack64 stack = new DirectMemoryStack64();
        try (var ignored = stack.safePoint()) {

        }

        try (var ignored = stack.safePoint()) {
            try (var ignored2 = stack.safePoint()) {

            }
        }
        stack.close();
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
        stack.close();
    }

    @Test
    void testGetPointer() {
        DirectMemoryStack64 stack64 = new DirectMemoryStack64();

        assertNotEquals(0, stack64.getPointer());
        stack64.close();
    }

    @Test
    void isAddressInside() {
        DirectMemoryStack64 stack = new DirectMemoryStack64();

        assertTrue(stack.isAddressInside(refL(stack.pushString("Test!"))));
        assertFalse(stack.isAddressInside(refL(allocManaged(BBInt2.newAllocatable(null)))));
        stack.close();
    }
}