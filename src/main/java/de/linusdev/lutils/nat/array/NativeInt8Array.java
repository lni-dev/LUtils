package de.linusdev.lutils.nat.array;

import de.linusdev.lutils.nat.NativeType;
import de.linusdev.lutils.nat.struct.annos.StructValue;
import de.linusdev.lutils.nat.struct.annos.SVWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

public class NativeInt8Array extends NativePrimitiveTypeArray<Byte> {

    @SuppressWarnings("unused")
    public static PrimitiveArrayStaticGenerator GENERATOR = new PrimitiveArrayStaticGenerator(NativeType.INT8);

    /**
     * Creates an allocated array.
     * @param structValue {@link StructValue} with {@link StructValue#length()} set. See {@link SVWrapper}.
     */
    public NativeInt8Array(@NotNull StructValue structValue) {
        super(structValue, GENERATOR);
    }

    /**
     * Creates an unallocated array.
     */
    public NativeInt8Array() {
    }

    @Override
    public Byte get(@Range(from = 0, to = Integer.MAX_VALUE) int index) {
        return getInt8(index);
    }

    public byte getInt8(@Range(from = 0, to = Integer.MAX_VALUE) int index) {
        return byteBuf.get(positions.position(index));
    }

    @Override
    public void set(int index, Byte item) {
        setInt8(index, item);
    }

    public void setInt8(int index, byte item) {
        byteBuf.put(positions.position(index), item);
    }

}
