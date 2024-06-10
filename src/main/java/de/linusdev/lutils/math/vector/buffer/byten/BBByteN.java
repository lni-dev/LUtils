package de.linusdev.lutils.math.vector.buffer.byten;

import de.linusdev.lutils.math.vector.Vector;
import de.linusdev.lutils.math.vector.abstracts.byten.ByteN;
import de.linusdev.lutils.math.vector.buffer.BBVector;
import de.linusdev.lutils.nat.struct.abstracts.Structure;
import de.linusdev.lutils.nat.struct.annos.StructValue;
import de.linusdev.lutils.nat.struct.info.StructureInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class BBByteN extends BBVector implements ByteN {

    public BBByteN(
            @NotNull BBVectorGenerator generator,
            boolean generateInfo,
            @Nullable StructValue structValue
    ) {
        super(generator, generateInfo, structValue);
    }


    @Override
    protected void useBuffer(
            @NotNull Structure mostParentStructure,
            int offset,
            @NotNull StructureInfo info
    ) {
        super.useBuffer(mostParentStructure, offset, info);
    }

    @Override
    public String toString() {
        return toString(
                ELEMENT_TYPE_NAME + getMemberCount(),
                Vector.toString(this, ELEMENT_TYPE_NAME, BBByteN::get)
        );
    }

    @Override
    public byte get(int index) {
        return byteBuf.get(posInBuf(index));
    }

    @Override
    public void put(int index, byte value) {
        byteBuf.put(posInBuf(index), value);
    }

}
