package de.linusdev.lutils.math.vector.buffer.byten;

import de.linusdev.lutils.math.vector.UnsignedVector;
import de.linusdev.lutils.nat.struct.abstracts.StructureStaticVariables;
import de.linusdev.lutils.nat.struct.annos.StructValue;
import org.jetbrains.annotations.Nullable;

public class BBUByte1 extends BBByte1 implements UnsignedVector {

    /**
     * @see StructureStaticVariables#newUnallocated()
     */
    public static BBUByte1 newUnallocated() {
        return new BBUByte1(false, null);
    }

    /**
     * @see StructureStaticVariables#newAllocatable()
     */
    public static BBUByte1 newAllocatable(@Nullable StructValue structValue) {
        return new BBUByte1(true, structValue);
    }

    /**
     * @see StructureStaticVariables#newAllocated()
     */
    public static BBUByte1 newAllocated(@Nullable StructValue structValue) {
        BBUByte1 ret = newAllocatable(structValue);
        ret.allocate();
        return ret;
    }

    protected BBUByte1(boolean generateInfo, @Nullable StructValue structValue) {
        super(generateInfo, structValue);
    }

}
