package de.linusdev.lutils.nat.pointer;

import de.linusdev.lutils.nat.NativeParsable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Pointer64 {

    long NULL_POINTER = 0L;

    static @NotNull Pointer64 of(long pointer) {
        return new Pointer64Impl(pointer);
    }

    /**
     * Create a new {@link Pointer64}, whose {@link #get()} method will return the value of {@link NativeParsable#getPointer()}
     * or {@link #NULL_POINTER} if {@code obj} is {@code null}.
     */
    static @NotNull Pointer64 of(@Nullable NativeParsable obj) {
        return new Pointer64Impl(obj == null ? NULL_POINTER : obj.getPointer());
    }

    long get();

    void set(long pointer);

    /**
     *
     * @return {@code true} if this pointer points to null (0), {@code false} otherwise
     */
    default boolean isNullPtr() {
        return get() == NULL_POINTER;
    }

}
