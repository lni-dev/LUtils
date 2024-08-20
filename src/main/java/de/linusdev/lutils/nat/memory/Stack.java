package de.linusdev.lutils.nat.memory;

import de.linusdev.lutils.math.vector.buffer.intn.BBInt1;
import de.linusdev.lutils.math.vector.buffer.intn.BBUInt1;
import de.linusdev.lutils.nat.string.NullTerminatedUTF8String;
import de.linusdev.lutils.nat.struct.abstracts.Structure;
import de.linusdev.lutils.nat.struct.annos.StructValue;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

public interface Stack extends DirectMemoryManager {

    /**
     * Pushes given {@code structure}) onto this stack and allocates (calls {@link Structure#claimBuffer(ByteBuffer) claimBuffer}).
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

}
