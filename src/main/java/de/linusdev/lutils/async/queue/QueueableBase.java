/*
 * Copyright (c) 2022-2024 Linus Andera
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

package de.linusdev.lutils.async.queue;


import de.linusdev.lutils.async.Future;
import de.linusdev.lutils.async.executable.ExecutableFuture;
import de.linusdev.lutils.async.executable.ExecutableTaskBase;
import de.linusdev.lutils.async.manager.AsyncQueue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

/**
 * <p>
 *     {@link Queueable} base.
 * </p><br>
 * <b>Example: </b>
 * <pre>{@code
 * QueueableImpl<String, Nothing> queueable = new QueueableImpl<>(manager) {
 *     @Override
 *     @NotNull
 *     public ComputationResult<String, Nothing> execute()
 *              throws InterruptedException {
 *         //Execute task
 *         Thread.sleep(3000);
 *         return new ComputationResult<>("test", Nothing.INSTANCE, null);
 *     }
 * };
 *
 *
 * Queueable<String, Nothing> qableToReturn = queueable;
 *
 * //End User:
 * String result = qableToReturn.queue().getResult();
 * System.out.println("Result: " + result);}</pre>
 * @param <T> return type
 * @param <R> response type
 */
public abstract class QueueableBase<T, R extends QResponse> extends ExecutableTaskBase<T, R> implements Queueable<T, R> {

    protected final @NotNull AsyncQueue<R> asyncQueue;

    public QueueableBase(@NotNull AsyncQueue<R> asyncManager) {
        super(asyncManager);
        this.asyncQueue = asyncManager;
    }

    @Override
    public @NotNull Future<T, R> consumeAndQueue(@Nullable Consumer<Future<T, R>> consumer) {
        final QueueableFuture<T, R> future = new QueueableFuture<>(this);
        if(consumer != null) consumer.accept(future);
        getAsyncQueue().queue(future);
        return future;
    }

    @Override
    protected void launch(@NotNull ExecutableFuture<T, R, ExecutableTaskBase<T, R>> future) {
        //Done in consumeAndQueue
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull AsyncQueue<R> getAsyncQueue() {
        return asyncQueue;
    }
}
