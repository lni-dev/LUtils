package de.linusdev.lutils.nat.array;

import de.linusdev.lutils.nat.NativeType;
import de.linusdev.lutils.nat.struct.annos.StructValue;
import de.linusdev.lutils.nat.struct.annos.SVWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

public class NativeInt32Array extends NativePrimitiveTypeArray<Integer> {

    @SuppressWarnings("unused")
    public static PrimitiveArrayStaticGenerator GENERATOR = new PrimitiveArrayStaticGenerator(NativeType.INT32);

    /**
     * Creates an allocated array.
     * @param structValue {@link StructValue} with {@link StructValue#length()} set. See {@link SVWrapper}.
     */
    public NativeInt32Array(@NotNull StructValue structValue) {
        super(structValue, GENERATOR);
    }

    /**
     * Creates an unallocated array.
     */
    public NativeInt32Array() {
    }

    @Override
    public Integer get(@Range(from = 0, to = Integer.MAX_VALUE) int index) {
        return getInt(index);
    }

    public int getInt(@Range(from = 0, to = Integer.MAX_VALUE) int index) {
        return byteBuf.getInt(positions.position(index));
    }

    @Override
    public void set(int index, Integer item) {
        setInt(index, item);
    }

    public void setInt(int index, int item) {
        byteBuf.putInt(positions.position(index), item);
    }

}
