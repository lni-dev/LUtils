/*
 * Copyright (c) 2023 Linus Andera all rights reserved
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

public class CompletableFuture<R, S, T extends CompletableTask<R, S>> extends AbstractFuture<R, S, T> {

    public CompletableFuture(@Nullable T task, @NotNull AsyncManager asyncManager) {
        super(task, asyncManager);
    }

    public void complete(@Nullable R result, @NotNull S secondary, @Nullable AsyncError error) {
        if(result == null && error == null)
            throw new IllegalArgumentException("result or error must be not null.");

        synchronized (lock) {
            this.result =  new ComputationResult<>(result, secondary, error);
            this.done = true;
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

    @Override
    public @NotNull Future<R, S> beforeExecution(@NotNull Consumer<Future<R, S>> consumer) {
        throw new UnsupportedOperationException("Completable future does not support before execution listener.");
    }

    @Override
    public @NotNull Future<R, S> cancel() {
        throw new UnsupportedOperationException("Completable future cannot be canceled.");
    }
}
