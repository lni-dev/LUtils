package de.linusdev.lutils.math.vector.buffer.doublen;

import de.linusdev.lutils.math.vector.abstracts.doublen.Double1;
import de.linusdev.lutils.nat.struct.abstracts.StructureStaticVariables;
import de.linusdev.lutils.nat.struct.annos.StructValue;
import org.jetbrains.annotations.Nullable;

public class BBDouble1 extends BBDoubleN implements Double1 {

    public static final BBVectorGenerator GENERATOR = new BBVectorGenerator(MEMBER_COUNT, ELEMENT_NATIVE_TYPE);

    /**
     * @see StructureStaticVariables#newUnallocated()
     */
    public static BBDouble1 newUnallocated() {
        return new BBDouble1(false, null);
    }

    /**
     * @see StructureStaticVariables#newAllocatable(StructValue) 
     */
    public static BBDouble1 newAllocatable(@Nullable StructValue structValue) {
        return new BBDouble1(true, structValue);
    }

    /**
     * @see StructureStaticVariables#newAllocated(StructValue) 
     */
    public static BBDouble1 newAllocated(@Nullable StructValue structValue) {
        BBDouble1 ret = newAllocatable(structValue);
        ret.allocate();
        return ret;
    }


    protected BBDouble1(boolean generateInfo, @Nullable StructValue structValue) {
        super(GENERATOR, generateInfo, structValue);
    }
}
