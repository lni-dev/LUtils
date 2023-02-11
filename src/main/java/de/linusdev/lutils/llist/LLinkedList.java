/*
 * Copyright (c) 2022-2023 Linus Andera all rights reserved
 */

package de.linusdev.lutils.llist;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;

public class LLinkedList<O> implements List<O> {

    final @NotNull Object lock = new Object();

    volatile int size;
    private final @NotNull LLinkedListEntry<O> head = new LLinkedListEntry<>(null);
    private @NotNull LLinkedListEntry<O> tail = head;


    public LLinkedList(){

    }

    /**
     * Executes given consumer synchronized on this list's lock
     * @param function {@link Function}
     */
    public <R> R doSynchronized(@NotNull Function<LLinkedList<O>, R> function) {
        synchronized (lock) {
            return function.apply(this);
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return head.getNext() == null;
    }

    @Override
    public boolean contains(Object o) {
        for(O value : this) {
            if(Objects.equals(value, o)) return true;
        }
        return false;
    }

    @NotNull
    @Override
    public LLinkedListIterator<O> iterator() {
        return new LLinkedListIterator<>(this);
    }

    @NotNull
    @Override
    public Object @NotNull [] toArray() {
        Object[] array = new Object[size];
        int i = 0;
        for(O value : this) {
            array[i++] = value;
        }
        return array;
    }

    @NotNull
    @Override
    public <T> T @NotNull [] toArray(@NotNull T @NotNull [] a) {
        return (T[]) Arrays.copyOf(toArray(), size, a.getClass());
    }

    @Override
    public boolean add(O o) {
        synchronized (lock) {
            LLinkedListEntry<O> entry = new LLinkedListEntry<>(o);

            tail.setNext(entry);
            tail = entry;

            size++;
            return true;
        }
    }

    @Override
    public boolean remove(Object o) {
        synchronized (lock) {
            Iterator<O> iterator = iterator();

            while (iterator().hasNext()){
                if(Objects.equals(iterator.next(), o)){
                    iterator.remove();
                    return true;
                }
            }

            return false;
        }

    }

    /**
     * removes removeEntry from this list
     * @param beforeEntry the entry before the entry to remove
     * @param removeEntry entry to remove
     */
    void remove(@NotNull LLinkedListEntry<O> beforeEntry, @NotNull LLinkedListEntry<O> removeEntry) {
        synchronized (lock) {
            if(removeEntry == tail) {
                //we are removing the last entry, so we need to change the tail
                tail = beforeEntry;
                beforeEntry.setNext(null);

            } else {
                beforeEntry.setNext(removeEntry.getNext());

            }
            size--;
        }
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        for(Object o : c ) {
            if(!contains(o)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends O> c) {
        synchronized (lock) {
            for(O o : c) add(o);
        }
        return true;
    }

    @Override
    public boolean addAll(int index, @NotNull Collection<? extends O> c) {
        synchronized (lock) {
            if(index == size) {
                return addAll(c);
            } else if(c.isEmpty()) {
                return false;
            }

            LLinkedListEntry<O> before = getEntry(index - 1);
            LLinkedListEntry<O> toMove = before.getNext();
            LLinkedListEntry<O> lastAdded = null;

            for(O o : c) {
                lastAdded = new LLinkedListEntry<>(o);
                before.setNext(lastAdded);
                before = lastAdded;
                size++;
            }

            //noinspection DataFlowIssue: Will never null, because c will not be empty
            lastAdded.setNext(toMove);
            return true;
        }
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        //TODO: implement
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        //TODO: implement
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void clear() {
        head.setNext(null);
    }

    @Override
    public O get(int index) {
        int i = 0;
        for(O value : this){
            if(i++ == index) return value;
        }
        throw new IndexOutOfBoundsException(index);
    }

    private @NotNull LLinkedListEntry<O> getEntry(int index) {

        if(index >= size)
            throw new IndexOutOfBoundsException(index);

        LLinkedListIterator<O> it = iterator();
        for(int i = 0; i <= index; i++) it.next();
        return it.getCurrentEntry();
    }

    @Override
    public O set(int index, @Nullable O element) {
        synchronized (lock) {
            LLinkedListEntry<O> entry = getEntry(index);
            O old = entry.getValue();
            entry.setValue(element);
            return old;
        }
    }

    @Override
    public void add(int index, O element) {
        synchronized (lock) {
            if(index == size) {
                add(element);
                return;
            }

            LLinkedListEntry<O> before = getEntry(index - 1);
            LLinkedListEntry<O> toMove = before.getNext();
            LLinkedListEntry<O> toAdd = new LLinkedListEntry<>(element);

            before.setNext(toAdd);
            toAdd.setNext(toMove);

            size++;
        }
    }

    @Override
    public O remove(int index) {
        synchronized (lock) {
            if(index >= size)
                throw new IndexOutOfBoundsException(index);

            LLinkedListEntry<O> before = getEntry(index-1);
            //noinspection DataFlowIssue: will never be null, because index < size.
            O removed = before.getNext().getValue();
            remove(before, before.getNext());
            return removed;
        }
    }

    @Override
    public int indexOf(Object o) {
        //TODO: implement
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public int lastIndexOf(Object o) {
        //TODO: implement
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * This List cannot be traversed in reverse.
     * @throws UnsupportedOperationException always
     */
    @NotNull
    @Override
    public ListIterator<O> listIterator() {
        throw new UnsupportedOperationException("Not implemented: This List cannot be traversed in reverse.");
    }

    /**
     * This List cannot be traversed in reverse.
     * @throws UnsupportedOperationException always
     */
    @NotNull
    @Override
    public ListIterator<O> listIterator(int index) {
        throw new UnsupportedOperationException("Not implemented: This List cannot be traversed in reverse.");
    }

    @NotNull
    @Override
    public List<O> subList(int fromIndex, int toIndex) {
        //TODO: implement
        throw new UnsupportedOperationException("Not implemented");
    }

    @NotNull LLinkedListEntry<O> getHead() {
        return head;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");

        boolean first = true;
        for(O value : this) {
            if(!first){
                sb.append(", ");
            } else first = false;
            sb.append(value);
        }

        sb.append("]");

        return sb.toString();
    }
}
