package de.linusdev.lutils.other.iterator;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

@SuppressWarnings("unused")
public class SingleElementIterator<E> implements Iterator<E> {

    private final @NotNull E element;
    private boolean hasNext = true;

    public SingleElementIterator(@NotNull E element) {
        this.element = element;
    }

    @Override
    public boolean hasNext() {
        return hasNext;
    }

    @Override
    public E next() {
        hasNext = false;
        return element;
    }
}
