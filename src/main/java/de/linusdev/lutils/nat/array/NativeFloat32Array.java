package de.linusdev.lutils.nat.array;

import de.linusdev.lutils.nat.NativeType;
import de.linusdev.lutils.nat.struct.annos.StructValue;
import de.linusdev.lutils.nat.struct.annos.SVWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

public class NativeFloat32Array extends NativePrimitiveTypeArray<Float> {

    @SuppressWarnings("unused")
    public static PrimitiveArrayStaticGenerator GENERATOR = new PrimitiveArrayStaticGenerator(NativeType.FLOAT32);

    /**
     * Creates an allocated array.
     * @param structValue {@link StructValue} with {@link StructValue#length()} set. See {@link SVWrapper}.
     */
    public NativeFloat32Array(@NotNull StructValue structValue) {
        super(structValue, GENERATOR);
    }

    /**
     * Creates an unallocated array.
     */
    public NativeFloat32Array() {
    }

    @Override
    public Float get(@Range(from = 0, to = Integer.MAX_VALUE) int index) {
        return getFloat(index);
    }

    public float getFloat(@Range(from = 0, to = Integer.MAX_VALUE) int index) {
        return byteBuf.getFloat(positions.position(index));
    }

    @Override
    public void set(int index, Float item) {
        setFloat(index, item);
    }

    public void setFloat(int index, float item) {
        byteBuf.putFloat(positions.position(index), item);
    }

}
