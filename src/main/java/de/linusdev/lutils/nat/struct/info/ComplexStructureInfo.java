package de.linusdev.lutils.nat.struct.info;

import de.linusdev.lutils.nat.abi.OverwriteChildABI;
import de.linusdev.lutils.nat.struct.abstracts.ComplexStructure;
import de.linusdev.lutils.nat.struct.abstracts.Structure;
import de.linusdev.lutils.nat.struct.annos.StructValue;
import de.linusdev.lutils.nat.abi.ABI;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * Additional to the information contained in {@link StructureInfo}, this class also contains information about
 * the values inside the structure ({@link #childrenInfo}) and the {@link ABI} used to create this info ({@link #abi}).
 */
public class ComplexStructureInfo extends StructureInfo {

    /**
     * Reads all fields of given {@code clazz} annotated with {@link StructValue}
     * and generates a {@link ComplexStructureInfo} from it.
     * @param clazz class extending {@link ComplexStructure}
     * @return {@link ComplexStructureInfo} for this structure
     */
    public static @NotNull ComplexStructureInfo generateFromStructVars(
            @NotNull Class<?> clazz,
            @NotNull ABI abi,
            @Nullable OverwriteChildABI overwriteChildAbi
    ) {
        Field[] fields = clazz.getFields();

        StructVarInfo[] varInfos = new StructVarInfo[fields.length];
        int index = 0;
        int size = 0;

        for(Field field : fields) {
            StructVarInfo info = StructVarInfo.ofField(field, abi, overwriteChildAbi);

            if(info == null) continue;

            //get INFO
            if(info.getStructValue().value() == -1) varInfos[index++] = info;
            else varInfos[info.getStructValue().value()] = info;
            size++;
        }

        StructureInfo[] infos = new StructureInfo[size];
        for (int i = 0; i < size; i++)
            infos[i] = varInfos[i].getInfo();

        StructureInfo info = abi.calculateStructureLayout(false, infos);

        return new ComplexStructureInfo(
                info.getAlignment(),
                info.isCompressed(),
                info.getRequiredSize(),
                info.getSizes(),
                abi,
                Arrays.copyOf(varInfos, size)
        );
    }

    /**
     * {@link ABI} used to create this info.
     */
    protected final @NotNull ABI abi;

    /**
     * information about values inside the structure
     */
    protected final @NotNull StructVarInfo @NotNull [] childrenInfo;

    private ComplexStructureInfo(
            int alignment,
            boolean compress,
            int size,
            int[] sizes, @NotNull ABI abi,
            @NotNull StructVarInfo @NotNull [] infos
    ) {
        super(alignment, compress, size, sizes);
        this.abi = abi;
        this.childrenInfo = infos;
    }

    /**
     * @see #abi
     */
    public @NotNull ABI getABI() {
        return abi;
    }

    /**
     * @see #childrenInfo
     */
    public @NotNull StructVarInfo @NotNull [] getChildrenInfo() {
        return childrenInfo;
    }

    /**
     * Gets all children through reflection. This should be used sparsely. But it is required if this {@link StructureInfo}
     * was automatically generated (see {@link #generateFromStructVars(Class, ABI, OverwriteChildABI)}) with no {@link StructValue#value() element order} specified.
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
