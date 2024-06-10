package de.linusdev.lutils.nat.array;

import de.linusdev.lutils.nat.NativeType;
import de.linusdev.lutils.nat.struct.annos.StructValue;
import de.linusdev.lutils.nat.struct.annos.SVWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

public class NativeFloat64Array extends NativePrimitiveTypeArray<Double> {

    @SuppressWarnings("unused")
    public static PrimitiveArrayStaticGenerator GENERATOR = new PrimitiveArrayStaticGenerator(NativeType.FLOAT64);

    /**
     * Creates an allocated array.
     * @param structValue {@link StructValue} with {@link StructValue#length()} set. See {@link SVWrapper}.
     */
    public NativeFloat64Array(@NotNull StructValue structValue) {
        super(structValue, GENERATOR);
    }

    /**
     * Creates an unallocated array.
     */
    public NativeFloat64Array() {
    }

    @Override
    public Double get(@Range(from = 0, to = Integer.MAX_VALUE) int index) {
        return getFloat64(index);
    }

    public double getFloat64(@Range(from = 0, to = Integer.MAX_VALUE) int index) {
        return byteBuf.getDouble(positions.position(index));
    }

    @Override
    public void set(int index, Double item) {
        setFloat64(index, item);
    }

    public void setFloat64(int index, double item) {
        byteBuf.putDouble(positions.position(index), item);
    }

}
