package de.linusdev.lutils.struct.array;

import de.linusdev.lutils.struct.abstracts.NativeParsable;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public interface NativeArray<T> extends Iterable<T>, NativeParsable {

    T get(int index);

    void set(int index, T t);

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
