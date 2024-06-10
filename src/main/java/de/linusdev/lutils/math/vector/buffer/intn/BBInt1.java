package de.linusdev.lutils.math.vector.buffer.intn;

import de.linusdev.lutils.math.vector.abstracts.intn.Int1;
import de.linusdev.lutils.nat.struct.abstracts.StructureStaticVariables;
import de.linusdev.lutils.nat.struct.annos.StructValue;
import org.jetbrains.annotations.Nullable;

public class BBInt1 extends BBIntN implements Int1 {

    public static final BBVectorGenerator GENERATOR = new BBVectorGenerator(MEMBER_COUNT, ELEMENT_NATIVE_TYPE);

    /**
     * @see StructureStaticVariables#newUnallocated()
     */
    public static BBInt1 newUnallocated() {
        return new BBInt1(false, null);
    }

    /**
     * @see StructureStaticVariables#newAllocatable()
     */
    public static BBInt1 newAllocatable(@Nullable StructValue structValue) {
        return new BBInt1(true, structValue);
    }

    /**
     * @see StructureStaticVariables#newAllocated()
     */
    public static BBInt1 newAllocated(@Nullable StructValue structValue) {
        BBInt1 ret = newAllocatable(structValue);
        ret.allocate();
        return ret;
    }


    protected BBInt1(boolean generateInfo, @Nullable StructValue structValue) {
        super(GENERATOR, generateInfo, structValue);
    }
}
