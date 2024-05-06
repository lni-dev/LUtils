package de.linusdev.lutils.nat;

/**
 * An object that may be stored in the heap memory.
 */
public interface MemorySizeable {

    /**
     * Required size including padding.
     * @return the size required by this object in memory
     */
    int getRequiredSize();

    /**
     * The recommended alignment of this object in memory.
     * Either {@code 1, 2, 4, 8} or {@code 16}.
     * @return recommended alignment
     */
    int getAlignment();

}
