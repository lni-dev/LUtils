/*
 * Copyright (c) 2022-2023 Linus Andera all rights reserved
 */

package de.linusdev.lutils.async.consumer;

import org.jetbrains.annotations.NotNull;

public interface SingleResultConsumer<R, S>  extends ErrorConsumer<R, S> {

    void consume(@NotNull R result);

    @SuppressWarnings("unused")
    default @NotNull SingleResultConsumer<R, S> thenConsume(@NotNull SingleResultConsumer<R, S> second) {
        return (result) -> {
            this.consume(result);
            second.consume(result);
        };
    }

}
