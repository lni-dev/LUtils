package de.linusdev.lutils.other.iterator;

import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

@SuppressWarnings("unused")
public class IteratorUtils {

    public static <E> @NotNull EmptyIterator<E> emptyIterator() {
        return new EmptyIterator<>();
    }

    public static <E> @NotNull SingleElementIterator<E> singleElementIterator(E element) {
        return new SingleElementIterator<>(element);
    }

    public static <R, E> @NotNull IteratorView<R, E> iteratorView(@NotNull Iterable<R> original, @NotNull Function<R, E> converter) {
        return new IteratorView<>(original, converter);
    }

}
