package de.linusdev.lutils.nat.abi;

import de.linusdev.lutils.nat.MemorySizeable;
import de.linusdev.lutils.nat.NativeType;
import de.linusdev.lutils.nat.struct.info.ArrayInfo;
import de.linusdev.lutils.nat.struct.info.StructureInfo;
import org.jetbrains.annotations.NotNull;

/**
 * Application Binary Interface. Used to create {@link StructureInfo}s, which match a specific ABI.
 */
public enum DefaultABIs implements ABI, Types {

    /**
     * @see <a href="https://learn.microsoft.com/en-us/cpp/build/x64-software-conventions?view=msvc-170">MSVC x64 software conventions</a>
     */
    MSVC_X64 {

        private final @NotNull MemorySizeable INT8 = MemorySizeable.of(1);
        private final @NotNull MemorySizeable INT16 = MemorySizeable.of(2);
        private final @NotNull MemorySizeable INT32 = MemorySizeable.of(4);
        private final @NotNull MemorySizeable INT64 = MemorySizeable.of(8);
        private final @NotNull MemorySizeable FLOAT32 = MemorySizeable.of(4);
        private final @NotNull MemorySizeable FLOAT64 = MemorySizeable.of(8);
        private final @NotNull MemorySizeable POINTER64 = MemorySizeable.of(8);

        @Override
        public @NotNull ABI getAbi() {
            return this;
        }

        @Override
        public @NotNull MemorySizeable int8() {
            return INT8;
        }

        @Override
        public @NotNull MemorySizeable int16() {
            return INT16;
        }

        @Override
        public @NotNull MemorySizeable int32() {
            return INT32;
        }

        @Override
        public @NotNull MemorySizeable int64() {
            return INT64;
        }

        @Override
        public @NotNull MemorySizeable float32() {
            return FLOAT32;
        }

        @Override
        public @NotNull MemorySizeable float64() {
            return FLOAT64;
        }

        @Override
        public @NotNull MemorySizeable pointer() {
            return POINTER64;
        }

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

        @Override
        public @NotNull ArrayInfo calculateArrayLayout(
                boolean compress,
                @NotNull MemorySizeable children,
                int length,
                int stride
        ) {
            int alignment = children.getAlignment();

            if(stride == -1) {
                stride = children.getRequiredSize();

                if(!compress && stride < alignment)
                    stride = alignment;

                if(!compress && (stride % alignment) != 0) {
                    stride += alignment - (stride % alignment);
                }
            }

            int finalStride = stride;
            return new ArrayInfo(
                    alignment,
                    compress,
                    stride * length,
                    new int[]{0, stride * length, 0},
                    length,
                    index -> index * finalStride
            );
        }

        @Override
        public @NotNull Types types() {
            return this;
        }
    },

    /**
     * Used for OPEN_CL, but structure code in OpenCL must be generated with {@link de.linusdev.lutils.nat.struct.generator.StructCodeGenerator StructCodeGenerator}.
     */
    CVG4J_OPEN_CL {
        private final @NotNull MemorySizeable INT8 = MemorySizeable.of(1);
        private final @NotNull MemorySizeable INT16 = MemorySizeable.of(2);
        private final @NotNull MemorySizeable INT32 = MemorySizeable.of(4);
        private final @NotNull MemorySizeable INT64 = MemorySizeable.of(8);
        private final @NotNull MemorySizeable FLOAT32 = MemorySizeable.of(4);
        private final @NotNull MemorySizeable FLOAT64 = MemorySizeable.of(8);
        private final @NotNull MemorySizeable POINTER64 = MemorySizeable.of(8);

        @Override
        public @NotNull ABI getAbi() {
            return this;
        }

        @Override
        public @NotNull MemorySizeable int8() {
            return INT8;
        }

        @Override
        public @NotNull MemorySizeable int16() {
            return INT16;
        }

        @Override
        public @NotNull MemorySizeable int32() {
            return INT32;
        }

        @Override
        public @NotNull MemorySizeable int64() {
            return INT64;
        }

        @Override
        public @NotNull MemorySizeable float32() {
            return FLOAT32;
        }

        @Override
        public @NotNull MemorySizeable float64() {
            return FLOAT64;
        }

        @Override
        public @NotNull MemorySizeable pointer() {
            return POINTER64;
        }

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

        @Override
        public @NotNull ArrayInfo calculateArrayLayout(boolean compress, @NotNull MemorySizeable children, int length, int stride) {
            return MSVC_X64.calculateArrayLayout(
                    compress,
                    children,
                    length,
                    stride
            );
        }

        @Override
        public @NotNull ArrayInfo calculateVectorLayout(@NotNull NativeType componentType, int length) {
            // OpenCL's specification states:
            // "A built-in data type that is not a power of two bytes in size must be aligned to the next larger
            // power of two. This rule applies to built-in types only, not structs or unions."
            // "For 3-component vector data types, the size of the data type is 4 * sizeof(component). This means
            // that a 3-component vector data type will be aligned to a 4 * sizeof(component) boundary."

            int componentSize = componentType.getMemorySizeable(this).getRequiredSize();
            int originalSize = componentSize * length;
            int size = originalSize;

            if( Integer.bitCount(size) != 1) {
                //expand size to the next power of 2
                size = Integer.highestOneBit(size) << 1;
            }

            return new ArrayInfo(
                    size,
                    false,
                    size,
                    new int[]{0, originalSize, size-originalSize},
                    length,
                    index -> index * componentSize
            );
        }

        @Override
        public @NotNull Types types() {
            return this;
        }
    },
    ;

    @Override
    public @NotNull String identifier() {
        return name();
    }
}
