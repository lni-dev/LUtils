package de.linusdev.lutils.math.vector.abstracts.byten;

import de.linusdev.lutils.math.vector.abstracts.vectorn.Vector1;

public interface Byte1 extends ByteN, Vector1 {

    default float get() {
        return get(0);
    }

    default void set(byte f) {
        put(0, f);
    }
}
