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

import de.linusdev.lutils.async.Future;
import de.linusdev.lutils.async.Nothing;
import de.linusdev.lutils.async.completeable.CompletableFuture;
import de.linusdev.lutils.async.completeable.CompletableTask;
import de.linusdev.lutils.async.error.ThrowableAsyncError;
import de.linusdev.lutils.async.manager.AsyncManager;
import de.linusdev.lutils.collections.llist.LLinkedList;
import de.linusdev.lutils.interfaces.TFunction;
import de.linusdev.lutils.nat.memory.stack.Stack;
import de.linusdev.lutils.nat.memory.stack.StackFactory;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * Thread pool, where each thread has its own {@link Stack}.
 * @see #execute(TFunction)
 */
public class ThreadWithStackPool implements AutoCloseable {

    /**
     * Minimum number of threads, which always remain active.
     */
    private final int minThreadCount;
    /**
     * Max idle time for inactive threads (if {@link #minThreadCount} is exceeded).
     */
    private final long maxIdleMillis;

    private final @NotNull ThreadFactory threadFactory;
    private final @NotNull StackFactory stackFactory;

    private final @NotNull AsyncManager asyncManager;
    private final @NotNull LLinkedList<ThreadWithStack> threads;

    private final @NotNull ScheduledExecutorService sweeper;
    private boolean closed = false;

    public ThreadWithStackPool(
            int minThreadCount, long maxIdleMillis,
            @NotNull AsyncManager asyncManager,
            @NotNull ThreadFactory threadFactory,
            @NotNull StackFactory stackFactory
    ) {
        this.minThreadCount = minThreadCount;
        this.maxIdleMillis = maxIdleMillis;
        this.asyncManager = asyncManager;
        this.threadFactory = threadFactory;
        this.stackFactory = stackFactory;
        this.threads = new LLinkedList<>();

        for (int i = 0; i < minThreadCount; i++) {
            this.threads.add(new ThreadWithStack(threadFactory, stackFactory, Throwable::printStackTrace));
        }

        this.sweeper = Executors.newSingleThreadScheduledExecutor();
        this.sweeper.scheduleAtFixedRate(this::sweep, maxIdleMillis + 1000L, maxIdleMillis + 1000L, TimeUnit.MILLISECONDS);
    }

    /**
     * This method will remove all threads, that have been idle for more than {@link #maxIdleMillis} if the current
     * thread count is larger than {@link #minThreadCount}. This method is periodically executed.
     */
    private void sweep() {
        if(threads.size() <= minThreadCount)
            return;

        ThreadWithStack[] threads = new ThreadWithStack[this.threads.size()];

        int i = 0;
        int j = threads.length - 1;
        for (ThreadWithStack thread : this.threads) {
            if(thread.isRunningATask()) {
                threads[i++] = thread;
            } else {
                threads[j--] = thread;
            }
        }

        for (j = minThreadCount; j < threads.length; j++) {
            if(threads[j].shutdownIfLastRunPast(maxIdleMillis)) {
                this.threads.remove(threads[j]);
            }
        }
    }

    /**
     * Execute given task on any thread of this pool.
     * @param runnable task
     * @return {@link Future} to wait until the task is completed
     * @param <T> return type.
     */
    public <T> @NotNull Future<T, Nothing> execute(@NotNull TFunction<Stack, T, ?> runnable) {
        if(closed)
            throw new IllegalStateException("Already closed");

        CompletableFuture<T, Nothing, CompletableTask<T, Nothing>> fut = CompletableFuture.create(asyncManager, false);

        Function<Stack, Runnable> fun = (stack) -> () -> {
            try {
                fut.complete(runnable.apply(stack), Nothing.INSTANCE, null);
            } catch (Throwable e) {
                fut.complete(null, Nothing.INSTANCE, new ThrowableAsyncError(e));
            }
        };


        for (ThreadWithStack thread : threads) {
            if(thread.setRunnableIfAvailable(fun)) {
                return fut;
            }
        }

        ThreadWithStack thread = new ThreadWithStack(
                threadFactory,
                stackFactory,
                Throwable::printStackTrace /*Exceptions are caught before anyway*/
        );
        if(!thread.setRunnableIfAvailable(fun))
            throw new Error();
        threads.add(thread);
        return fut;
    }

    public int getCurrentThreadCount() {
        return threads.size();
    }

    /**
     * Whether {@link #close()} has been called.
     */
    public boolean isClosed() {
        return closed;
    }

    /**
     * Closes this thread pool. This pool won't accept any new tasks and all threads will be {@link ThreadWithStack#shutdown() shutdown}.
     */
    @Override
    public void close() {
        closed = true;
        sweeper.shutdown();
        for (ThreadWithStack thread : threads) {
            thread.shutdown();
        }
    }
}
