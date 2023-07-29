/*
 * Copyright (c) 2023 Linus Andera
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

package de.linusdev.lutils.async.completeable;

import de.linusdev.lutils.async.AbstractFuture;
import de.linusdev.lutils.async.ComputationResult;
import de.linusdev.lutils.async.Future;
import de.linusdev.lutils.async.consumer.ResultConsumer;
import de.linusdev.lutils.async.error.AsyncError;
import de.linusdev.lutils.async.manager.AsyncManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

/**
 * A {@link Future} which can be completed with the {@link #complete(Object, Object, AsyncError)} method.
 * This method will then call all listeners ({@link Future#then(ResultConsumer) see}, ...) in the current Thread.
 * <br><br>
 * For the usage in combination with a {@link CompletableTask}, see {@link CompletableTask}.
 * <br><br>
 * <b>Example:</b>
 * <pre>{@code
 * //Create the future
 * var future = CompletableFuture.<String, Nothing>create(asyncManager);
 *
 * new Thread(() -> {
 *     try {
 *         Thread.sleep(3000);
 *         //start the future, if it was not canceled
 *         if(future.startIfNotCanceled()) return;
 *         //Complete the future in a different Thread
 *         future.complete("Hello", Nothing.INSTANCE, null);
 *     } catch (Throwable e) {
 *         future.complete(null, Nothing.INSTANCE,
 *                 new ThrowableAsyncError(e));
 *     }
 * }).start();
 *
 * //End User:
 * Future<String, Nothing> returnedFuture = future;
 * String res = returnedFuture.getResult();
 * System.out.println("Result: " + res);}</pre>
 * @see Future
 */
@SuppressWarnings("unused")
public class CompletableFuture<R, S, T extends CompletableTask<R, S>> extends AbstractFuture<R, S, T> {

    private final boolean supportsBeforeExecutionListener;

    protected CompletableFuture(
            @NotNull AsyncManager asyncManager,
            boolean supportsBeforeExecutionListener
    ) {
        this(null, asyncManager, supportsBeforeExecutionListener);
    }

    public static <R, S> @NotNull CompletableFuture<R, S, CompletableTask<R, S>> create(
            @NotNull AsyncManager asyncManager,
            boolean supportsBeforeExecutionListener
    ) {
        return new CompletableFuture<>(asyncManager, supportsBeforeExecutionListener);
    }

    public CompletableFuture(
            @Nullable T task,
            @NotNull AsyncManager asyncManager,
            boolean supportsBeforeExecutionListener
    ) {
        super(task, asyncManager);
        this.supportsBeforeExecutionListener = supportsBeforeExecutionListener;
    }

    public void complete(@Nullable R result, @NotNull S secondary, @Nullable AsyncError error) {
        if(result == null && error == null)
            throw new IllegalArgumentException("result or error must be not null.");

        synchronized (lock) {
            this.result =  new ComputationResult<>(result, secondary, error);
            this.done = true;

            if(isCanceled())
                return; //If it was canceled return here, so the listeners will not be called.

            lock.notifyAll();
        }

        try {
            final ResultConsumer<R, S> then = this.then;
            if(then != null) {
                if(result != null)
                    then.consume(result, secondary);
                else then.onError(error, task, secondary);
            }
        } catch (Throwable t) {
            getAsyncManager().onExceptionInListener(this, task, t);
        }
    }

    /**
     * Set the future's state to {@link #hasStarted() started} if it was not {@link #isCanceled() canceled} or already
     * {@link #hasStarted() started} before.
     * <br>
     * To {@link #complete(Object, Object, AsyncError)} a future it is not required to call this function before. (but it is
     * recommended)
     * @return {@code true} if the future was {@link #cancel() canceled} or {@link #hasStarted() started}. {@code false}
     * if execution may be started.
     */
    public boolean startIfNotCanceled() {
        synchronized (lock) {
            if(isCanceled() || hasStarted() || isDone())
                return true;
        }

        try {
            final Consumer<Future<R, S>> before = this.before;
            if(before != null) before.accept(this);
        } catch (Throwable t) {
            getAsyncManager().onExceptionInListener(this, task, t);
        }

        synchronized (lock) {
            if (isCanceled() || hasStarted() || isDone())
                return true;

            started = true;
            return false;
        }
    }

    @Override
    public @NotNull Future<R, S> beforeExecution(@NotNull Consumer<Future<R, S>> consumer) {
        if(!supportsBeforeExecutionListener)
            throw new UnsupportedOperationException("Completable future does not support before execution listener.");
        return super.beforeExecution(consumer);
    }

    /**
     *
     * @return {@code true} if this future supports a {@link #beforeExecution(Consumer)} listener.
     */
    public boolean supportsBeforeExecutionListener() {
        return supportsBeforeExecutionListener;
    }
}
