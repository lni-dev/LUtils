/*
 * Copyright (c) 2023-2024 Linus Andera
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
            if (isDone() || hasStarted())
                return get();
            if(isCanceled())
                return null;

            started = true;
        }

        try {
            final Consumer<Future<R, S>> before = this.before;
            if(before != null) before.accept(this);
        } catch (Throwable t) {
            getAsyncManager().onExceptionInListener(this, task, t);
        }

        synchronized (lock) {
            if(isCanceled())
                return null;
        }

        assert task != null; //initialized in constructor as @NotNull
        final @NotNull ComputationResult<R, S> result = task.execute();

        synchronized (lock) {
            this.result = result;
            this.done = true;

            if(isCanceled())
                return result; //if it was canceled, return here to avoid calling then listeners.

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
