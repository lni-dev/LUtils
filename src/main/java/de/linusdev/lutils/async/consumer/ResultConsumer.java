/*
 * Copyright (c) 2022-2023 Linus Andera all rights reserved
 */

package de.linusdev.lutils.async.consumer;

import de.linusdev.lutils.async.Task;
import de.linusdev.lutils.async.error.AsyncError;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ResultConsumer<R, S> extends ErrorConsumer<R, S> {

    void consume(@NotNull R result, @NotNull S secondary);

    default @NotNull ResultConsumer<R, S> thenConsume(@NotNull ResultConsumer<R, S> second) {
        ResultConsumer<R, S> first = this;
        return new ResultConsumer<>() {
            @Override
            public void consume(@NotNull R result, @NotNull S secondary) {
                first.consume(result, secondary);
                second.consume(result, secondary);
            }

            @Override
            public void onError(@NotNull AsyncError error, @Nullable Task<R, S> task, @NotNull S secondary) {
                first.onError(error, task, secondary);
                second.onError(error, task, secondary);
            }
        };
    }

}
