package de.linusdev.lutils.nat.pointer;

import de.linusdev.lutils.nat.NativeParsable;
import de.linusdev.lutils.nat.struct.abstracts.Structure;
import de.linusdev.lutils.nat.struct.array.StructureArray;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Typed pointer, that points to a {@link NativeParsable}. Similar to {@code T*} in cpp.
 * @param <T> type of pointer
 */
public interface TypedPointer64<T extends NativeParsable> extends Pointer64 {

    static <T extends NativeParsable> @NotNull TypedPointer64<T> of(long pointer) {
        return new TypedPointer64Impl<>(pointer);
    }

    /**
     * Create a new {@link TypedPointer64}, whose {@link #get()} method will return the value of {@link NativeParsable#getPointer()}
     * or {@link #NULL_POINTER} if {@code obj} is {@code null}.
     */
    static <T extends NativeParsable> @NotNull TypedPointer64<T> of(@Nullable T obj) {
        return new TypedPointer64Impl<>(obj == null ? NULL_POINTER : obj.getPointer());
    }

    /**
     * Create a new {@link TypedPointer64}, whose {@link #get()} method will return the value of {@link NativeParsable#getPointer()}
     * or {@link #NULL_POINTER} if {@code obj} is {@code null}. Does not provide type safety.
     */
    static <T extends NativeParsable> @NotNull TypedPointer64<T> ofOther(@Nullable NativeParsable obj) {
        return new TypedPointer64Impl<>(obj == null ? NULL_POINTER : obj.getPointer());
    }

    /**
     * Create a new {@link TypedPointer64}, whose {@link #get()} method will return the value of {@link StructureArray#getPointer()}.
     * Thus, a {@link TypedPointer64} pointing to the first array element is returned. Which corresponds to a c-style array.
     */
    static <T extends Structure> @NotNull TypedPointer64<T> ofArray(@NotNull StructureArray<T> array) {
        return new TypedPointer64Impl<>(array.getPointer());
    }

    /**
     * Same as {@link #of(T) of(T)}
     */
    static <T extends NativeParsable> @NotNull TypedPointer64<T> ref(@Nullable T obj) {
        return of(obj);
    }

    /**
     * Sets this pointers value, so that the {@link #get()} method will return the value of {@link NativeParsable#getPointer()}
     * or {@link #NULL_POINTER} if {@code obj} is {@code null}.
     */
    default void set(@Nullable T obj) {
        set(obj == null ? NULL_POINTER : obj.getPointer());
    }

    /**
     * Cast {@link T} to {@link U}.
     * @return a new {@link TypedPointer64}, whose type is {@link U}.
     * @param <U> new pointer type
     */
    default <U extends NativeParsable> @NotNull TypedPointer64<U> cast() {
        return of(get());
    }
}
