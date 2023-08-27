package de.linusdev.lutils.math.vector.abstracts.vectorn;

import de.linusdev.lutils.math.vector.Vector;

public interface Vector1 extends Vector {

    int MEMBER_COUNT = 1;

    @Override
    default int getMemberCount() {
        return MEMBER_COUNT;
    }
}
