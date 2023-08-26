package de.linusdev.lutils.struct.info;

import de.linusdev.lutils.struct.abstracts.ComplexStructure;
import de.linusdev.lutils.struct.abstracts.Structure;
import de.linusdev.lutils.struct.annos.StructValue;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.Arrays;


public class ComplexStructureInfo extends StructureInfo {

    public static @NotNull ComplexStructureInfo generateFromStructVars(
            int alignment,
            boolean compress,
            @NotNull StructVarInfo @NotNull [] vars
    ) {
        int[] sizes = new int[vars.length * 2 + 1];
        int padding = 0;
        int position = 0;

        for(int i = 0; i < vars.length; ) {
            StructureInfo structure = vars[i].getInfo();
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

        return new ComplexStructureInfo(alignment, compress, position, sizes,  vars);
    }

    /**
     * Returns the size of the biggest {@link Structure} in given array.
     * The size will be at least {@code min} and at most {@code max}.
     * @param min minimum size
     * @param max maximum size
     * @param vars array of {@link Structure}
     * @return clamp(min, max, biggestStruct.getRequiredSize())
     */
    @SuppressWarnings("SameParameterValue")
    private static int getBiggestStructAlignment(int min, int max, @NotNull StructVarInfo @NotNull ... vars) {
        int biggest = min;
        for(StructVarInfo structure : vars)
            biggest = Math.max(biggest, structure.getInfo().getAlignment());
        return Math.min(max, biggest);
    }

    public static @NotNull ComplexStructureInfo generateFromStructVars(@NotNull StructVarInfo @NotNull [] vars) {
        return generateFromStructVars(
                getBiggestStructAlignment(4, 16, vars),
                false,
                vars
        );
    }

    public static @NotNull ComplexStructureInfo generateFromStructVars(@NotNull Class<?> clazz) {
        Field[] fields = clazz.getFields();

        StructVarInfo[] infos = new StructVarInfo[fields.length];
        int index = 0;
        int size = 0;

        for(Field field : fields) {
            StructVarInfo info = StructVarInfo.ofField(field);

            if(info == null) continue;

            //get INFO
            if(info.getStructValue().value() == -1) infos[index++] = info;
            else infos[info.getStructValue().value()] = info;
            size++;
        }

        return generateFromStructVars(Arrays.copyOf(infos, size));
    }

    protected final @NotNull StructVarInfo @NotNull [] childrenInfo;

    private ComplexStructureInfo(
            int alignment,
            boolean compress,
            int size,
            int[] sizes,
            @NotNull StructVarInfo @NotNull [] infos
    ) {
        super(alignment, compress, size, sizes);
        this.childrenInfo = infos;
    }

    public @NotNull StructVarInfo @NotNull [] getChildrenInfo() {
        return childrenInfo;
    }

    /**
     * Gets all children through reflection. This should be used sparsely. But it is required if this {@link StructureInfo}
     * was automatically generated (see {@link #generateFromStructVars(Class)}) with no {@link StructValue#value() element order} specified.
     * @param instance instance of the {@link ComplexStructure} this info belongs to
     * @return children {@link Structure} array
     */
    public @NotNull Structure @NotNull [] getChildren(@NotNull ComplexStructure instance) {
        Structure[] structures = new Structure[childrenInfo.length];

        for(int i = 0; i < structures.length; i++) {
            structures[i] = childrenInfo[i].get(instance);
        }

        return structures;
    }
}
