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

package de.linusdev.lutils.async.exception;

import de.linusdev.lutils.async.error.AsyncError;
import org.jetbrains.annotations.NotNull;

public class ErrorException extends RuntimeException {

    private final @NotNull AsyncError error;

    public ErrorException(@NotNull AsyncError error) {
        super(error.getThrowable());
        this.error = error;
    }

    @Override
    public String getMessage() {
        return error.getMessage();
    }
}
