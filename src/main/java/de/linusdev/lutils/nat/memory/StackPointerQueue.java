package de.linusdev.lutils.nat.memory;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayDeque;
import java.util.Arrays;

public class StackPointerQueue {

    public final int DEFAULT_CAP = 100;
    public final int DEFAULT_GROW_SIZE = 100;

    /**
     * Array this queue is backed by
     */
    protected long[] pointers = new long[DEFAULT_CAP];
    protected @NotNull ArrayDeque<Integer> safePoints = new ArrayDeque<>();

    /**
     * index where the next pointer will be added
     */
    protected int index = 0;

    public void push(long pointer) {
        growIfRequired();
        pointers[index++] = pointer;
    }

    /**
     * Calls {@link #grow(int)} if {@link #index} is the same as the length of {@link #pointers}.
     */
    protected void growIfRequired() {
        if(index >= pointers.length)
            grow(pointers.length + DEFAULT_GROW_SIZE);
    }

    protected void grow(int newCap) {
        pointers = Arrays.copyOf(pointers, newCap);
    }

    public long pop() {
        if(index <= 0) {
            throw new IllegalStateException("Cannot pop as no items are in this stack pointer queue");
        }

        return pointers[--index];
    }

    public void createSafePoint() {
        safePoints.addLast(index);
    }

    /**
     * Checks and removes the last check point
     * @return {@code true} if the last safe point has been reached.
     */
    public boolean checkSafePoint() {
        Integer safePoint = safePoints.peekLast();
        if(safePoint == null)
            throw new IllegalStateException("No more safe-points store.");
        if(index == safePoint) {
            safePoints.pollLast();
            return true;
        }
        return false;
    }

    public int size() {
        return index;
    }

}
