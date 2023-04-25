/*
 * Copyright (c) 2022-2023 Linus Andera
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
