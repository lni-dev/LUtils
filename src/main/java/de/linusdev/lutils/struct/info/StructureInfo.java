package de.linusdev.lutils.struct.info;

import de.linusdev.lutils.struct.abstracts.MemorySizeable;
import de.linusdev.lutils.struct.abstracts.Structure;
import org.jetbrains.annotations.NotNull;

public class StructureInfo implements MemorySizeable {

    protected final int alignment;
    protected final boolean compressed;
    protected final int size;

    /**
     * array of item sizes. Always alternating between padding and item.
     * First and last element are always a padding.<br><br>
     * Layout: <br>
     * [0]: pad, [1]: size, [2]: pad, [3]: size, [4]: pad, [5]: size, ...
     */
    protected final int @NotNull [] sizes;

    /**
     * Manually create {@link StructureInfo}.
     * @param alignment alignment of the structure
     * @param compressed whether the structure is compressed, see {@link #isCompressed()}
     * @param prePadding padding before the {@link Structure}
     * @param size size of the actual {@link Structure} (without any padding)
     * @param postPadding padding after the {@link Structure}
     */
    public StructureInfo(int alignment, boolean compressed, int prePadding, int size, int postPadding) {
        this.alignment = alignment;
        this.compressed = compressed;
        this.sizes = new int[]{prePadding, size, postPadding};
        this.size = prePadding + size + postPadding;
    }

    protected StructureInfo(int alignment, boolean compressed, int size, int @NotNull [] sizes) {
        this.alignment = alignment;
        this.compressed = compressed;
        this.sizes = sizes;
        this.size = size;
    }

    @Override
    public int getRequiredSize() {
        return size;
    }

    @Override
    public int getAlignment() {
        return alignment;
    }

    /**
     * Whether this {@link StructureInfo} is compressed. Compressed means, it should ignore
     * the size of the individual elements when aligning the structure.
     * @return {@code true} if it is compressed.
     */
    public boolean isCompressed() {
        return compressed;
    }

    public int @NotNull [] getSizes() {
        return sizes;
    }
}
