package de.linusdev.lutils.nat.struct.info;

import org.jetbrains.annotations.NotNull;

public class ArrayInfo extends StructureInfo {

    /**
     * The length of the array (Amount of elements inside the array).
     */
    protected final int length;

    /**
     * The position in bytes of each array element.
     * @see ArrayPositionFunction#position(int) position function
     */
    protected final @NotNull ArrayPositionFunction positions;

    /**
     *
     * @param alignment see {@link StructureInfo#StructureInfo(int, boolean, int, int[])}
     * @param compressed see {@link StructureInfo#StructureInfo(int, boolean, int, int[])}
     * @param size see {@link StructureInfo#StructureInfo(int, boolean, int, int[])}
     * @param sizes see {@link StructureInfo#StructureInfo(int, boolean, int, int[])}
     * @param length see {@link #length}
     * @param positions see {@link #positions}
     */
    public ArrayInfo(
            int alignment,
            boolean compressed,
            int size,
            int @NotNull [] sizes,
            int length,
            @NotNull ArrayPositionFunction positions
    ) {
        super(alignment, compressed, size, sizes);
        this.length = length;
        this.positions = positions;
    }

    /**
     * @see #length
     */
    public int getLength() {
        return length;
    }

    /**
     * @see #positions
     */
    public @NotNull ArrayPositionFunction getPositions() {
        return positions;
    }

    /**
     * Functional interface with the function {@link #position(int)}.
     * @see #position(int)
     */
    @FunctionalInterface
    public interface ArrayPositionFunction {
        /**
         * @param index the index of the element, whose starting position shall be acquired.
         * @return starting position (in number of bytes) of element with given {@code index}.
         */
        int position(int index);
    }
}
