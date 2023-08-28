package de.linusdev.lutils.math.vector.buffer;

import de.linusdev.lutils.struct.info.StructureInfo;
import org.jetbrains.annotations.NotNull;

public class BBVectorInfo extends StructureInfo {

    private final int elementCount;
    private final @NotNull String elementTypeName;

    public static @NotNull BBVectorInfo create(
            @NotNull String elementTypeName,
            int elementCount,
            int elementSize
    ) {

        int size = elementCount * elementSize;
        int postPadding = size > 16 ? (16 - (size % 16)) % 16 : (Integer.highestOneBit(size) << 1) % size;

        return new BBVectorInfo(
                Math.min(size + postPadding, 16),
                false,
                0,
                size,
                postPadding,
                elementTypeName,
                elementCount
        );
    }

    public BBVectorInfo(
            int alignment,
            boolean compressed,
            int prePadding,
            int size,
            int postPadding,
            @NotNull String elementTypeName,
            int elementCount
    ) {
        super(alignment, compressed, prePadding, size, postPadding);
        this.elementCount = elementCount;
        this.elementTypeName = elementTypeName;
    }

    public int getElementCount() {
        return elementCount;
    }

    public @NotNull String getElementTypeName() {
        return elementTypeName;
    }
}
