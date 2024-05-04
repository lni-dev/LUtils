package de.linusdev.lutils.math.vector.buffer.shortn;

import de.linusdev.lutils.math.vector.Vector;
import de.linusdev.lutils.math.vector.abstracts.shortn.ShortN;
import de.linusdev.lutils.math.vector.buffer.BBVector;
import de.linusdev.lutils.struct.abstracts.Structure;
import org.jetbrains.annotations.NotNull;

import java.nio.ShortBuffer;

public abstract class BBShortN extends BBVector implements ShortN {

    protected ShortBuffer buf;

    public BBShortN(boolean allocateBuffer) {
        if(allocateBuffer)
            allocate();
    }

    @Override
    protected void useBuffer(@NotNull Structure mostParentStructure, int offset) {
        super.useBuffer(mostParentStructure, offset);
        buf = byteBuf.asShortBuffer();
    }

    @Override
    public String toString() {
        return toString(
                ELEMENT_TYPE_NAME + getMemberCount(),
                Vector.toString(this, ELEMENT_TYPE_NAME, BBShortN::get)
        );
    }

    @Override
    public short get(int index) {
        return buf.get(index);
    }

    @Override
    public void put(int index, short value) {
        buf.put(index, value);
    }

}
