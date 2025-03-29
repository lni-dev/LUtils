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

package de.linusdev.lutils.thread.pool;

import de.linusdev.lutils.interfaces.ExceptionHandler;
import de.linusdev.lutils.nat.memory.stack.Stack;
import de.linusdev.lutils.nat.memory.stack.StackFactory;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.ThreadFactory;
import java.util.function.Function;

public class ThreadWithStack implements Runnable {

    private final @NotNull Object lock = new Object();

    private final @NotNull Stack stack;
    private final @NotNull ExceptionHandler exceptionHandler;

    private volatile @Nullable Runnable runnable;
    private long lastTaskEnded = System.currentTimeMillis();
    private boolean keepAlive = true;

    public ThreadWithStack(
            @NotNull ThreadFactory factory,
            @NotNull StackFactory stackFactory,
            @NotNull ExceptionHandler exceptionHandler
    ) {
        this.stack = stackFactory.create();
        this.exceptionHandler = exceptionHandler;

        @NotNull Thread thread = factory.newThread(this);
        thread.start();
    }

    /**
     * Runs the runnable returned by given {@code runnableGetter} if no other runnable is currently running in this thread.
     * @param runnableGetter Function to create the runnable with the stack of this thread.
     * @return {@code true} if runnable will be run. {@code false} if another runnable is already being run by this thread.
     */
    public boolean setRunnableIfAvailable(@NotNull Function<Stack, Runnable> runnableGetter) {
        synchronized (lock) {
            if (this.runnable == null) {
                this.runnable = runnableGetter.apply(stack);
                lock.notify();
                return true;
            }

            return false;
        }
    }

    /**
     * Whether this thread is currently running a task/runnable.
     */
    public boolean isRunningATask() {
        synchronized (lock) {
            return runnable != null;
        }
    }

    /**
     * Shutdown this thread as soon as the current runnable (if any) has finished.
     */
    public void shutdown() {
        keepAlive = false;
    }

    /**
     * Shutdown this thread if it is not currently running a task and the last runnable finished more than {@code duration}
     * milliseconds ago.
     * @param duration max duration between the last task run and the current time.
     * @return {@code true} if this thread was {@link #shutdown()}, {@code false} otherwise.
     */
    public boolean shutdownIfLastRunPast(long duration) {
        synchronized (lock) {
            if (runnable == null && System.currentTimeMillis() - lastTaskEnded > duration) {
                shutdown();
                return true;
            }
        }

        return false;
    }

    /**
     * Run method for threads. Class-Internal.
     */
    @ApiStatus.Internal
    @Override
    public void run() {
        while (keepAlive) {
            @Nullable Runnable local = null;
            synchronized (lock) {
                try {
                    while (runnable == null && keepAlive) {
                        lock.wait();
                    }

                    local = runnable;

                } catch (InterruptedException ignored) {}
            }

            try {
                if (local != null) {
                    local.run();
                    synchronized (lock) {
                        lastTaskEnded = System.currentTimeMillis();
                        runnable = null;
                    }
                }
            } catch (Throwable throwable) {
                exceptionHandler.accept(throwable);
            }
        }

    }
}
