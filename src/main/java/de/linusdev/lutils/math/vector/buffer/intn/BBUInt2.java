package de.linusdev.lutils.math.vector.buffer.intn;

import de.linusdev.lutils.math.vector.UnsignedVector;
import de.linusdev.lutils.nat.struct.abstracts.StructureStaticVariables;
import de.linusdev.lutils.nat.struct.annos.StructValue;
import org.jetbrains.annotations.Nullable;

public class BBUInt2 extends BBInt2 implements UnsignedVector {

    /**
     * @see StructureStaticVariables#newUnallocated()
     */
    public static BBUInt2 newUnallocated() {
        return new BBUInt2(false, null);
    }

    /**
     * @see StructureStaticVariables#newAllocatable(StructValue)
     */
    public static BBUInt2 newAllocatable(@Nullable StructValue structValue) {
        return new BBUInt2(true, structValue);
    }

    /**
     * @see StructureStaticVariables#newAllocated(StructValue)
     */
    public static BBUInt2 newAllocated(@Nullable StructValue structValue) {
        BBUInt2 ret = newAllocatable(structValue);
        ret.allocate();
        return ret;
    }

    protected BBUInt2(boolean generateInfo, @Nullable StructValue structValue) {
        super(generateInfo, structValue);
    }
}
