/*
 * Copyright (c) 2022-2024 Linus Andera
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

package de.linusdev.lutils.async.queue;

import de.linusdev.lutils.async.executable.ExecutableFuture;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

public class QueueableFuture<T, R extends QResponse> extends ExecutableFuture<T, R, QueueableBase<T, R>> {

    private final long createdMillis;

    public QueueableFuture(@NotNull QueueableBase<T, R> task) {
        super(task);
        createdMillis = System.currentTimeMillis();
    }

    @ApiStatus.Internal
    @Override
    public @NotNull QueueableBase<T, R> getTask() {
        assert super.getTask() != null; //Initialised in constructor as @NotNull
        return super.getTask();
    }

    /**
     *
     * @return creation time of this future (in milliseconds since 01.01.1970)
     */
    @SuppressWarnings("unused")
    public long getCreatedMillis() {
        return createdMillis;
    }
}
