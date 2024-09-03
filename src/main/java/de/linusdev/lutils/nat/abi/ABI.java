package de.linusdev.lutils.nat.abi;

import de.linusdev.lutils.nat.MemorySizeable;
import de.linusdev.lutils.nat.NativeType;
import de.linusdev.lutils.nat.struct.info.ArrayInfo;
import de.linusdev.lutils.nat.struct.info.StructureInfo;
import de.linusdev.lutils.nat.struct.info.UnionInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ABI {

    /**
     * Two ABIs are considered equal if both have the same {@link #identifier()}.
     * @return {@code true} if the given ABIs are equal, {@code false} otherwise.
     */
    static boolean equals(@NotNull ABI that, @Nullable ABI other) {
        return other != null && that.identifier().equals(other.identifier());
    }

    /**
     * Unique Identifier of this ABI.
     */
    @NotNull String identifier();

    /**
     *
     * @param children ordered array of {@link StructureInfo}s of children
     * @param compress Whether the structure should be compressed (alignment will be ignored).
     * @return calculated {@link StructureInfo} for a structure with given children
     */
    @NotNull StructureInfo calculateStructureLayout(
            boolean compress,
            @NotNull MemorySizeable @NotNull ... children
    );

    /**
     *
     * @param children ordered array of {@link StructureInfo}s of children
     * @param compress Whether the structure should be compressed (alignment will be ignored).
     * @return calculated {@link UnionInfo} for a structure with given children
     */
    @NotNull
    UnionInfo calculateUnionLayout(
            boolean compress,
            @NotNull MemorySizeable @NotNull ... children
    );

    /**
     *
     * @param compress whether to try and {@link StructureInfo#isCompressed() compress} the array.
     * @param children {@link MemorySizeable} of the child elements.
     * @param length amount of child elements
     * @param stride -1 to determine stride based on {@code children}. Otherwise, the size of one element including padding.
     * @return calculated {@link ArrayInfo} for an array with given properties.
     */
    @NotNull ArrayInfo calculateArrayLayout(
            boolean compress,
            @NotNull MemorySizeable children,
            int length,
            int stride
    );

    /**
     * Get Layout for a vector type.
     * @param componentType The vector component type
     * @param length The component count
     * @return {@link ArrayInfo} with information about the layout of the vector.
     */
    default @NotNull ArrayInfo calculateVectorLayout(
            @NotNull NativeType componentType,
            int length
    ) {
        return calculateArrayLayout(
                false,
                componentType.getMemorySizeable(types()),
                length,
                -1
        );
    }

    /**
     * Get layout for a matrix type
     * @param componentType the matrix component type
     * @param width the width of the matrix
     * @param height the height of the matrix
     * @return {@link ArrayInfo} with information about the layout of the vector.
     */
    default @NotNull ArrayInfo calculateMatrixLayout(
            @NotNull NativeType componentType,
            int width,
            int height
    ) {
        return calculateArrayLayout(
                false,
                componentType.getMemorySizeable(types()),
                width*height,
                -1
        );
    }

    @NotNull Types types();
}
