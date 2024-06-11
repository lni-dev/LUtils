package de.linusdev.lutils.math.vector.buffer.shortn;

import de.linusdev.lutils.math.vector.UnsignedVector;
import de.linusdev.lutils.nat.struct.abstracts.StructureStaticVariables;
import de.linusdev.lutils.nat.struct.annos.StructValue;
import org.jetbrains.annotations.Nullable;

public class BBUShort1 extends BBShort1 implements UnsignedVector {

    /**
     * @see StructureStaticVariables#newUnallocated()
     */
    public static BBUShort1 newUnallocated() {
        return new BBUShort1(false, null);
    }

    /**
     * @see StructureStaticVariables#newAllocatable(StructValue) 
     */
    public static BBUShort1 newAllocatable(@Nullable StructValue structValue) {
        return new BBUShort1(true, structValue);
    }

    /**
     * @see StructureStaticVariables#newAllocated(StructValue) 
     */
    public static BBUShort1 newAllocated(@Nullable StructValue structValue) {
        BBUShort1 ret = newAllocatable(structValue);
        ret.allocate();
        return ret;
    }

    protected BBUShort1(boolean generateInfo, @Nullable StructValue structValue) {
        super(generateInfo, structValue);
    }

}
