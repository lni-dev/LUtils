/*
 * Copyright (c) 2022-2025 Linus Andera
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

package de.linusdev.lutils.async;

import de.linusdev.lutils.async.consumer.ResultConsumer;
import de.linusdev.lutils.async.exception.CancellationException;
import de.linusdev.lutils.async.manager.AsyncManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

@SuppressWarnings("DataFlowIssue") //ensured with lock
public abstract class AbstractFuture<R, S, T extends Task<R, S>> implements Future<R, S> {

    protected final @NotNull AsyncManager asyncManager;

    protected final @Nullable T task;
    protected volatile boolean canceled = false;
    protected volatile boolean started = false;
    protected volatile boolean done = false;

    protected final Object lock = new Object();

    protected volatile @Nullable Consumer<Future<R, S>> before;
    protected volatile @Nullable ResultConsumer<R, S> then;

    protected volatile @Nullable ComputationResult<R, S> result;

    protected AbstractFuture(@Nullable T task, @NotNull AsyncManager asyncManager) {
        this.asyncManager = asyncManager;
        this.task = task;
    }

    protected @Nullable T getTask() {
        return task;
    }

    @Override
    public @NotNull Future<R, S> cancel() {
        synchronized (lock) {
            canceled = true;
            lock.notifyAll();
        }
        return this;
    }

    @Override
    public boolean isCanceled() {
        return canceled;
    }

    @Override
    public boolean hasStarted() {
        return started;
    }

    @Override
    public boolean isDone() {
        return done;
    }

    @Override
    public @NotNull Future<R, S> beforeExecution(@NotNull Consumer<Future<R, S>> consumer) {
        synchronized (lock) {
            if(before == null) before = consumer;
            else {
                before = before.andThen(consumer);
            }
        }
        return this;
    }

    @Override
    public @NotNull Future<R, S> then(@NotNull ResultConsumer<R, S> consumer) {
        synchronized (lock) {
            if(isDone()) {
                if(result.getResult() != null)
                    consumer.consume(result.getResult(), result.getSecondary());
                else consumer.onError(result.getError(), task, result.getSecondary());
            }
            if(then == null) then = consumer;
            else {
                then = then.thenConsume(consumer);
            }
        }
        return this;
    }

    @Override
    public @NotNull ComputationResult<R, S> get() throws InterruptedException {
        synchronized (lock) {
            if(isCanceled()) throw new CancellationException();
            if(!isDone()) lock.wait();
            if(isCanceled()) throw new CancellationException();
            return result;
        }
    }

    @NotNull
    @Override
    public AsyncManager getAsyncManager() {
        return asyncManager;
    }
}
