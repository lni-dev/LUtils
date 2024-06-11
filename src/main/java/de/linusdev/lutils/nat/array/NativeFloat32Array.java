package de.linusdev.lutils.nat.array;

import de.linusdev.lutils.nat.NativeType;
import de.linusdev.lutils.nat.struct.abstracts.StructureStaticVariables;
import de.linusdev.lutils.nat.struct.annos.StructValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

public class NativeFloat32Array extends NativePrimitiveTypeArray<Float> {

    @SuppressWarnings("unused")
    public static PrimitiveArrayStaticGenerator GENERATOR = new PrimitiveArrayStaticGenerator(NativeType.FLOAT32);

    /**
     * @see StructureStaticVariables#newUnallocated()
     */
    public static NativeFloat32Array newUnallocated() {
        return new NativeFloat32Array(null, false);
    }

    /**
     * @see StructureStaticVariables#newAllocatable(StructValue)
     */
    public static NativeFloat32Array newAllocatable(@NotNull StructValue structValue) {
        return new NativeFloat32Array(structValue, true);
    }

    /**
     * @see StructureStaticVariables#newAllocated(StructValue)
     */
    public static NativeFloat32Array newAllocated(@NotNull StructValue structValue) {
        NativeFloat32Array ret = newAllocatable(structValue);
        ret.allocate();
        return ret;
    }

    protected NativeFloat32Array(
            @Nullable StructValue structValue,
            boolean generateInfo
    ) {
        super(structValue, generateInfo, GENERATOR);
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
