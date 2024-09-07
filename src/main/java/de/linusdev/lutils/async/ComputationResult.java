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

package de.linusdev.lutils.async;

import de.linusdev.lutils.async.error.AsyncError;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public class ComputationResult<R, S> {

    private final @Nullable R result;
    private final @NotNull S secondary;
    private final @Nullable AsyncError error;

    public ComputationResult(@Nullable R result, @NotNull S secondary, @Nullable AsyncError error) {
        this.result = result;
        this.secondary = secondary;
        this.error = error;
    }

    public boolean hasResult() {
        return result != null;
    }

    @SuppressWarnings("NullableProblems") //Not annotated to avoid compiler warnings when using this method in combination with hasError()
    public R getResult() {
        return result;
    }

    public boolean hasError() {
        return error != null;
    }

    @SuppressWarnings("NullableProblems") //Not annotated to avoid compiler warnings when using this method in combination with hasError()
    public AsyncError getError() {
        return error;
    }

    public @NotNull S getSecondary() {
        return secondary;
    }
}
