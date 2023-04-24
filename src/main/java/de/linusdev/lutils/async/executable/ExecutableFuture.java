/*
 * Copyright (c) 2023 Linus Andera all rights reserved
 */

package de.linusdev.lutils.async.executable;

import de.linusdev.lutils.async.AbstractFuture;
import de.linusdev.lutils.async.ComputationResult;
import de.linusdev.lutils.async.Future;
import de.linusdev.lutils.async.consumer.ResultConsumer;
import de.linusdev.lutils.async.manager.HasAsyncManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class ExecutableFuture<R, S, T extends ExecutableTask<R, S>> extends AbstractFuture<R, S, T> implements HasAsyncManager {

    public ExecutableFuture(@NotNull T task) {
        super(task, task.getAsyncManager());
    }

    /**
     * Checks if this {@link Future} can execute now.
     * @return {@code true} if this future can be executed
     */
    public boolean isExecutable() {
        return true;
    }

    /**
     * Executes the {@link Future} if it has not been {@link #cancel() canceled} and is not {@link #isDone() done} or
     * {@link #started}.
     * <br><br>
     * {@link #isExecutable()} will be ignored by this implementation
     * @return {@link ComputationResult result} or {@code null} if this future was {@link #isCanceled() canceled}.
     */
    public @Nullable ComputationResult<R, S> executeHere() throws InterruptedException {

        synchronized (lock) {
            if (isDone() || hasStarted()) return result;
        }

        try {
            final Consumer<Future<R, S>> before = this.before;
            if(before != null) before.accept(this);
        } catch (Throwable t) {
            getAsyncManager().onExceptionInListener(this, task, t);
        }

        synchronized (lock) {
            if(isCanceled()) {
                lock.notifyAll();
                return null;
            }
            started = true;
        }

        assert task != null; //initialized in constructor as @NotNull
        final @NotNull ComputationResult<R, S> result = task.execute();

        synchronized (lock) {
            this.result = result;
            this.done = true;
            lock.notifyAll();
        }

        try {
            final ResultConsumer<R, S> then = this.then;
            if(then != null) {
                if(result.getResult() != null)
                    then.consume(result.getResult(), result.getSecondary());
                else //noinspection DataFlowIssue: checked above
                    then.onError(result.getError(), task, result.getSecondary());
            }
        } catch (Throwable t) {
            getAsyncManager().onExceptionInListener(this, task, t);
        }

        return result;
    }
}
