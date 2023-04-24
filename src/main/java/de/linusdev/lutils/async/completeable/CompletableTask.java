/*
 * Copyright (c) 2023 Linus Andera all rights reserved
 */

package de.linusdev.lutils.async.completeable;

import de.linusdev.lutils.async.ComputationResult;
import de.linusdev.lutils.async.Future;
import de.linusdev.lutils.async.PTask;
import de.linusdev.lutils.async.manager.AsyncManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class CompletableTask<R, S> implements PTask<R, S> {

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
        return future;
    }

    @Override
    public @NotNull AsyncManager getAsyncManager() {
        return asyncManager;
    }
}
