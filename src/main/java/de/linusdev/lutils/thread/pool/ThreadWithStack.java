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

package de.linusdev.lutils.thread.pool;

import de.linusdev.lutils.nat.memory.stack.Stack;
import de.linusdev.lutils.nat.memory.stack.StackFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.ThreadFactory;
import java.util.function.Function;

public class ThreadWithStack implements Runnable {

    private final @NotNull Object lock = new Object();

    private final @NotNull Thread thread;
    private final @NotNull Stack stack;

    private volatile @Nullable Runnable runnable;
    private boolean keepAlive = true;

    public ThreadWithStack(@NotNull ThreadFactory factory, @NotNull StackFactory stackFactory) {
        this.thread = factory.newThread(this);
        this.stack = stackFactory.create();
        this.thread.start();
    }

        public boolean setRunnableIfAvailable(@NotNull Function<Stack, Runnable> runnable) {
        synchronized (lock) {
            if(this.runnable == null) {
                this.runnable = runnable.apply(stack);
                lock.notify();
                return true;
            }

            return false;
        }
    }

    public void shutdown() {
        keepAlive = false;
    }

    public @NotNull Stack getStack() {
        return stack;
    }

    @Override
    public void run() {
        while (!keepAlive) {
            @Nullable Runnable local = null;
            synchronized (lock) {
                try {
                    while (runnable == null && keepAlive) {
                        lock.wait();
                    }

                    local = runnable;

                } catch (InterruptedException ignored) {
                }
            }

            try {
                if (local != null) {
                    local.run();
                    synchronized (lock) {
                        runnable = null;
                    }
                }
            } catch (Throwable throwable) {
            }
        }

    }
}
