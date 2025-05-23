/*
 * Copyright (c) 2023-2025 Linus Andera
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
import de.linusdev.lutils.async.error.MessageError;
import de.linusdev.lutils.async.error.StandardErrorTypes;
import de.linusdev.lutils.async.error.ThrowableAsyncError;
import de.linusdev.lutils.async.exception.CannotQueueTaskException;
import de.linusdev.lutils.async.exception.ErrorException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * {@link Task} with some methods, that should not be visible to the end user.
 * @see Task
 */
public interface PTask<R, S> extends Task<R, S> {

    /**
     *
     * @param consumer {@link Consumer} to set listeners before the Future is queued.
     * @return {@link Future}
     */
    @NotNull Future<R, S> consumeAndQueue(@Nullable Consumer<Future<R, S>> consumer) throws CannotQueueTaskException;

    @Override
    default @NotNull Future<R, S> queue() {
        return consumeAndQueue(null);
    }

    @Override
    default @NotNull Future<R, S> queueAndSetBeforeExecutionListener(@NotNull Consumer<Future<R, S>> consumer) {
        return consumeAndQueue(future -> future.beforeExecution(consumer));
    }

    @Override
    default @NotNull Future<R, S> queue(@NotNull ResultConsumer<R, S> consumer) {
        return consumeAndQueue(future -> future.then(consumer));
    }

    @Override
    default @NotNull Future<R, S> queue(@NotNull SingleResultConsumer<R, S> consumer) {
        return consumeAndQueue(future -> future.then(consumer));
    }

    @Override
    default @NotNull Future<R, S> queue(@NotNull ResultAndErrorConsumer<R, S> consumer) {
        return consumeAndQueue(future -> future.then(consumer));
    }

    @Override
    default @NotNull R queueAndWait() throws InterruptedException, ErrorException {
        return queue().getResult();
    }

    @Override
    default @NotNull Future<R, S> queueAndWriteToFile(@NotNull Path file, boolean overwriteIfExists,
                                                      @Nullable ResultAndErrorConsumer<R, S> after) {
        return queue((result, secondary, error) -> {

            if(error != null) {
                if(after != null) after.onError(error, this, secondary);
                return;
            }

            if (Files.exists(file)) {
                if (!overwriteIfExists) {
                    if (after != null)
                        after.onError(new MessageError("File " + file + " already exists.", StandardErrorTypes.FILE_ALREADY_EXISTS), this, secondary);
                    return;
                }
            }

            try {
                Files.deleteIfExists(file);
                Files.writeString(file, Objects.toString(result), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
                if (after != null) after.consume(result, secondary);

            } catch (IOException e) {
                if (after != null) after.onError(new ThrowableAsyncError(e), this, secondary);
            }

        });
    }

}
