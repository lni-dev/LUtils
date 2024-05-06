package de.linusdev.lutils.struct.info;

import de.linusdev.lutils.nat.MemorySizeable;
import org.jetbrains.annotations.NotNull;

/**
 * Application Binary Interface. Used to create {@link StructureInfo}s, which match a specific ABI.
 */
public enum DefaultABIs implements ABI {

    /**
     * @see <a href="https://learn.microsoft.com/en-us/cpp/build/x64-software-conventions?view=msvc-170">MSVC x64 software conventions</a>
     */
    MSVC_X64 {
        @Override
        public @NotNull StructureInfo calculateStructureLayout(
                boolean compress,
                @NotNull MemorySizeable @NotNull ... children
        ) {

            int alignment = 1;
            int[] sizes = new int[children.length * 2 + 1];
            int padding = 0;
            int position = 0;

            for(int i = 0; i < children.length; ) {
                MemorySizeable child = children[i];

                if ((position % child.getAlignment()) != 0 && !compress) {
                    int offset = (child.getAlignment() - (position % child.getAlignment()));
                    position += offset;
                    padding += offset;
                    continue;
                }

                sizes[i * 2] = padding;
                sizes[i * 2 + 1] = child.getRequiredSize();
                position += child.getRequiredSize();
                padding = 0;
                alignment = Math.max(alignment, child.getAlignment());
                i++;
            }

            if(compress)
                alignment = 1;

            if(position % alignment != 0) {
                sizes[sizes.length - 1] = (alignment - (position % alignment));
                position += sizes[sizes.length - 1];
            }
            else sizes[sizes.length - 1] = 0;

            return new StructureInfo(alignment, compress, position, sizes);
        }
    },
    CVG4J {

        /**
         * Returns the alignment of the biggest {@link MemorySizeable} with the biggest alignment in given array.
         * The size will be at least {@code min} and at most {@code max}.
         * @param min minimum size
         * @param max maximum size
         * @param vars array of {@link MemorySizeable}
         * @return clamp(min, max, biggestStruct.getRequiredSize())
         */
        @SuppressWarnings("SameParameterValue")
        private int getBiggestStructAlignment(int min, int max, @NotNull MemorySizeable @NotNull ... vars) {
            int biggest = min;
            for(MemorySizeable structure : vars)
                biggest = Math.max(biggest, structure.getAlignment());
            return Math.min(max, biggest);
        }

        @Override
        public @NotNull StructureInfo calculateStructureLayout(
                boolean compress,
                @NotNull MemorySizeable @NotNull ... children
        ) {
            int alignment = getBiggestStructAlignment(4, 16, children);
            int[] sizes = new int[children.length * 2 + 1];
            int padding = 0;
            int position = 0;

            for(int i = 0; i < children.length; ) {
                MemorySizeable structure = children[i];
                if((position % alignment) == 0 || alignment - (position % alignment) >= structure.getRequiredSize()) {

                    int itemSize = structure.getRequiredSize();
                    int itemAlignment = structure.getAlignment();
                    if(!compress && (position % itemAlignment) != 0) {
                        int offset = (itemAlignment - (position % itemAlignment));
                        position += offset;
                        padding += offset;
                        continue;
                    }

                    sizes[i * 2] = padding;
                    sizes[i * 2 + 1] = itemSize;
                    position += itemSize;
                    padding = 0;
                    i++;
                } else {
                    int offset = (alignment - (position % alignment));
                    position += offset;
                    padding += offset;
                }
            }

            if(position % alignment != 0) {
                sizes[sizes.length - 1] = (alignment - (position % alignment));
                position += sizes[sizes.length - 1];
            }
            else sizes[sizes.length - 1] = 0;

            return new StructureInfo(alignment, compress, position, sizes);
        }
    },
}
