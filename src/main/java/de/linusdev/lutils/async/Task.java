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

import de.linusdev.lutils.async.completeable.CompletableTask;
import de.linusdev.lutils.async.consumer.ResultAndErrorConsumer;
import de.linusdev.lutils.async.consumer.ResultConsumer;
import de.linusdev.lutils.async.consumer.SingleResultConsumer;
import de.linusdev.lutils.async.exception.CannotQueueTaskException;
import de.linusdev.lutils.async.exception.ErrorException;
import de.linusdev.lutils.async.exception.NonBlockingThreadException;
import de.linusdev.lutils.async.executable.ExecutableTaskBase;
import de.linusdev.lutils.async.manager.AsyncManager;
import de.linusdev.lutils.async.manager.HasAsyncManager;
import de.linusdev.lutils.async.queue.QueueableBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.function.Consumer;

/**
 * <p>
 *     A {@link Task} can be executed {@link #queue() asynchronous} or {@link #executeHere() in the current thread}.
 * </p><br>
 * <b>Example {@link #queue() asynchronous} execution:</b>
 * <pre>{@code
 * Task<String, Nothing> task = ...;
 *
 * //Queue and create a listener.
 * //This creates a Future.
 * task.queue((result, response, error) -> {
 *             if(error != null) {
 *                 System.out.println("could not execute task.");
 *                 //Handle error
 *                 return;
 *             }
 *
 *             //Process result...
 *             System.out.println("Result: " + result);
 *         });
 * }</pre>
 * <p>
 *     For Information on how to create your own {@link Task}, see {@link ExecutableTaskBase},
 *     {@link QueueableBase} and {@link CompletableTask}.
 * </p>
 *
 *
 * @param <R> the result type. Nullable.
 * @param <S> the secondary result type. usually contains information about the task's execution process. NotNull.
 * @see Future
 */
@SuppressWarnings("unused")
public interface Task<R, S> extends HasAsyncManager {

    /**
     * @return {@link String} name of the task.
     */
    default @NotNull String getName() {
        return this.getClass().getSimpleName();
    }

    /**
     * Executes this task in the current thread.
     * <br><br>
     * For asynchronous execution see {@link #queue()}.
     * @return {@link ComputationResult} result.
     * @throws NonBlockingThreadException if the current thread is a thread of {@link AsyncManager} and should not be blocked. See {@link AsyncManager#checkThread()}.
     * @throws InterruptedException if the thread got interrupted while executing
     * @throws UnsupportedOperationException if this {@link Task} cannot be executed (It may have to wait on some kind of external event)
     * @throws CannotQueueTaskException if this {@link Task} cannot be executed for some reason (For Example if it was already executed and cannot be executed again)
     * @see #queue()
     */
    @NotNull ComputationResult<R, S>  executeHere() throws InterruptedException, UnsupportedOperationException, CannotQueueTaskException, NonBlockingThreadException;

    /**
     * Queues the {@link Task} for future asynchronous execution. That does not mean, that the {@link Task}
     * actually ends up in a queue, but that the {@link Task} will be executed at any point in time in the future.
     * @return {@link Future}
     * @throws CannotQueueTaskException if this {@link Task} cannot be queued.
     * @see #queue(SingleResultConsumer)
     * @see #queue(ResultConsumer)
     * @see #queue(ResultAndErrorConsumer)
     * @see #queueAndWait()
     * @see #queueAndWriteToFile(Path, boolean, ResultAndErrorConsumer)
     * @see #queueAndSetBeforeExecutionListener(Consumer)
     */
    @NotNull Future<R, S> queue() throws CannotQueueTaskException;

    /**
     *
     * @param consumer {@link Consumer} listener to be called before execution starts.
     * @return {@link Future}
     * @throws CannotQueueTaskException if this {@link Task} cannot be queued.
     * @see #queue()
     */
    @NotNull Future<R, S> queueAndSetBeforeExecutionListener(@NotNull Consumer<Future<R, S>> consumer) throws CannotQueueTaskException;

    /**
     *
     * @param consumer {@link ResultConsumer} listener to be called when the result is ready.
     * @return {@link Future}
     * @throws CannotQueueTaskException if this {@link Task} cannot be queued.
     * @see #queue()
     */
    @NotNull Future<R, S> queue(@NotNull ResultConsumer<R, S> consumer) throws CannotQueueTaskException;

    /**
     *
     * @param consumer {@link SingleResultConsumer} listener to be called when the result is ready.
     * @return {@link Future}
     * @throws CannotQueueTaskException if this {@link Task} cannot be queued.
     * @see #queue()
     */
    @NotNull Future<R, S> queue(@NotNull SingleResultConsumer<R, S> consumer) throws CannotQueueTaskException;

    /**
     *
     * @param consumer {@link ResultAndErrorConsumer} listener to be called when the result is ready.
     * @return {@link Future}
     * @throws CannotQueueTaskException if this {@link Task} cannot be queued.
     * @see #queue()
     */
    @NotNull Future<R, S> queue(@NotNull ResultAndErrorConsumer<R, S> consumer) throws CannotQueueTaskException;

    /**
     * Waits the current Thread until the {@link Future} of this queued {@link Task} has been executed.
     * @return {@link R} result
     * @throws InterruptedException if interrupted while waiting
     * @throws CannotQueueTaskException if this {@link Task} cannot be queued.
     * @throws ErrorException if the {@link Future} returned with an error.
     */
    @NotNull R queueAndWait() throws InterruptedException, ErrorException, CannotQueueTaskException;

    /**
     * What exactly will be written to the file, depends on the implementation.
     *
     * @param file {@link Path} of the file to write to.
     * @param overwriteIfExists if the file should be overwritten if it already exists.
     * @param after {@link ResultAndErrorConsumer} to consume the result after it has been written to the file.
     * @return {@link Future}
     * @throws CannotQueueTaskException if this {@link Task} cannot be queued.
     */
    @NotNull Future<R, S> queueAndWriteToFile(
            @NotNull Path file, boolean overwriteIfExists,
            @Nullable ResultAndErrorConsumer<R, S> after
    ) throws CannotQueueTaskException;
}
