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

package de.linusdev.lutils.async.conditioned;


import de.linusdev.lutils.async.ComputationResult;
import de.linusdev.lutils.async.executable.ExecutableFuture;
import de.linusdev.lutils.async.executable.ExecutableTask;
import de.linusdev.lutils.async.Future;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public class ConditionedFuture<R, S, T extends ConditionedTask<R, S> & ExecutableTask<R, S>> extends ExecutableFuture<R, S, T> {
    public ConditionedFuture(@NotNull T task) {
        super(task);
    }

    @Override
    public boolean isExecutable() {
        assert getTask() != null;//Initialized in constructor as @NotNull
        return getTask().getCondition().check();
    }

    /**
     * Executes the {@link Future} if it has not been {@link #cancel() canceled} and is not {@link #isDone() done} or
     * {@link #started}.
     * <br><br>
     * If the {@link Future} is not {@link #isExecutable() executable}, this function will wait until it is executable.
     * @return {@link ComputationResult result} or {@code null} if this future was {@link #isCanceled() canceled}.
     */
    @Override
    public @Nullable ComputationResult<R, S> executeHere() throws InterruptedException {
        if (!isExecutable()) {
            //waiting may take a while, this should not be done on blocking threads.
            getAsyncManager().checkThread();
            assert getTask() != null; //initialised in constructor as @NotNull
            getTask().getCondition().await();
        }
        return super.executeHere();
    }
}
