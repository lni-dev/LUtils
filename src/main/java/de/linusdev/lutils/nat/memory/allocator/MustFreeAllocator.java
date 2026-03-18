/*
 * Copyright (c) 2026 Linus Andera
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

package de.linusdev.lutils.nat.memory.allocator;

import de.linusdev.lutils.id.Identifier;
import de.linusdev.lutils.nat.memory.AllocatedMemory;
import de.linusdev.lutils.nat.memory.NativeMemAllocator;
import de.linusdev.lutils.nat.memory.buffer.UnsafeNativeMemBuffer;
import de.linusdev.lutils.other.log.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.Cleaner;
import java.nio.ByteOrder;

public abstract class MustFreeAllocator implements NativeMemAllocator {

    private static final @NotNull Logger LOG = Logger.getLogger();
    private static @Nullable Cleaner CLEANER = null;

    static {
        boolean doCleanup = false;
        //noinspection ConstantValue,AssertWithSideEffects:
        assert !(doCleanup = true); // automatically enable cleaner if assertions are enabled.

        if(doCleanup) {
            CLEANER = Cleaner.create();
        }
    }

    protected MustFreeAllocator() {

    }

    public static void enableCleaner() {
        CLEANER = Cleaner.create();
    }

    @Override
    public @NotNull AllocatedMemory allocate(long size) {
        long address = allocateInternal(size);
        return new Memory(address, size, ByteOrder.nativeOrder(), null);
    }

    @Override
    public @NotNull AllocatedMemory allocate(long size, @NotNull Identifier debugId) {
        long address = allocateInternal(size);
        return new Memory(address, size, ByteOrder.nativeOrder(), debugId);
    }

    protected abstract long allocateInternal(long size);

    protected abstract void freeInternal(long address);

    class Memory extends UnsafeNativeMemBuffer implements AllocatedMemory {

        private final Cleaner.Cleanable cleanable;
        private final State cleaningState;
        private boolean closed = false;

        public Memory(long address, long size, @NotNull ByteOrder byteOrder, @Nullable Identifier debugId) {
            super(address, size, byteOrder);

            if(CLEANER != null) {
                this.cleaningState = new State(address, debugId);
                this.cleanable = CLEANER.register(this, cleaningState);
            } else {
                this.cleaningState = null;
                this.cleanable = null;
            }
        }

        @Override
        public synchronized void close() throws Exception {
            if(closed)
                return;
            freeInternal(address());
            closed = true;
            if(cleaningState != null) {
                cleaningState.freed = true;
                cleanable.clean();
            }
        }

        static class State implements Runnable {

            private final long address;
            private final @Nullable Identifier debugId;
            volatile boolean freed = false;

            State(long address, @Nullable Identifier debugId) {
                this.address = address;
                this.debugId = debugId;
            }

            @Override
            public void run() {
                if(freed)
                    return;
                LOG.error("The memory with the address '" + address + "' and the id '"
                        + (debugId == null ? "null" : debugId.getIdentifierAsString())
                        + "' was never closed, but its representing object is about to be garbage collected.");
            }
        }
    }
}
