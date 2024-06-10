package de.linusdev.lutils.math.matrix.buffer;

import de.linusdev.lutils.math.vector.buffer.BBVectorInfo;
import de.linusdev.lutils.nat.NativeType;
import de.linusdev.lutils.nat.struct.info.ArrayInfo;
import de.linusdev.lutils.nat.abi.ABI;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class BBMatrixInfo extends BBVectorInfo {

    @Contract("_, _, _, _ -> new")
    public static @NotNull BBMatrixInfo create(
            @NotNull ABI abi,
            int width, int height,
            @NotNull NativeType type
    ) {
        ArrayInfo arrayInfo = abi.calculateArrayLayout(
                false,
                type.getMemorySizeable(abi.types()),
                width*height,
                -1
        );

        return new BBMatrixInfo(
                arrayInfo.getAlignment(),
                arrayInfo.getRequiredSize(),
                arrayInfo.getSizes(),
                arrayInfo.getLength(),
                arrayInfo.getPositions(),
                type,
                width,
                height
        );
    }

    protected final int width;
    protected final int height;

    public BBMatrixInfo(
            int alignment,
            int size,
            int @NotNull [] sizes,
            int length,
            @NotNull ArrayPositionFunction positions,
            @NotNull NativeType type,
            int width,
            int height
    ) {
        super(alignment, size, sizes, length, positions, type);
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
