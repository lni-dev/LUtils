package de.linusdev.lutils.math.vector.buffer.intn;

import de.linusdev.lutils.math.vector.UnsignedVector;
import de.linusdev.lutils.nat.struct.abstracts.StructureStaticVariables;
import de.linusdev.lutils.nat.struct.annos.StructValue;
import org.jetbrains.annotations.Nullable;

public class BBUInt1 extends BBInt1 implements UnsignedVector {

    /**
     * @see StructureStaticVariables#newUnallocated()
     */
    public static BBUInt1 newUnallocated() {
        return new BBUInt1(false, null);
    }

    /**
     * @see StructureStaticVariables#newAllocatable()
     */
    public static BBUInt1 newAllocatable(@Nullable StructValue structValue) {
        return new BBUInt1(true, structValue);
    }

    /**
     * @see StructureStaticVariables#newAllocated()
     */
    public static BBUInt1 newAllocated(@Nullable StructValue structValue) {
        BBUInt1 ret = newAllocatable(structValue);
        ret.allocate();
        return ret;
    }

    protected BBUInt1(boolean generateInfo, @Nullable StructValue structValue) {
        super(generateInfo, structValue);
    }
}
