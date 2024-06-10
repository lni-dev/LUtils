package de.linusdev.lutils.math.vector.buffer.longn;

import de.linusdev.lutils.math.vector.abstracts.longn.Long3;
import de.linusdev.lutils.nat.struct.abstracts.StructureStaticVariables;
import de.linusdev.lutils.nat.struct.annos.StructValue;
import org.jetbrains.annotations.Nullable;

public class BBLong3 extends BBLongN implements Long3 {

    public static final BBVectorGenerator GENERATOR = new BBVectorGenerator(MEMBER_COUNT, ELEMENT_NATIVE_TYPE);

    /**
     * @see StructureStaticVariables#newUnallocated()
     */
    public static BBLong3 newUnallocated() {
        return new BBLong3(false, null);
    }

    /**
     * @see StructureStaticVariables#newAllocatable()
     */
    public static BBLong3 newAllocatable(@Nullable StructValue structValue) {
        return new BBLong3(true, structValue);
    }

    /**
     * @see StructureStaticVariables#newAllocated()
     */
    public static BBLong3 newAllocated(@Nullable StructValue structValue) {
        BBLong3 ret = newAllocatable(structValue);
        ret.allocate();
        return ret;
    }


    protected BBLong3(boolean generateInfo, @Nullable StructValue structValue) {
        super(GENERATOR, generateInfo, structValue);
    }
}
