/*
 * Copyright (c) 2024 Linus Andera
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

import de.linusdev.lutils.nat.memory.stack.SafePoint;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class StackPointerQueue {

    public static class SPQSafePoint implements SafePoint {

        final int index;
        final @NotNull StackPointerQueue spq;

        public SPQSafePoint(int index, @NotNull StackPointerQueue spq) {
            this.index = index;
            this.spq = spq;
        }

        @Override
        public void close() {

        }
    }

    public final int DEFAULT_CAP = 100;
    public final int DEFAULT_GROW_SIZE = 100;

    public final int DEFAULT_SAFE_POINT_CAP = 100;
    public final int DEFAULT_SAFE_POINT_CAP_GROW_SIZE = 100;

    /**
     * Array this queue is backed by
     */
    protected long[] pointers = new long[DEFAULT_CAP];
    protected SPQSafePoint[] safePoints = new SPQSafePoint[DEFAULT_SAFE_POINT_CAP];

    /**
     * index where the next pointer will be added
     */
    protected int index = 0;
    /**
     * Index where the next safe point will be added.
     */
    protected int safePointsIndex = 0;

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

    public long pop() {
        if(index <= 0) {
            throw new IllegalStateException("Cannot pop as no items are in this stack pointer queue");
        }
        if(index == safePoints[safePointsIndex-1].index) {
            throw new IllegalStateException("");
        }

        return pointers[--index];
    }

    public @NotNull SPQSafePoint safePoint() {
        growSafePointsIfRequired();
        safePoints[safePointsIndex] = new SPQSafePoint(index, this);

        return safePoints[safePointsIndex++];
    }

    protected void checkSafePoint(@NotNull SPQSafePoint safePoint) {

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
            throw new IllegalStateException("No more safe-points stored.");
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
