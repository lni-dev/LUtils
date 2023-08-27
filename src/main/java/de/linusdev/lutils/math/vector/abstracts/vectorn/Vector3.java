package de.linusdev.lutils.math.vector.abstracts.vectorn;

import de.linusdev.lutils.math.vector.Vector;

public interface Vector3 extends Vector {

    int MEMBER_COUNT = 3;

    @Override
    default int getMemberCount() {
        return MEMBER_COUNT;
    }
}
