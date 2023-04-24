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

package de.linusdev.lutils.async.error;


import de.linusdev.lutils.async.exception.ErrorException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface AsyncError {

    @Nullable Throwable getThrowable();

    default @NotNull Throwable asThrowable() {
        if(hasThrowable()) //noinspection ConstantConditions
            return getThrowable();
        return new ErrorException(this);
    }

    default boolean hasThrowable() {
        return getThrowable() != null;
    }

    @SuppressWarnings("unused")
    @NotNull ErrorType getType();

    default @NotNull String getMessage() {
        if(!hasThrowable()) return "";
        //noinspection DataFlowIssue: checked above
        return getThrowable().getMessage();
    }

}
