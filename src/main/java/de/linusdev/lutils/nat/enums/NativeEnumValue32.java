package de.linusdev.lutils.nat.enums;

import de.linusdev.lutils.math.vector.buffer.intn.BBInt1;
import de.linusdev.lutils.nat.struct.abstracts.StructureStaticVariables;
import de.linusdev.lutils.nat.struct.annos.StructValue;
import de.linusdev.lutils.other.UnknownConstantException;
import org.jetbrains.annotations.Blocking;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Type corresponding to a 32 bit native enum type.
 * @param <M> The enum this value must be of.
 * @see NativeEnumMember32
 */
@SuppressWarnings("unused")
public class NativeEnumValue32<M extends NativeEnumMember32> extends BBInt1 {

    /**
     * @see StructureStaticVariables#newUnallocated()
     */
    public static <T extends NativeEnumMember32> NativeEnumValue32<T> newUnallocatedT()  {
        return new NativeEnumValue32<>(false, null);
    }

    /**
     * @see StructureStaticVariables#newAllocatable(StructValue) 
     */
    public static <T extends NativeEnumMember32> NativeEnumValue32<T> newAllocatableT(
            @Nullable StructValue structValue
    )  {
        return new NativeEnumValue32<>(true, structValue);
    }

    /**
     * @see StructureStaticVariables#newAllocated(StructValue) 
     */
    public static <T extends NativeEnumMember32> NativeEnumValue32<T> newAllocatedT(
            @Nullable StructValue structValue
    )  {
        NativeEnumValue32<T> ret = newAllocatableT(structValue);
        ret.allocate();
        return ret;
    }

    protected NativeEnumValue32(
            boolean generateInfo,
            @Nullable StructValue structValue
    ) {
        super(generateInfo, structValue);
    }

    public void set(@Nullable M value) {
        if(value == null) set(0);
        else set(value.getValue());
    }

    public void set(@NotNull NativeEnumValue32<M> value) {
        set(value.get());
    }

    /**
     * Iterates through all enum constants of given {@code enumClass} and returns
     * the one, whose {@link NativeEnumMember32#getValue()} corresponds to the {@link #get() value} of this enum value
     * @param enumClass The class of the enum {@link M}.
     * @return {@link M} as described above.
     * @throws UnknownConstantException if no enum constant of given {@code enumClass} matches the {@link #get() value} of this enum value.
     */
    @Blocking
    public @NotNull M get(@NotNull Class<M> enumClass) {
        int val = get();
        for (M c : enumClass.getEnumConstants()) {
            if(c.getValue() == val)
                return c;
        }

        throw new UnknownConstantException(val);
    }

}
