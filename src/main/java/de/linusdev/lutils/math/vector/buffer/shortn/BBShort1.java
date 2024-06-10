package de.linusdev.lutils.math.vector.buffer.shortn;

import de.linusdev.lutils.math.vector.abstracts.shortn.Short1;
import de.linusdev.lutils.nat.struct.abstracts.StructureStaticVariables;
import de.linusdev.lutils.nat.struct.annos.StructValue;
import org.jetbrains.annotations.Nullable;

public class BBShort1 extends BBShortN implements Short1 {

    public static final BBVectorGenerator GENERATOR = new BBVectorGenerator(MEMBER_COUNT, ELEMENT_NATIVE_TYPE);

    /**
     * @see StructureStaticVariables#newUnallocated()
     */
    public static BBShort1 newUnallocated() {
        return new BBShort1(false, null);
    }

    /**
     * @see StructureStaticVariables#newAllocatable()
     */
    public static BBShort1 newAllocatable(@Nullable StructValue structValue) {
        return new BBShort1(true, structValue);
    }

    /**
     * @see StructureStaticVariables#newAllocated()
     */
    public static BBShort1 newAllocated(@Nullable StructValue structValue) {
        BBShort1 ret = newAllocatable(structValue);
        ret.allocate();
        return ret;
    }


    protected BBShort1(boolean generateInfo, @Nullable StructValue structValue) {
        super(GENERATOR, generateInfo, structValue);
    }
}
