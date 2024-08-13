package de.linusdev.lutils.nat.pointer;

import de.linusdev.lutils.nat.NativeParsable;
import de.linusdev.lutils.nat.struct.abstracts.StructureStaticVariables;
import de.linusdev.lutils.nat.struct.annos.StructValue;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public class BBTypedPointer64<T extends NativeParsable> extends BBPointer64 implements TypedPointer64<T> {

    /**
     * @see StructureStaticVariables#newUnallocated()
     */
    public static <T extends NativeParsable> BBTypedPointer64<T> newUnallocated1()  {
        return new BBTypedPointer64<>(false, null);
    }

    /**
     * @see StructureStaticVariables#newAllocatable(StructValue) 
     */
    public static <T extends NativeParsable> BBTypedPointer64<T> newAllocatable1(
            @Nullable StructValue structValue
    )  {
        return new BBTypedPointer64<>(true, structValue);
    }

    /**
     * @see StructureStaticVariables#newAllocated(StructValue) 
     */
    public static <T extends NativeParsable> BBTypedPointer64<T> newAllocated1(
            @Nullable StructValue structValue
    )  {
        BBTypedPointer64<T> ret = newAllocatable1(structValue);
        ret.allocate();
        return ret;
    }

    protected BBTypedPointer64(boolean generateInfo, @Nullable StructValue structValue) {
        super(generateInfo, structValue);
    }

}
