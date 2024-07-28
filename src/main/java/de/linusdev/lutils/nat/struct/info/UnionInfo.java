package de.linusdev.lutils.nat.struct.info;

import org.jetbrains.annotations.NotNull;

/**
 * {@link StructureInfo} for unions. {@link #sizes} does not contain the sizes of the unions element.
 * instead it will always be of length three and only define the paddings and length of the union itself.
 */
public class UnionInfo extends StructureInfo {

    /**
     * @see #getPositions()
     */
    protected final int @NotNull [] positions;

    public UnionInfo(
            int alignment,
            boolean compressed,
            int prePadding, int size, int postPadding,
            int @NotNull [] positions
    ) {
        super(alignment, compressed, prePadding, size, postPadding);
        this.positions = positions;
    }

    /**
     * Positions at which the different elements start
     * @return array of positions
     */
    public int @NotNull [] getPositions() {
        return positions;
    }
}
