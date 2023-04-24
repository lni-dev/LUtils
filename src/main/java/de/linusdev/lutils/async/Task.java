/*
 * Copyright (c) 2022 Linus Andera
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

import de.linusdev.lutils.async.consumer.ResultAndErrorConsumer;
import de.linusdev.lutils.async.consumer.ResultConsumer;
import de.linusdev.lutils.async.consumer.SingleResultConsumer;
import de.linusdev.lutils.async.exception.ErrorException;
import de.linusdev.lutils.async.manager.AsyncManager;
import de.linusdev.lutils.async.manager.HasAsyncManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.function.Consumer;

/**
 * A {@link Task} can be executed {@link #queue() asynchronous} or {@link #executeHere() in the current thread}.
 *
 * @param <R> the result type
 * @param <S> the secondary result type. usually contains information about the task's execution process.
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
     * @throws RuntimeException if the current thread is a thread of {@link AsyncManager} and should not be blocked. See {@link AsyncManager#checkThread()}.
     * @see #queue()
     */
    @NotNull ComputationResult<R, S>  executeHere() throws InterruptedException;

    /**
     * Queues the {@link Task} for future asynchronous execution. That does not mean, that the {@link Task}
     * actually ends up in a queue, but that the {@link Task} will be executed at any point in time in the future.
     * @return {@link Future}
     * @see #queue(SingleResultConsumer)
     * @see #queue(ResultConsumer)
     * @see #queue(ResultAndErrorConsumer)
     * @see #queueAndWait()
     * @see #queueAndWriteToFile(Path, boolean, ResultAndErrorConsumer)
     * @see #queueAndSetBeforeExecutionListener(Consumer)
     */
    @NotNull Future<R, S> queue();

    /**
     *
     * @param consumer {@link Consumer} listener to be called before execution starts.
     * @return {@link Future}
     * @see #queue()
     */
    @NotNull Future<R, S> queueAndSetBeforeExecutionListener(@NotNull Consumer<Future<R, S>> consumer);

    /**
     *
     * @param consumer {@link ResultConsumer} listener to be called when the result is ready.
     * @return {@link Future}
     * @see #queue()
     */
    @NotNull Future<R, S> queue(@NotNull ResultConsumer<R, S> consumer);

    /**
     *
     * @param consumer {@link SingleResultConsumer} listener to be called when the result is ready.
     * @return {@link Future}
     * @see #queue()
     */
    @NotNull Future<R, S> queue(@NotNull SingleResultConsumer<R, S> consumer);

    /**
     *
     * @param consumer {@link ResultAndErrorConsumer} listener to be called when the result is ready.
     * @return {@link Future}
     * @see #queue()
     */
    @NotNull Future<R, S> queue(@NotNull ResultAndErrorConsumer<R, S> consumer);

    /**
     * Waits the current Thread until the {@link Future} of this queued {@link Task} has been executed.
     * @return {@link R} result
     * @throws InterruptedException if interrupted while waiting
     * @throws ErrorException if the {@link Future} returned with an error.
     */
    @NotNull R queueAndWait() throws InterruptedException, ErrorException;

    /**
     * What exactly will be written to the file, depends on the implementation.
     *
     * @param file {@link Path} of the file to write to.
     * @param overwriteIfExists if the file should be overwritten if it already exists.
     * @param after {@link ResultAndErrorConsumer} to consume the result after it has been written to the file.
     * @return {@link Future}
     */
    @NotNull Future<R, S> queueAndWriteToFile(
            @NotNull Path file, boolean overwriteIfExists,
            @Nullable ResultAndErrorConsumer<R, S> after
    );
}
