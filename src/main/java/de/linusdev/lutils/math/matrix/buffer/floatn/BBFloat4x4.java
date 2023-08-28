package de.linusdev.lutils.math.matrix.buffer.floatn;

import de.linusdev.lutils.math.matrix.abstracts.floatn.Float4x4;
import de.linusdev.lutils.math.matrix.buffer.BBMatrixInfo;
import de.linusdev.lutils.struct.info.StructureInfo;
import org.jetbrains.annotations.NotNull;

public class BBFloat4x4 extends BBFloatMxN implements Float4x4 {

    public static final @NotNull BBMatrixInfo INFO = BBMatrixInfo.create(WIDTH, HEIGHT, ELEMENT_SIZE, ELEMENT_TYPE_NAME);

    public BBFloat4x4(boolean allocateBuffer) {
        super(allocateBuffer);
    }

    @Override
    public @NotNull StructureInfo getInfo() {
        return INFO;
    }
}
