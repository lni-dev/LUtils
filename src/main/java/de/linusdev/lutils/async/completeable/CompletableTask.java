/*
 * Copyright (c) 2023 Linus Andera all rights reserved
 */

package de.linusdev.lutils.async.completeable;

import de.linusdev.lutils.async.ComputationResult;
import de.linusdev.lutils.async.Future;
import de.linusdev.lutils.async.PTask;
import de.linusdev.lutils.async.Task;
import de.linusdev.lutils.async.exception.CannotQueueTaskException;
import de.linusdev.lutils.async.manager.AsyncManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

/**
 * A {@link Task}, which creates a {@link CompletableFuture} when started.
 * <br><br>
 * <b>Example:</b>
 * <pre>{@code
 * CompletableTask<String, Nothing> task = new CompletableTask<>(asyncManager) {
 *     @Override
 *     public void start(
 *             CompletableFuture<String, Nothing,
 *             CompletableTask<String, Nothing>> future
 *     ) {
 *         //Store the future for later completion or start execution...
 *         new Thread(() -> {
 *             try {
 *                 Thread.sleep(3000);
 *                 //complete the future in a different Thread
 *                 future.complete("Hello", Nothing.INSTANCE, null);
 *             } catch (Throwable e) {
 *                 future.complete(null, Nothing.INSTANCE,
 *                         new ThrowableAsyncError(e));
 *             }
 *         }).start();
 *     }
 * };
 *
 * Task<String, Nothing> taskToReturn = task;
 *
 * //End User:
 * Future<String, Nothing> f = taskToReturn.queue();
 * String res = f.getResult();
 * System.out.println("Result: " + res);}</pre>
 * @param <R> result type
 * @param <S> secondary type
 * @see Task
 * @see CompletableFuture
 */
public abstract class CompletableTask<R, S> implements PTask<R, S> {

    private final @NotNull AsyncManager asyncManager;

    public CompletableTask(@NotNull AsyncManager asyncManager) {
        this.asyncManager = asyncManager;
    }

    @Override
    public @NotNull ComputationResult<R, S> executeHere() {
        throw new UnsupportedOperationException("A completable task cannot be executed.");
    }

    @Override
    public @NotNull CompletableFuture<R, S, CompletableTask<R, S>> consumeAndQueue(@Nullable Consumer<Future<R, S>> consumer) {
        CompletableFuture<R, S, CompletableTask<R, S>> future = new CompletableFuture<>(this, asyncManager);
        if(consumer != null) consumer.accept(future);
        start(future);
        return future;
    }

    public abstract void start(@NotNull CompletableFuture<R, S, CompletableTask<R, S>> future) throws CannotQueueTaskException;


    @Override
    public @NotNull AsyncManager getAsyncManager() {
        return asyncManager;
    }
}
