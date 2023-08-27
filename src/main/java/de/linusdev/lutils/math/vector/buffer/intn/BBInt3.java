package de.linusdev.lutils.math.vector.buffer.intn;

import de.linusdev.lutils.math.vector.abstracts.intn.Int3;
import de.linusdev.lutils.math.vector.buffer.BBVectorInfo;
import de.linusdev.lutils.struct.info.StructureInfo;
import org.jetbrains.annotations.NotNull;

public class BBInt3 extends BBIntN implements Int3 {

    public static final @NotNull BBVectorInfo INFO = BBVectorInfo.create(ELEMENT_TYPE_NAME, MEMBER_COUNT, ELEMENT_SIZE);

    public BBInt3(boolean allocateBuffer) {
        super(MEMBER_COUNT, allocateBuffer);
    }

    @Override
    public @NotNull StructureInfo getInfo() {
        return INFO;
    }
}
