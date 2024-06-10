package de.linusdev.lutils.math.vector.buffer;

import de.linusdev.lutils.nat.NativeType;
import de.linusdev.lutils.nat.struct.info.ArrayInfo;
import de.linusdev.lutils.nat.abi.ABI;
import org.jetbrains.annotations.NotNull;

public class BBVectorInfo extends ArrayInfo {

    private final @NotNull NativeType type;

    public static @NotNull BBVectorInfo create(
            @NotNull ABI abi,
            int elementCount,
            @NotNull NativeType type
    ) {
        ArrayInfo arrayInfo = abi.calculateVectorLayout(
                type,
                elementCount
        );

        return new BBVectorInfo(
                arrayInfo.getAlignment(),
                arrayInfo.getRequiredSize(),
                arrayInfo.getSizes(),
                arrayInfo.getLength(),
                arrayInfo.getPositions(),
                type
        );
    }

    public BBVectorInfo(
            int alignment,
            int size,
            int @NotNull [] sizes,
            int length,
            @NotNull ArrayPositionFunction positions,
            @NotNull NativeType type
    ) {
        super(alignment, false, size, sizes, length, positions);
        this.type = type;
    }

    public @NotNull NativeType getType() {
        return type;
    }
}
