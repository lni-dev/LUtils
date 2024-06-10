package de.linusdev.lutils.nat.struct.array;

import de.linusdev.lutils.nat.struct.info.StructureInfo;
import org.jetbrains.annotations.NotNull;

public class ArrayStructureInfo extends StructureInfo {

    protected final @NotNull Class<?> elementClass;
    protected final @NotNull StructureInfo elementInfo;
    protected final int length;

    public ArrayStructureInfo(int alignment, boolean compressed, int size, @NotNull Class<?> elementClass, @NotNull StructureInfo elementInfo, int length) {
        super(alignment, compressed, 0, size, 0);

        this.elementClass = elementClass;
        this.elementInfo = elementInfo;
        this.length = length;
    }
}
