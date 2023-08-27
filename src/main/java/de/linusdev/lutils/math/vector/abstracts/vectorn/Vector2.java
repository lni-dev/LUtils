package de.linusdev.lutils.math.vector.abstracts.vectorn;

import de.linusdev.lutils.math.vector.Vector;

public interface Vector2 extends Vector {

    int MEMBER_COUNT = 2;

    @Override
    default int getMemberCount() {
        return MEMBER_COUNT;
    }
}
