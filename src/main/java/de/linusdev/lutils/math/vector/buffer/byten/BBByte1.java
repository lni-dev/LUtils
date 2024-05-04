package de.linusdev.lutils.math.vector.buffer.byten;

import de.linusdev.lutils.math.vector.abstracts.byten.Byte1;
import de.linusdev.lutils.math.vector.buffer.BBVectorInfo;
import de.linusdev.lutils.struct.info.StructureInfo;
import org.jetbrains.annotations.NotNull;

public class BBByte1 extends BBByteN implements Byte1 {

    public static final BBVectorInfo INFO = BBVectorInfo.create(ELEMENT_TYPE_NAME, MEMBER_COUNT, ELEMENT_SIZE);

    public BBByte1(boolean allocateBuffer) {
        super(allocateBuffer);
    }

    @Override
    public @NotNull StructureInfo getInfo() {
        return INFO;
    }
}
