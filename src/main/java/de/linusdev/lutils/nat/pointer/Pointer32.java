package de.linusdev.lutils.nat.pointer;

import de.linusdev.lutils.math.vector.buffer.intn.BBInt1;
import de.linusdev.lutils.nat.struct.abstracts.StructureStaticVariables;
import de.linusdev.lutils.nat.struct.annos.StructValue;
import org.jetbrains.annotations.Nullable;

/**
 * This class can store a 32 bit pointer.
 */
@SuppressWarnings("unused")
public class Pointer32 extends BBInt1 {

    /**
     * @see StructureStaticVariables#newUnallocated()
     */
    public static Pointer32 newUnallocated()  {
        return new Pointer32(false, null);
    }

    /**
     * @see StructureStaticVariables#newAllocatable(StructValue) 
     */
    public static Pointer32 newAllocatable(
            @Nullable StructValue structValue
    )  {
        return new Pointer32(true, structValue);
    }

    /**
     * @see StructureStaticVariables#newAllocated(StructValue) 
     */
    public static Pointer32 newAllocated(
            @Nullable StructValue structValue
    )  {
        Pointer32 ret = newAllocatable(structValue);
        ret.allocate();
        return ret;
    }

    protected Pointer32(
            boolean generateInfo,
            @Nullable StructValue structValue
    ) {
        super(generateInfo, structValue);
    }
}
