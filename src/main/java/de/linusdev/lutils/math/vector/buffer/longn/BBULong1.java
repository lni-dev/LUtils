package de.linusdev.lutils.math.vector.buffer.longn;

import de.linusdev.lutils.math.vector.UnsignedVector;
import de.linusdev.lutils.nat.struct.abstracts.StructureStaticVariables;
import de.linusdev.lutils.nat.struct.annos.StructValue;
import org.jetbrains.annotations.Nullable;

public class BBULong1 extends BBLong1 implements UnsignedVector {

    /**
     * @see StructureStaticVariables#newUnallocated()
     */
    public static BBULong1 newUnallocated() {
        return new BBULong1(false, null);
    }

    /**
     * @see StructureStaticVariables#newAllocatable(StructValue) 
     */
    public static BBULong1 newAllocatable(@Nullable StructValue structValue) {
        return new BBULong1(true, structValue);
    }

    /**
     * @see StructureStaticVariables#newAllocated(StructValue) 
     */
    public static BBULong1 newAllocated(@Nullable StructValue structValue) {
        BBULong1 ret = newAllocatable(structValue);
        ret.allocate();
        return ret;
    }

    protected BBULong1(boolean generateInfo, @Nullable StructValue structValue) {
        super(generateInfo, structValue);
    }

}
