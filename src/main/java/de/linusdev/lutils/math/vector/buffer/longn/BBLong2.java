package de.linusdev.lutils.math.vector.buffer.longn;

import de.linusdev.lutils.math.vector.abstracts.longn.Long2;
import de.linusdev.lutils.math.vector.buffer.BBVectorInfo;
import de.linusdev.lutils.struct.info.StructureInfo;
import org.jetbrains.annotations.NotNull;

public class BBLong2 extends BBLongN implements Long2 {

    public static final @NotNull BBVectorInfo INFO = BBVectorInfo.create(ELEMENT_TYPE_NAME, MEMBER_COUNT, ELEMENT_SIZE);

    public BBLong2(boolean allocateBuffer) {
        super(MEMBER_COUNT, allocateBuffer);
    }

    @Override
    public @NotNull StructureInfo getInfo() {
        return INFO;
    }
}
