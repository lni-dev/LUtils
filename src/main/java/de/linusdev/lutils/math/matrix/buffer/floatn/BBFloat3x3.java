package de.linusdev.lutils.math.matrix.buffer.floatn;

import de.linusdev.lutils.math.matrix.abstracts.floatn.Float3x3;
import de.linusdev.lutils.nat.struct.abstracts.StructureStaticVariables;
import de.linusdev.lutils.nat.struct.annos.StructValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BBFloat3x3 extends BBFloatMxN implements Float3x3 {

    public static final @NotNull BBMatrixGenerator GENERATOR = new BBMatrixGenerator(WIDTH, HEIGHT, ELEMENT_NATIVE_TYPE);

    /**
     * @see StructureStaticVariables#newUnallocated()
     */
    public static BBFloat3x3 newUnallocated() {
        return new BBFloat3x3(false, null);
    }

    /**
     * @see StructureStaticVariables#newAllocatable()
     */
    public static BBFloat3x3 newAllocatable(@Nullable StructValue structValue) {
        return new BBFloat3x3(true, structValue);
    }

    /**
     * @see StructureStaticVariables#newAllocated()
     */
    public static BBFloat3x3 newAllocated(@Nullable StructValue structValue) {
        BBFloat3x3 ret = newAllocatable(structValue);
        ret.allocate();
        return ret;
    }
    

    protected BBFloat3x3(boolean generateInfo, @Nullable StructValue structValue) {
        super(GENERATOR, generateInfo, structValue);
    }
}
