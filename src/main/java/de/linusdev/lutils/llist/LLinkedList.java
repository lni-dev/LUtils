/*
 * Copyright (c) 2022-2024 Linus Andera
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.linusdev.lutils.llist;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;

/**
 * A thread safe linked list implementation. It can only be traversed in a single direction.
 * Tt can not be traversed in reverse. That is because every node only stores the next element, but not
 * the previous element.<br>
 * <br>
 * This list allows modification during iteration. Whether the modification applies during the iteration
 * depends solely on the order, in which the iteration and modification events happen.<br>
 * <br>
 * For more complex operations, which must be thread safe, this list also provides a {@link #doSynchronized(Function) doSynchronized}
 * method, which executes the given {@link Function} synchronized on this list's lock. Which means, no modifying operations
 * can be started by other threads, while the lock is being hold. Reading operations are still possible by all threads.
 *
 * @param <O> element class
 */
public class LLinkedList<O> implements List<O> {

    final @NotNull Object lock = new Object();

    volatile int size;
    private final @NotNull LLinkedListEntry<O> head = new LLinkedListEntry<>(null);
    private @NotNull LLinkedListEntry<O> tail = head;


    public LLinkedList(){

    }

    /**
     * Executes given consumer synchronized on this list's lock. See {@link LLinkedList} for more information.
     * @param function {@link Function}
     * @see LLinkedList
     */
    @SuppressWarnings("unused")
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
    public <T> T @NotNull [] toArray(T @NotNull [] a) {
        //noinspection unchecked: class will be of correct type.
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
            LLinkedListIterator<O> iterator = iterator();

            while (iterator().hasNext()){
                if(Objects.equals(iterator.next(), o)){
                    //noinspection DataFlowIssue: Will not be null, because next() has been called at least once.
                    remove(iterator.getLastEntry(), iterator.getCurrentEntry());
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
    private void remove(@NotNull LLinkedListEntry<O> beforeEntry, @NotNull LLinkedListEntry<O> removeEntry) {
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

    void removeEntry(@NotNull LLinkedListEntry<O> entry) {
        //TODO: test method
        synchronized (lock) {
            LLinkedListIterator<O> iterator = iterator();

            while (iterator.hasNext()){
                iterator.next();
                if(iterator.getCurrentEntry() == entry){
                    //noinspection DataFlowIssue: Will not be null, because next() has been called at least once.
                    remove(iterator.getLastEntry(), iterator.getCurrentEntry());
                    return;
                }
            }
        }
    }

    /**
     * adds {@code toAddEntry} to this list after the entry {@code beforeEntry}.
     * @param beforeEntry the entry after which the new entry should be added
     * @param toAddEntry entry to add
     */
    void add(@NotNull LLinkedListEntry<O> beforeEntry, @NotNull LLinkedListEntry<O> toAddEntry) {
        synchronized (lock) {
            if(beforeEntry == tail) {
                tail.setNext(toAddEntry);
                tail = toAddEntry;

                size++;
                return;
            }

            LLinkedListEntry<O> toMove = beforeEntry.getNext();
            beforeEntry.setNext(toAddEntry);
            toAddEntry.setNext(toMove);
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

    @NotNull
    @Override
    public ListIterator<O> listIterator() {
        return new LLinkedListIterator<>(this);
    }

    /**
     * This List cannot be traversed in reverse.
     * @throws UnsupportedOperationException always
     */
    @NotNull
    @Override
    public ListIterator<O> listIterator(int index) {

        if(index == 0)
            return listIterator();

        LLinkedListEntry<O> before = getEntry(index - 1);
        LLinkedListEntry<O> next = before.getNext();

        if(next == null)
            throw new IndexOutOfBoundsException(index);

        return new LLinkedListIterator<>(this, index, before, next);
    }

    @NotNull
    @Override
    public List<O> subList(int fromIndex, int toIndex) {
        LLinkedList<O> sub = new LLinkedList<>();

        int index = 0;
        for(O o : this) {
            if(index >= toIndex)
                return sub;
            if(index >= fromIndex)
                sub.add(o);
        }

        return sub;
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
