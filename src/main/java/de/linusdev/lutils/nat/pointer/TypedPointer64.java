package de.linusdev.lutils.nat.pointer;

import de.linusdev.lutils.nat.NativeParsable;
import de.linusdev.lutils.nat.struct.abstracts.StructureStaticVariables;
import de.linusdev.lutils.nat.struct.annos.StructValue;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public class TypedPointer64<T extends NativeParsable> extends Pointer64 {

    /**
     * @see StructureStaticVariables#newUnallocated()
     */
    public static <T extends NativeParsable> TypedPointer64<T> newUnallocated1()  {
        return new TypedPointer64<>(false, null);
    }

    /**
     * @see StructureStaticVariables#newAllocatable()
     */
    public static <T extends NativeParsable> TypedPointer64<T> newAllocatable1(
            @Nullable StructValue structValue
    )  {
        return new TypedPointer64<>(true, structValue);
    }

    /**
     * @see StructureStaticVariables#newAllocated()
     */
    public static <T extends NativeParsable> TypedPointer64<T> newAllocated1(
            @Nullable StructValue structValue
    )  {
        TypedPointer64<T> ret = newAllocatable1(structValue);
        ret.allocate();
        return ret;
    }

    protected TypedPointer64(boolean generateInfo, @Nullable StructValue structValue) {
        super(generateInfo, structValue);
    }

    public void set(@Nullable T object) {
        if(object == null) set(0);
        else set(object.getPointer());
    }

}
