package de.linusdev.lutils.nat.pointer;

import de.linusdev.lutils.nat.NativeParsable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface TypedPointer64<T extends NativeParsable> extends Pointer64 {

    static <T extends NativeParsable> @NotNull TypedPointer64<T> of(long pointer) {
        return new TypedPointer64Impl<>(pointer);
    }

    /**
     * Create a new {@link TypedPointer64}, whose {@link #get()} method will return the value of {@link NativeParsable#getPointer()}
     * or {@link #NULL_POINTER} if {@code obj} is {@code null}.
     */
    static <T extends NativeParsable> @NotNull TypedPointer64<T> of(@Nullable NativeParsable obj) {
        return new TypedPointer64Impl<>(obj == null ? NULL_POINTER : obj.getPointer());
    }

    /**
     * Sets this pointers value, so that the {@link #get()} method will return the value of {@link NativeParsable#getPointer()}
     * or {@link #NULL_POINTER} if {@code obj} is {@code null}.
     */
    default void set(@Nullable T obj) {
        set(obj == null ? NULL_POINTER : obj.getPointer());
    }
}
