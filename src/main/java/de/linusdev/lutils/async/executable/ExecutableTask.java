/*
 * Copyright (c) 2022-2023 Linus Andera all rights reserved
 */

package de.linusdev.lutils.async.executable;

import de.linusdev.lutils.async.ComputationResult;
import de.linusdev.lutils.async.Task;
import de.linusdev.lutils.async.exception.NonBlockingThreadException;
import de.linusdev.lutils.async.manager.AsyncManager;
import org.jetbrains.annotations.NotNull;


public interface ExecutableTask<R, S> extends Task<R, S> {

    /**
     * Executes this task in the current thread. Does not call {@link AsyncManager#checkThread()}.
     * @return {@link ComputationResult} result.
     */
    @NotNull ComputationResult<R, S> execute() throws InterruptedException;

    /**
     * Executes this task in the current thread.
     * @return {@link ComputationResult} result.
     * @throws NonBlockingThreadException if the current thread is a thread of {@link AsyncManager} and should not be blocked. See {@link AsyncManager#checkThread()}.
     */
    @NotNull
    @Override
    default ComputationResult<R, S> executeHere() throws NonBlockingThreadException, InterruptedException {
        getAsyncManager().checkThread();
        return execute();
    }

}
