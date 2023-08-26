package de.linusdev.lutils.struct.array;

import de.linusdev.lutils.struct.info.StructureInfo;
import org.jetbrains.annotations.NotNull;

public class ArrayStructureInfo extends StructureInfo {

    protected final @NotNull Class<?> elementClass;
    protected final int length;

    public ArrayStructureInfo(int alignment, boolean compressed, int size, @NotNull Class<?> elementClass, int length) {
        super(alignment, compressed, 0, size, 0);

        this.elementClass = elementClass;
        this.length = length;
    }
}
