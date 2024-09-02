package de.linusdev.lutils.nat.memory;

import de.linusdev.lutils.nat.NativeParsable;
import org.jetbrains.annotations.NotNull;

/**
 * Instances of this interface manage directly allocated memory.
 * @see DirectMemoryStack64
 * @see Stack
 */
public interface DirectMemoryManager extends NativeParsable {

    /**
     * size of the managed memory in bytes.
     */
    long memorySize();

    /**
     * amount of bytes currently in use
     */
    long usedByteCount();

    /**
     * amount of bytes currently free
     */
    default long freeByteCount() {
        return memorySize() - usedByteCount();
    }

    /**
     * Percentage of bytes currently in use.
     */
    default double usedBytesPercentage() {
        return ((double) usedByteCount()) / ((double) freeByteCount());
    }

    /**
     * Amount of structures currently managed by this {@link DirectMemoryManager}.
     */
    int currentStructCount();

    default @NotNull String info() {
        return String.format(
                "%s { used:%.3f%% (%d bytes), structs: %d }",
                getClass().getSimpleName(), usedBytesPercentage(), usedByteCount(), currentStructCount()
        );
    }

    /**
     * @return {@code true} if given {@code address} is inside the address space of this memory manager.
     */
    default boolean isAddressInside(long address) {
        if((address -= getPointer()) < 0L) return false; // too small
        return address < memorySize(); // too big
    }

}
