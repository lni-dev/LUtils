/*
 * Copyright (c) 2024 Linus Andera
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

package de.linusdev.lutils.nat.memory.stack;

import de.linusdev.lutils.math.vector.buffer.intn.BBInt1;
import de.linusdev.lutils.math.vector.buffer.intn.BBUInt1;
import de.linusdev.lutils.nat.memory.DirectMemoryManager;
import de.linusdev.lutils.nat.pointer.BBPointer64;
import de.linusdev.lutils.nat.pointer.BBTypedPointer64;
import de.linusdev.lutils.nat.string.NullTerminatedUTF8String;
import de.linusdev.lutils.nat.struct.UStructSupplier;
import de.linusdev.lutils.nat.struct.abstracts.Structure;
import de.linusdev.lutils.nat.struct.annos.SVWrapper;
import de.linusdev.lutils.nat.struct.annos.StructValue;
import de.linusdev.lutils.nat.struct.array.StructureArray;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

public interface Stack extends DirectMemoryManager {

    /**
     * Pushes given {@code structure}) onto this stack and allocates it (calls {@link Structure#claimBuffer(ByteBuffer) claimBuffer}). The stack
     * must respect the alignment requirements of given {@code structure}. The stack must also ensure, that all bytes, the structure will use are set to
     * zero before {@link Structure#claimBuffer(ByteBuffer) claimBuffer} is called.
     * <br><br>
     * For each push call should exist a {@link #pop()} call later.
     * @param structure unallocated {@link Structure}, which should use a part of this stack as its {@link Structure#getByteBuffer() buffer}.
     * @return allocated {@link Structure}
     * @param <T> structure type
     */
    <T extends Structure> T push(@NotNull T structure);

    /**
     * Pops the last {@link #push(Structure) pushed} structure from this stack. This means, that
     * the stack pointer will be decreased to the state, before the {@link #push(Structure) push} operation.
     * <br><br>
     * The {@link Structure} will still be backed by this stack and may not be used anymore.
     */
    void pop();

    /**
     * Creates an {@link SafePoint} on the current stack. When this safe point is {@link SafePoint#close() closed},
     * it is checked if the {@link Stack} is in the same state as when the safe point was created. This means, the same
     * number of structures are on the stack. If the same state has not been reached a {@link SafePointError} will
     * be thrown. Additionally {@link #pop()} will throw a {@link SafePointError}, when the pop operation
     * would pop past the last {@link SafePoint#close() unclosed} safe point (if assertions are enabled).<br><br>
     * Safe points implement {@link AutoCloseable} and can be used in combination with try statements:
     * <pre>{@code
     * try(var ignored2 = stack.safePoint()) {
     *     var string = stack.pushString("some string");
     *     // Do something ...
     *     stack.pop(); // string
     * }
     * }</pre>
     * Safe points can also be used as the stack itself:
     * <pre>{@code
     * try(var safePoint = stack.safePoint()) {
     *     var string = safePoint.pushString("some string");
     *     // Do something ...
     *     safePoint.pop(); // string
     * }
     * }</pre>
     */
    @NotNull SafePoint safePoint();

    /**
     * Creates a {@link PopPoint} on the current stack. When this pop point is {@link PopPoint#close() closed},
     * the stack is reverted to the state as when the pop point was created. Additionally {@link #pop()}
     * will throw a {@link SafePointError}, when the pop operation would pop past the last
     * {@link PopPoint#close() unclosed} pop point (if assertions are enabled).<br><br>
     * Pop points implement {@link AutoCloseable} and can be used in combination with try statements:
     * <pre>{@code
     * try(var ignored2 = stack.popPoint()) {
     *     var string = stack.pushString("some string");
     *     // Do something ...
     *
     * }  // string will automatically be popped.
     * }</pre>
     * Pop points differ from {@link #safePoint() safe points} only in the {@link PopPoint#close() close} operation.<br>
     * Pop points can also be used as the stack itself:
     * <pre>{@code
     * try(var popPoint = stack.popPoint()) {
     *     var string = popPoint.pushString("some string");
     *     // Do something ...
     *
     * } // string will automatically be popped.
     * }</pre>
     */
    @NotNull PopPoint popPoint();

    /**
     * Creates a new {@link BBUInt1} using {@link BBUInt1#newAllocatable(StructValue) BBUInt1.newAllocatable(null)} and
     * {@link #push(Structure) pushes} it onto this stack.
     * @return pushed {@link BBUInt1}
     */
    default @NotNull BBUInt1 pushUnsignedInt() {
        return push(BBUInt1.newAllocatable(null));
    }

    /**
     * Creates a new {@link BBInt1} using {@link BBInt1#newAllocatable(StructValue) BBInt1.newAllocatable(null)} and
     * {@link #push(Structure) pushes} it onto this stack.
     * @return pushed {@link BBInt1}
     */
    default @NotNull BBInt1 pushInt() {
        return push(BBUInt1.newAllocatable(null));
    }

    /**
     * Creates a new {@link NullTerminatedUTF8String} using
     * {@link NullTerminatedUTF8String#newAllocatable(String) NullTerminatedUTF8String.newAllocatable(string)} and
     * {@link #push(Structure) pushes} it onto this stack.
     * @return pushed {@link NullTerminatedUTF8String}
     */
    default @NotNull NullTerminatedUTF8String pushString(@NotNull String string) {
        return push(NullTerminatedUTF8String.newAllocatable(string));
    }

    /**
     * Creates a new {@link StructureArray} using
     * {@link StructureArray#newAllocatable(boolean, StructValue, StructValue, UStructSupplier)
     * StructureArray.newAllocatable(false, SVWrapper.of(size, elementClazz), null, creator)} and
     * {@link #push(Structure) pushes} it onto this stack.
     *
     * @param size array length
     * @param elementClazz class of {@link T}
     * @param creator see {@link StructureArray#newAllocated(int, Class, UStructSupplier) newAllocated()}
     * @return pushed {@link StructureArray}
     */
    default <T extends Structure> @NotNull StructureArray<T> pushArray(
            int size,
            @NotNull Class<?> elementClazz,
            @NotNull UStructSupplier<T> creator
    ) {
        StructureArray<T> array = StructureArray.newAllocatable(
                false, 
                SVWrapper.of(size, elementClazz), 
                null, 
                creator
        );

        return push(array);
    }

    /**
     * Creates a new {@link BBPointer64} using {@link BBPointer64#newAllocatable(StructValue) BBPointer64.newAllocatable(null)} and
     * {@link #push(Structure) pushes} it onto this stack.
     * @return pushed {@link BBPointer64}
     */
    default @NotNull BBPointer64 pushPointer() {
        return push(BBPointer64.newAllocatable(null));
    }

    /**
     * Creates a new {@link BBTypedPointer64} using {@link BBTypedPointer64#newAllocatable1(StructValue) BBPointer64.newAllocatable1(null)} and
     * {@link #push(Structure) pushes} it onto this stack.
     * @return pushed {@link BBTypedPointer64}
     */
    default <T extends Structure> BBTypedPointer64<T> pushTypedPointer() {
        return push(BBTypedPointer64.newAllocatable1(null));
    }

    /**
     * Creates a new byte buffer as described below and pushes it onto this stack. The returned byte buffers
     * content will not be set to zero and may contain garbage.
     * @param size size of the required byte buffer
     * @param alignment alignment of the required byte buffer
     * @return {@link ByteBuffer} with given {@code size} and aligned to given {@code alignment}
     */
    @NotNull ByteBuffer pushByteBuffer(int size, int alignment);

}
