package de.linusdev.lutils.math.vector.buffer.doublen;

import de.linusdev.lutils.math.vector.Vector;
import de.linusdev.lutils.math.vector.abstracts.doublen.DoubleN;
import de.linusdev.lutils.math.vector.buffer.BBVector;
import de.linusdev.lutils.struct.abstracts.Structure;
import org.jetbrains.annotations.NotNull;

import java.nio.DoubleBuffer;

public abstract class BBDoubleN extends BBVector implements DoubleN {

    protected DoubleBuffer buf;

    public BBDoubleN(boolean allocateBuffer) {
        if(allocateBuffer)
            allocate();
    }

    @Override
    protected void useBuffer(@NotNull Structure mostParentStructure, int offset) {
        super.useBuffer(mostParentStructure, offset);
        buf = byteBuf.asDoubleBuffer();
    }

    @Override
    public String toString() {
        return toString(
                ELEMENT_TYPE_NAME + getMemberCount(),
                Vector.toString(this, ELEMENT_TYPE_NAME, BBDoubleN::get)
        );
    }

    @Override
    public double get(int index) {
        return buf.get(index);
    }

    @Override
    public void put(int index, double value) {
        buf.put(index, value);
    }

}
