package de.linusdev.lutils.nat.pointer;

import de.linusdev.lutils.math.vector.buffer.longn.BBLong1;
import de.linusdev.lutils.nat.struct.abstracts.StructureStaticVariables;
import de.linusdev.lutils.nat.struct.annos.StructValue;
import org.jetbrains.annotations.Nullable;

/**
 * This class can store a 64 bit pointer.
 */
@SuppressWarnings("unused")
public class Pointer64 extends BBLong1 {
    /**
     * @see StructureStaticVariables#newUnallocated()
     */
    public static Pointer64 newUnallocated()  {
        return new Pointer64(false, null);
    }

    /**
     * @see StructureStaticVariables#newAllocatable()
     */
    public static Pointer64 newAllocatable(
            @Nullable StructValue structValue
    )  {
        return new Pointer64(true, structValue);
    }

    /**
     * @see StructureStaticVariables#newAllocated()
     */
    public static Pointer64 newAllocated(
            @Nullable StructValue structValue
    )  {
        Pointer64 ret = newAllocatable(structValue);
        ret.allocate();
        return ret;
    }

    protected Pointer64(
            boolean generateInfo,
            @Nullable StructValue structValue
    ) {
        super(generateInfo, structValue);
    }
}
