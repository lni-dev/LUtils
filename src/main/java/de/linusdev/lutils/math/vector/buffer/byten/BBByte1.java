package de.linusdev.lutils.math.vector.buffer.byten;

import de.linusdev.lutils.math.vector.abstracts.byten.Byte1;
import de.linusdev.lutils.nat.struct.abstracts.StructureStaticVariables;
import de.linusdev.lutils.nat.struct.annos.StructValue;
import org.jetbrains.annotations.Nullable;

public class BBByte1 extends BBByteN implements Byte1 {

    public static final BBVectorGenerator GENERATOR = new BBVectorGenerator(MEMBER_COUNT, ELEMENT_NATIVE_TYPE);

    /**
     * @see StructureStaticVariables#newUnallocated()
     */
    public static BBByte1 newUnallocated() {
        return new BBByte1(false, null);
    }

    /**
     * @see StructureStaticVariables#newAllocatable(StructValue) 
     */
    public static BBByte1 newAllocatable(@Nullable StructValue structValue) {
        return new BBByte1(true, structValue);
    }

    /**
     * @see StructureStaticVariables#newAllocated(StructValue) 
     */
    public static BBByte1 newAllocated(@Nullable StructValue structValue) {
        BBByte1 ret = newAllocatable(structValue);
        ret.allocate();
        return ret;
    }


    protected BBByte1(boolean generateInfo, @Nullable StructValue structValue) {
        super(GENERATOR, generateInfo, structValue);
    }
}
