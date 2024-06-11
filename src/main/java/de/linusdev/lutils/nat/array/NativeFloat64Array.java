package de.linusdev.lutils.nat.array;

import de.linusdev.lutils.nat.NativeType;
import de.linusdev.lutils.nat.struct.abstracts.StructureStaticVariables;
import de.linusdev.lutils.nat.struct.annos.StructValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

public class NativeFloat64Array extends NativePrimitiveTypeArray<Double> {

    @SuppressWarnings("unused")
    public static PrimitiveArrayStaticGenerator GENERATOR = new PrimitiveArrayStaticGenerator(NativeType.FLOAT64);

    /**
     * @see StructureStaticVariables#newUnallocated()
     */
    public static NativeFloat64Array newUnallocated() {
        return new NativeFloat64Array(null, false);
    }

    /**
     * @see StructureStaticVariables#newAllocatable(StructValue)
     */
    public static NativeFloat64Array newAllocatable(@NotNull StructValue structValue) {
        return new NativeFloat64Array(structValue, true);
    }

    /**
     * @see StructureStaticVariables#newAllocated(StructValue)
     */
    public static NativeFloat64Array newAllocated(@NotNull StructValue structValue) {
        NativeFloat64Array ret = newAllocatable(structValue);
        ret.allocate();
        return ret;
    }

    protected NativeFloat64Array(
            @Nullable StructValue structValue,
            boolean generateInfo
    ) {
        super(structValue, generateInfo, GENERATOR);
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
