package de.linusdev.lutils.math.matrix.buffer.floatn;

import de.linusdev.lutils.math.matrix.abstracts.floatn.Float3x3;
import de.linusdev.lutils.math.matrix.buffer.BBMatrixInfo;
import de.linusdev.lutils.struct.info.StructureInfo;
import org.jetbrains.annotations.NotNull;

public class BBFloat3x3 extends BBFloatMxN implements Float3x3 {

    public static final @NotNull BBMatrixInfo INFO = BBMatrixInfo.create(WIDTH, HEIGHT, ELEMENT_SIZE, ELEMENT_TYPE_NAME);

    public BBFloat3x3(boolean allocateBuffer) {
        super(allocateBuffer);
    }

    @Override
    public @NotNull StructureInfo getInfo() {
        return INFO;
    }
}
