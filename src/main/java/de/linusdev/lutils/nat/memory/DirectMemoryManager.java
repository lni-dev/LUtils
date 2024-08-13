package de.linusdev.lutils.nat.memory;

import de.linusdev.lutils.nat.NativeParsable;

public interface DirectMemoryManager extends NativeParsable {

    /**
     * size of the managed memory in bytes.
     */
    long memorySize();

}
