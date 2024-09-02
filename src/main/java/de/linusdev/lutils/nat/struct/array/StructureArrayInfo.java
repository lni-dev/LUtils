package de.linusdev.lutils.nat.struct.array;

import de.linusdev.lutils.nat.struct.info.ArrayInfo;
import de.linusdev.lutils.nat.struct.info.StructureInfo;
import org.jetbrains.annotations.NotNull;

public class StructureArrayInfo extends ArrayInfo {

    protected final @NotNull Class<?> elementClass;
    protected final @NotNull StructureInfo elementInfo;

    public StructureArrayInfo(
            int alignment,
            boolean compressed,
            int size,
            int[] sizes,
            int length,
            int stride,
            @NotNull ArrayInfo.ArrayPositionFunction positions,
            @NotNull Class<?> elementClass,
            @NotNull StructureInfo elementInfo
    ) {
        super(alignment, compressed, size, sizes, length, stride, positions);

        this.elementClass = elementClass;
        this.elementInfo = elementInfo;
    }
}
