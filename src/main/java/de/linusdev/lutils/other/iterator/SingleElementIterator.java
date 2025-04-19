package de.linusdev.lutils.other.iterator;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class SingleElementIterator<E> implements Iterator<E> {

    private final E element;
    private boolean hasNext = true;

    public SingleElementIterator(E element) {
        this.element = element;
    }

    @Override
    public boolean hasNext() {
        return hasNext;
    }

    @Override
    public E next() {
        if(!hasNext)
            throw new NoSuchElementException();
        hasNext = false;
        return element;
    }
}
