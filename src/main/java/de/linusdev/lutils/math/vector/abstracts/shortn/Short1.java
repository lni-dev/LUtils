package de.linusdev.lutils.math.vector.abstracts.shortn;

import de.linusdev.lutils.math.vector.abstracts.vectorn.Vector1;

public interface Short1 extends ShortN, Vector1 {

    default float get() {
        return get(0);
    }

    default void set(short f) {
        put(0, f);
    }
}
