/*
 * Copyright (c) 2022-2023 Linus Andera all rights reserved
 */

package de.linusdev.lutils.async.consumer;

import de.linusdev.lutils.async.Task;
import de.linusdev.lutils.async.error.AsyncError;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ErrorConsumer <R, S> {

    default void onError(@NotNull AsyncError error, @Nullable Task<R, S> task, @NotNull S secondary) {
        error.asThrowable().printStackTrace();
    }
}
