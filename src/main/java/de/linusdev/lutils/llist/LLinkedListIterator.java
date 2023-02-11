/*
 * Copyright (c) 2022-2023 Linus Andera all rights reserved
 */

package de.linusdev.lutils.llist;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class LLinkedListIterator<O> implements Iterator<O> {

    private final @NotNull LLinkedList<O> list;

    private @Nullable LLinkedListEntry<O> lastEntry;
    private @NotNull LLinkedListEntry<O> currentEntry;

    public LLinkedListIterator(@NotNull LLinkedList<O> list) {
        this.list = list;
        this.currentEntry = list.getHead();
        this.lastEntry = null;
    }

    @Override
    public boolean hasNext() {
        return currentEntry.getNext() != null;
    }

    @Override
    public O next() {
        if(currentEntry.getNext() == null)
            throw new NoSuchElementException();
        lastEntry = currentEntry;
        currentEntry = currentEntry.getNext();
        return currentEntry.getValue();
    }

    @Override
    public void remove() {
        if(lastEntry == null)
            throw new IllegalStateException("element cant be removed. next must be called");

        list.remove(lastEntry, currentEntry);
        lastEntry = null;
    }

    @NotNull LLinkedListEntry<O> getCurrentEntry() {
        return currentEntry;
    }

    @Nullable LLinkedListEntry<O> getLastEntry() {
        return lastEntry;
    }
}
