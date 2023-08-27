package de.linusdev.lutils.math.vector.buffer.longn;

import de.linusdev.lutils.math.vector.abstracts.longn.Long1;
import de.linusdev.lutils.math.vector.buffer.BBVectorInfo;
import de.linusdev.lutils.struct.info.StructureInfo;
import org.jetbrains.annotations.NotNull;

public class BBLong1 extends BBLongN implements Long1 {

    public static final @NotNull BBVectorInfo INFO = BBVectorInfo.create(ELEMENT_TYPE_NAME, MEMBER_COUNT, ELEMENT_SIZE);

    public BBLong1(boolean allocateBuffer) {
        super(MEMBER_COUNT, allocateBuffer);
    }

    @Override
    public @NotNull StructureInfo getInfo() {
        return INFO;
    }
}
