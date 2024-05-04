package de.linusdev.lutils.math.vector.abstracts.byten;

import de.linusdev.lutils.math.general.ByteElements;
import de.linusdev.lutils.math.vector.Vector;
import org.jetbrains.annotations.NotNull;

public interface ByteN extends Vector, ByteElements {

    /**
     * Get component at position {@code index}.
     * @param index index of the vector component to get
     * @return component value
     * @implNote No Error checking will be done by this method. Too large or small indices
     * may result in undefined behavior.
     */
    byte get(int index);

    /**
     *  Set component at position {@code index} to {@code value}.
     * @param index index of the vector component to set
     * @param value value to set
     *  @implNote No Error checking will be done by this method. Too large or small indices
     *  may result in undefined behavior.
     */
    void put(int index, byte value);

    @Override
    default @NotNull ByteN.View getAsView() {
        throw new UnsupportedOperationException("This vector is not a view on another vector.");
    }

    @SuppressWarnings("UnusedReturnValue")
    default @NotNull ByteN fillFromArray(byte @NotNull [] data) {
        for(int x = 0; x < getMemberCount(); x++) {
            put(x, (data[x]));
        }

        return this;
    }

    abstract class View extends Vector.View<ByteN> implements ByteN {

        protected View(@NotNull ByteN original, int @NotNull [] mapping) {
            super(original, mapping);
        }

        @Override
        public byte get(int index) {
            return original.get(mapping[index]);
        }

        @Override
        public void put(int index, byte value) {
            original.put(mapping[index], value);
        }

        @Override
        public boolean hasFactor() {
            return false;
        }

        @Override
        public byte @NotNull [] getFactor() {
            throw new UnsupportedOperationException("This view has no factor");
        }

        @Override
        public @NotNull ByteN.View getAsView() {
            return this;
        }

        @Override
        public String toString() {
            return Vector.toString(this, ELEMENT_TYPE_NAME, ByteN::get);
        }
    }

}
