package de.linusdev.lutils.nat.pointer;

import de.linusdev.lutils.math.vector.buffer.intn.BBInt1;
import de.linusdev.lutils.nat.struct.abstracts.StructureStaticVariables;
import de.linusdev.lutils.nat.struct.annos.StructValue;
import org.jetbrains.annotations.Nullable;

/**
 * This class can store a 32 bit pointer.
 */
@SuppressWarnings("unused")
public class BBPointer32 extends BBInt1 implements Pointer32{

    /**
     * @see StructureStaticVariables#newUnallocated()
     */
    public static BBPointer32 newUnallocated()  {
        return new BBPointer32(false, null);
    }

    /**
     * @see StructureStaticVariables#newAllocatable(StructValue) 
     */
    public static BBPointer32 newAllocatable(
            @Nullable StructValue structValue
    )  {
        return new BBPointer32(true, structValue);
    }

    /**
     * @see StructureStaticVariables#newAllocated(StructValue) 
     */
    public static BBPointer32 newAllocated(
            @Nullable StructValue structValue
    )  {
        BBPointer32 ret = newAllocatable(structValue);
        ret.allocate();
        return ret;
    }

    protected BBPointer32(
            boolean generateInfo,
            @Nullable StructValue structValue
    ) {
        super(generateInfo, structValue);
    }

    /**
     *
     * @return {@code true} if this pointer points to null (0), {@code false} otherwise
     */
    public boolean isNullPtr() {
        return get() == 0;
    }

    @Override
    public int get() {
        return super.get();
    }

    @Override
    public void set(int f) {
        super.set(f);
    }
}
