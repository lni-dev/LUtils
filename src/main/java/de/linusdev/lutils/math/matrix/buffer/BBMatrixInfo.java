package de.linusdev.lutils.math.matrix.buffer;

import de.linusdev.lutils.struct.info.StructureInfo;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class BBMatrixInfo extends StructureInfo {

    @Contract("_, _, _, _ -> new")
    public static @NotNull BBMatrixInfo create(
            int width, int height,
            int elementSize,
            @NotNull String elementTypeName
    ) {
        int size = width * height * elementSize;
        int postPadding = elementSize - (size % elementSize);

        return new BBMatrixInfo(elementSize, false, 0, size, postPadding, elementTypeName, width, height);
    }

    private final @NotNull String elementTypeName;
    protected final int width;
    protected final int height;

    public BBMatrixInfo(int alignment, boolean compressed, int prePadding, int size, int postPadding, @NotNull String elementTypeName, int width, int height) {
        super(alignment, compressed, prePadding, size, postPadding);
        this.elementTypeName = elementTypeName;
        this.width = width;
        this.height = height;
    }

    public @NotNull String getElementTypeName() {
        return elementTypeName;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
