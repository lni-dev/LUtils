/*
 * Copyright (c) 2022-2023 Linus Andera all rights reserved
 */

package de.linusdev.lutils.async.consumer;

import de.linusdev.lutils.async.Task;
import de.linusdev.lutils.async.error.AsyncError;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ResultAndErrorConsumer<R, S> extends ResultConsumer<R, S> {
    @Override
    default void onError(@NotNull AsyncError error, @Nullable Task<R, S> task, @NotNull S secondary) {
        consume(null, secondary, error);
    }

    @Override
    default void consume(@NotNull R result, @NotNull S secondary) {
        consume(result, secondary, null);
    }


    /**
     * One of result or error are always {@code null}. <br>
     * If result is not {@code null}, error will be {@code null}.<br>
     * If error is not {@code null}, result will be {@code null}.
     *
     * @param result {@link R} result
     * @param secondary {@link S} secondary result
     * @param error {@link Error}
     */
    void consume(R result, @NotNull S secondary, @Nullable AsyncError error);
}
