package de.linusdev.lutils.nat.pointer;

import de.linusdev.lutils.math.vector.buffer.longn.BBLong1;
import de.linusdev.lutils.nat.struct.abstracts.StructureStaticVariables;
import de.linusdev.lutils.nat.struct.annos.StructValue;
import org.jetbrains.annotations.Nullable;

/**
 * This class can store a 64 bit pointer.
 */
@SuppressWarnings("unused")
public class BBPointer64 extends BBLong1 implements Pointer64{
    /**
     * @see StructureStaticVariables#newUnallocated()
     */
    public static BBPointer64 newUnallocated()  {
        return new BBPointer64(false, null);
    }

    /**
     * @see StructureStaticVariables#newAllocatable(StructValue) 
     */
    public static BBPointer64 newAllocatable(
            @Nullable StructValue structValue
    )  {
        return new BBPointer64(true, structValue);
    }

    /**
     * @see StructureStaticVariables#newAllocated(StructValue) 
     */
    public static BBPointer64 newAllocated(
            @Nullable StructValue structValue
    )  {
        BBPointer64 ret = newAllocatable(structValue);
        ret.allocate();
        return ret;
    }

    protected BBPointer64(
            boolean generateInfo,
            @Nullable StructValue structValue
    ) {
        super(generateInfo, structValue);
    }

    @Override
    public long get() {
        return super.get();
    }

    @Override
    public void set(long f) {
        super.set(f);
    }
}
