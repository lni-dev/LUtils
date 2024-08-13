package de.linusdev.lutils.nat.memory;

import de.linusdev.lutils.nat.struct.abstracts.Structure;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

public interface Stack extends DirectMemoryManager {

    /**
     * Pushes given {@code structure}) onto this stack and allocates (calls {@link Structure#claimBuffer(ByteBuffer) claimBuffer}).
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

}
