/*
 * Copyright (c) 2023 Linus Andera all rights reserved
 */

package de.linusdev.lutils.async.manager;

import de.linusdev.lutils.async.queue.QResponse;
import de.linusdev.lutils.async.queue.QueueableFuture;
import org.jetbrains.annotations.NotNull;

public interface AsyncQueue<R extends QResponse> extends AsyncManager {

    void queue(@NotNull QueueableFuture<?, R> future);

}
