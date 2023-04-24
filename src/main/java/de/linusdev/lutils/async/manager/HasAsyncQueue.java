/*
 * Copyright (c) 2023 Linus Andera all rights reserved
 */

package de.linusdev.lutils.async.manager;

import de.linusdev.lutils.async.queue.QResponse;

public interface HasAsyncQueue<R extends QResponse> {

    AsyncQueue<R> getAsyncQueue();
}
