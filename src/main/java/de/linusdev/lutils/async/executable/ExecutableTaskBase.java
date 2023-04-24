/*
 * Copyright (c) 2023 Linus Andera all rights reserved
 */

package de.linusdev.lutils.async.executable;

import de.linusdev.lutils.async.*;
import de.linusdev.lutils.async.manager.AsyncManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

/**
 * A executable {@link Task}.
 * <br><br>
 * <b>Example:</b>
 * <pre>{@code
 * ExecutableTask<String, Nothing> task = new ExecutableTaskBase<>(manager) {
 *             @Override
 *             protected void launch(@NotNull ExecutableFuture<...> future) {
 *                 new Thread(() -> {
 *                     try {
 *                         future.executeHere();
 *                     } catch (InterruptedException e) {
 *                         throw new RuntimeException(e);
 *                     }
 *                 }).start();
 *             }
 *
 *             @Override
 *             public @NotNull ComputationResult<String, Nothing> execute() throws InterruptedException {
 *                 Thread.sleep(5000);
 *                 return new ComputationResult<>("Woooow", Nothing.INSTANCE, null);
 *             }
 *         };
 * task.queue((result, secondary) -> System.out.println(result));}</pre>
 * @param <R>
 * @param <S>
 */
public abstract class ExecutableTaskBase<R, S> implements ExecutableTask<R, S>, PTask<R, S> {

    private final @NotNull AsyncManager asyncManager;

    public ExecutableTaskBase(@NotNull AsyncManager asyncManager) {
        this.asyncManager = asyncManager;
    }

    @Override
    public @NotNull Future<R, S> consumeAndQueue(@Nullable Consumer<Future<R, S>> consumer) {
        ExecutableFuture<R, S, ExecutableTaskBase<R, S>> future = new ExecutableFuture<>(this);
        if (consumer != null)
            consumer.accept(future);
        launch(future);
        return future;
    }

    /**
     * Launch the future created by this {@link Task}.
     * <br>
     * This means, {@link ExecutableFuture#executeHere()} should be called in a different {@link Thread}.
     * @param future the {@link ExecutableFuture} which should be launched/executed.
     */
    protected abstract void launch(@NotNull ExecutableFuture<R, S, ExecutableTaskBase<R, S>> future);

    @Override
    public @NotNull AsyncManager getAsyncManager() {
        return asyncManager;
    }


}
