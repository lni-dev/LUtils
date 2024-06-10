package de.linusdev.lutils.nat;

import org.jetbrains.annotations.NotNull;

/**
 * An object that may be stored in the heap memory.
 */
public interface MemorySizeable {

    static @NotNull MemorySizeable of(int size, int alignment) {
        return new MemorySizeable() {
            @Override
            public int getRequiredSize() {
                return size;
            }

            @Override
            public int getAlignment() {
                return alignment;
            }
        };
    }

    static @NotNull MemorySizeable of(int size) {
        return new MemorySizeable() {
            @Override
            public int getRequiredSize() {
                return size;
            }

            @Override
            public int getAlignment() {
                return size;
            }
        };
    }

    /**
     * Required size in bytes including padding.
     * @return the size in bytes required by this object in memory
     */
    int getRequiredSize();

    /**
     * The recommended alignment of this object in memory.
     * Either {@code 1, 2, 4, 8} or {@code 16}.
     * @return recommended alignment in bytes
     */
    int getAlignment();

}
