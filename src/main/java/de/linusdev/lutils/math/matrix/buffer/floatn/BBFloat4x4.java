package de.linusdev.lutils.math.matrix.buffer.floatn;

import de.linusdev.lutils.math.matrix.abstracts.floatn.Float4x4;
import de.linusdev.lutils.nat.struct.abstracts.StructureStaticVariables;
import de.linusdev.lutils.nat.struct.annos.StructValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BBFloat4x4 extends BBFloatMxN implements Float4x4 {

    public static final @NotNull BBMatrixGenerator GENERATOR = new BBMatrixGenerator(WIDTH, HEIGHT, ELEMENT_NATIVE_TYPE);

    /**
     * @see StructureStaticVariables#newUnallocated()
     */
    public static BBFloat4x4 newUnallocated() {
        return new BBFloat4x4(false, null);
    }

    /**
     * @see StructureStaticVariables#newAllocatable(StructValue) 
     */
    public static BBFloat4x4 newAllocatable(@Nullable StructValue structValue) {
        return new BBFloat4x4(true, structValue);
    }

    /**
     * @see StructureStaticVariables#newAllocated(StructValue) 
     */
    public static BBFloat4x4 newAllocated(@Nullable StructValue structValue) {
        BBFloat4x4 ret = newAllocatable(structValue);
        ret.allocate();
        return ret;
    }


    protected BBFloat4x4(boolean generateInfo, @Nullable StructValue structValue) {
        super(GENERATOR, generateInfo, structValue);
    }
}
