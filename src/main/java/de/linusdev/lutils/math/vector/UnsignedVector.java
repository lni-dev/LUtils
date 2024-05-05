package de.linusdev.lutils.math.vector;

public interface UnsignedVector extends Vector {

    @Override
    default boolean areComponentsUnsigned() {
        return true;
    }
}
