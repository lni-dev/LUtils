/*
 * Copyright (c) 2024-2025 Linus Andera
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

package de.linusdev.lutils.nat.memory.stack.impl;

import de.linusdev.lutils.nat.memory.stack.SafePointError;
import de.linusdev.lutils.nat.memory.stack.Stack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class StackPointerQueue {

    public final int DEFAULT_CAP = 100;
    public final int DEFAULT_GROW_SIZE = 100;

    public final int DEFAULT_SAFE_POINT_CAP = 100;
    public final int DEFAULT_SAFE_POINT_CAP_GROW_SIZE = 100;

    /**
     * Current stack pointer.
     */
    protected long stackPointer;

    /**
     * Array this queue is backed by. Index {@code i} contains the
     * stack pointer as if the {@code (i-1)}th element was the last element pushed onto this stack and at least {@code (i+1)}
     * element are currently on the stack.
     * This means {@code pointers[0]} will contain the pointer of the stack itself if at least one element is on
     * the stack.<br>
     * With the above rules, the following holds:
     * <ul>
     *     <li>Element {@code (-1)} is the stack itself</li>
     *     <li>The first actual element is element {@code 0}</li>
     *     <li>{@link #index} is the next element, that will be added.</li>
     *     <li>{@code index-1} is the last element, that was added.</li>
     *     <li>The stack contains exactly {@code index} elements.</li>
     *     <li>Setting the stack pointer to {@code pointers[index-1]} will {@link #pop() pop} the last element.</li>
     *     <li>Setting the stack pointer to {@code pointers[i]} will {@link #pop() pop} {@code (index - i)} elements.</li>
     * </ul>
     */
    protected long[] pointers = new long[DEFAULT_CAP];
    /**
     * index where the next pointer will be added
     */
    protected int index = 0;

    /**
     * Array of {@link SPQSafePoint}s.
     */
    protected SPQSafePoint[] safePoints = new SPQSafePoint[DEFAULT_SAFE_POINT_CAP];
    /**
     * Index where the next safe point will be added.
     */
    protected int safePointsIndex = 0;

    public StackPointerQueue(long address) {
        this.stackPointer = address;
    }

    public long getStackPointer() {
        return stackPointer;
    }

    public void push(long size) {
        growPointersIfRequired();
        pointers[index++] = stackPointer;
        stackPointer += size;
    }

    public void pop() {
        assert popChecks();
        stackPointer = pointers[--index];
    }

    public @NotNull SPQSafePoint safePoint(@NotNull Stack stack) {
        growSafePointsIfRequired();
        //noinspection resource: Closed by method caller.
        safePoints[safePointsIndex] = new SPQSafePoint(index, this, stack);

        return safePoints[safePointsIndex++];
    }

    public @NotNull SPQPopPoint popPoint(@NotNull Stack stack) {
        growSafePointsIfRequired();
        SPQPopPoint point = new SPQPopPoint(index, this, stack);
        safePoints[safePointsIndex] = point;
        return point;
    }

    protected void checkSafePoint(@NotNull SPQSafePoint safePoint) {
        if(this.index == safePoint.index) {
            safePointsIndex--;
            return;
        }

        if(this.index > safePoint.index) {
            throw new SafePointError("SafePoint has not been reached. " + (this.index - safePoint.index)
                    + " too many items are still remaining on the stack and must be popped.");
        } else {
            throw new SafePointError("SafePoint has not been reached. " + (safePoint.index - this.index)
                    + " too many items have been popped from the stack!");
        }
    }

    protected void popToPopPoint(@NotNull SPQPopPoint popPoint) {
        if(this.index == popPoint.index) {
            // pop point already reached
            safePointsIndex--;
            return;
        }

        if(this.index < popPoint.index) {
            throw new SafePointError("SafePoint has not been reached. " + (popPoint.index - this.index)
                    + " too many items have been popped from the stack!");
        }

        stackPointer = pointers[popPoint.index];
        index = popPoint.index;
    }

    public int size() {
        return index;
    }



    /**
     * Calls {@link #growPointers(int)} if {@link #index} is the same as the length of {@link #pointers}.
     */
    protected void growPointersIfRequired() {
        if(index >= pointers.length)
            growPointers(pointers.length + DEFAULT_GROW_SIZE);
    }

    protected void growPointers(int newCap) {
        pointers = Arrays.copyOf(pointers, newCap);
    }

    /**
     * Calls {@link #growSafePointers(int)} if {@link #safePointsIndex} is the same as the length of {@link #safePoints}.
     */
    protected void growSafePointsIfRequired() {
        if(safePointsIndex >= safePoints.length)
            growSafePointers(safePoints.length + DEFAULT_SAFE_POINT_CAP_GROW_SIZE);
    }

    protected void growSafePointers(int newCap) {
        safePoints = Arrays.copyOf(safePoints, newCap);
    }

    protected boolean popChecks() {
        if(index <= 0)
            throw new IllegalStateException("Cannot pop as no items are in this stack pointer queue");
        if(safePointsIndex > 0 && index == safePoints[safePointsIndex-1].index)
            throw new SafePointError("Popping this item would reduce the stack pointer past the" +
                    " last safe point. checkSafePoint() must be called first.");

        return true;
    }

}
