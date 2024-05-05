package de.linusdev.lutils.math.vector.buffer.shortn;

import de.linusdev.lutils.math.vector.abstracts.shortn.Short1;
import de.linusdev.lutils.math.vector.buffer.BBVectorInfo;
import de.linusdev.lutils.struct.info.StructureInfo;
import org.jetbrains.annotations.NotNull;

public class BBShort1 extends BBShortN implements Short1 {

    public static final BBVectorInfo INFO = BBVectorInfo.create(ELEMENT_TYPE_NAME, MEMBER_COUNT, ELEMENT_SIZE);

    public BBShort1() {
        this(false);
    }

    public BBShort1(boolean allocateBuffer) {
        super(allocateBuffer);
    }

    @Override
    public @NotNull StructureInfo getInfo() {
        return INFO;
    }
}
