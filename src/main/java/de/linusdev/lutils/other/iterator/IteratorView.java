package de.linusdev.lutils.other.iterator;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.function.Function;

public class IteratorView<R, E> implements Iterator<E> {

    private final @NotNull Iterator<R> original;
    private final @NotNull Function<R, E> converter;

    public IteratorView(@NotNull Iterable<R> original, @NotNull Function<R, E> converter) {
        this.original = original.iterator();
        this.converter = converter;
    }

    @Override
    public boolean hasNext() {
        return original.hasNext();
    }

    @Override
    public E next() {
        return converter.apply(original.next());
    }
}
