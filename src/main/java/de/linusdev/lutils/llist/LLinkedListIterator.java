/*
 * Copyright (c) 2022-2023 Linus Andera all rights reserved
 */

package de.linusdev.lutils.llist;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * It is not recommended to use any index based methods. If the list is modified during iteration,
 * the indices may be wrong.
 *
 * @param <O> element class
 */
@SuppressWarnings("unused")
public class LLinkedListIterator<O> implements ListIterator<O>, Iterator<O> {

    private final @NotNull LLinkedList<O> list;

    private int index;
    private @Nullable LLinkedListEntry<O> lastEntry;
    private @NotNull LLinkedListEntry<O> currentEntry;

    public LLinkedListIterator(@NotNull LLinkedList<O> list) {
        this.list = list;
        this.index = -1;
        this.currentEntry = list.getHead();
        this.lastEntry = null;
    }

    public LLinkedListIterator(@NotNull LLinkedList<O> list, int index, @Nullable LLinkedListEntry<O> lastEntry, @NotNull LLinkedListEntry<O> currentEntry) {
        this.list = list;
        this.index = index;
        this.currentEntry = currentEntry;
        this.lastEntry = lastEntry;
    }

    @Override
    public synchronized boolean hasNext() {
        return currentEntry.getNext() != null;
    }

    @Override
    public synchronized O next() {
        if(currentEntry.getNext() == null)
            throw new NoSuchElementException();
        lastEntry = currentEntry;
        currentEntry = currentEntry.getNext();
        index++;
        return currentEntry.getValue();
    }

    @Override
    public boolean hasPrevious() {
        return false;
    }

    /**
     * reversed traversal is not supported by this iterator.
     * @throws UnsupportedOperationException always.
     */
    @Override
    public O previous() {
        throw new UnsupportedOperationException("This list cannot be traversed in reverse.");
    }

    /**
     * It is not recommended to use any index based methods. If the list is modified during iteration,
     * the indices may be wrong.
     */
    @Override
    public int nextIndex() {
        return index + 1;
    }

    /**
     * It is not recommended to use any index based methods. If the list is modified during iteration,
     * the indices may be wrong.
     */
    @Override
    public int previousIndex() {
        return index - 1;
    }

    @Override
    public void remove() {
        if(lastEntry == null)
            throw new IllegalStateException("element cant be removed. next must be called");

        list.removeEntry(currentEntry);
        lastEntry = null;
        index--;
    }

    @Override
    public void set(O o) {
        if(currentEntry == list.getHead()) //The head may not contain an element.
            throw new IllegalStateException("Cannot set an element to the list head.");
        currentEntry.setValue(o);
    }

    @Override
    public void add(O o) {
        list.add(currentEntry, new LLinkedListEntry<>(o));
    }

    @NotNull LLinkedListEntry<O> getCurrentEntry() {
        return currentEntry;
    }

    @Nullable LLinkedListEntry<O> getLastEntry() {
        return lastEntry;
    }
}
