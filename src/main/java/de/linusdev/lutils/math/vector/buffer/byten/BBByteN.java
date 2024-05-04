package de.linusdev.lutils.math.vector.buffer.byten;

import de.linusdev.lutils.math.vector.Vector;
import de.linusdev.lutils.math.vector.abstracts.byten.ByteN;
import de.linusdev.lutils.math.vector.buffer.BBVector;
import de.linusdev.lutils.struct.abstracts.Structure;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

public abstract class BBByteN extends BBVector implements ByteN {

    protected ByteBuffer buf;

    public BBByteN(boolean allocateBuffer) {
        if(allocateBuffer)
            allocate();
    }

    @Override
    protected void useBuffer(@NotNull Structure mostParentStructure, int offset) {
        super.useBuffer(mostParentStructure, offset);
        buf = byteBuf;
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
        return buf.get(index);
    }

    @Override
    public void put(int index, byte value) {
        buf.put(index, value);
    }

}
