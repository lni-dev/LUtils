package de.linusdev.lutils.math.vector.abstracts.vectorn;

import de.linusdev.lutils.math.vector.Vector;

public interface Vector4 extends Vector {

    int MEMBER_COUNT = 4;

    @Override
    default int getMemberCount() {
        return MEMBER_COUNT;
    }
}
