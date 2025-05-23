/*
 * Copyright (c) 2022-2025 Linus Andera
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
