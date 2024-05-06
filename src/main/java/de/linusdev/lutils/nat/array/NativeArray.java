package de.linusdev.lutils.nat.array;

import de.linusdev.lutils.nat.NativeParsable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.Iterator;

public interface NativeArray<T> extends Iterable<T>, NativeParsable {

    /**
     * Get {@link T} with at {@code index}
     * @param index index to get from. Must be greater than 0 and smaller then {@link #length()}.
     * @return {@link T} at {@code index}
     */
    T get(@Range(from = 0, to = Integer.MAX_VALUE) int index);

    /**
     * Sets given {@link T} at index.
     * @param index index to set.  Must be greater than 0 and smaller then {@link #length()}.
     * @param item to set at index
     */
    void set(int index, T item);

    /**
     * @return length of this array.
     */
    int length();

    @NotNull
    @Override
    default Iterator<T> iterator() {
        return new Iterator<>() {
            int index = 0;

            @Override
            public boolean hasNext() {
                return index < length();
            }

            @Override
            public T next() {
                return get(index++);
            }
        };
    }
}
