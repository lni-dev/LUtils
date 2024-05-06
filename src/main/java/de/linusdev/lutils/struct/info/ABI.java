package de.linusdev.lutils.struct.info;

import de.linusdev.lutils.nat.MemorySizeable;
import org.jetbrains.annotations.NotNull;

public interface ABI {

    /**
     *
     * @param children ordered array of {@link StructureInfo}s of children
     * @param compress Whether the structure should be compressed (alignment will be ignored).
     * @return calculated {@link StructureInfo} for a structure with given children
     */
    @NotNull StructureInfo calculateStructureLayout(
            boolean compress,
            @NotNull MemorySizeable @NotNull ... children
    );
}
