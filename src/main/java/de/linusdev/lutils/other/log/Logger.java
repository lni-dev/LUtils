/*
 * Copyright (c) 2025 Linus Andera
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

package de.linusdev.lutils.other.log;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ServiceLoader;

public interface Logger {

    @ApiStatus.Internal
    @NotNull StackWalker STACK_WALKER =  StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);

    static @NotNull Logger getLogger() {
        return getLogger(STACK_WALKER.getCallerClass());
    }

    static @NotNull Logger getLogger(@NotNull Class<?> source) {
        ServiceLoader<LoggerProviderService> serviceLoader = ServiceLoader.load(LoggerProviderService.class);

        return serviceLoader.findFirst()
                .map(loggerProviderService -> loggerProviderService.getLogger(source))
                .orElse(SilentLogger.INSTANCE);
    }

    @SuppressWarnings("unused")
    static @NotNull Logger getLogger(@NotNull String source) {
        ServiceLoader<LoggerProviderService> serviceLoader = ServiceLoader.load(LoggerProviderService.class);

        return serviceLoader.findFirst()
                .map(loggerProviderService -> loggerProviderService.getLogger(source))
                .orElse(SilentLogger.INSTANCE);
    }

    void debug(@NotNull String message);

    void info(@NotNull String message);

    void warning(@NotNull String message);

    void error(@NotNull String message);

    void error(@Nullable String message, @NotNull Throwable throwable);

    default void error(@NotNull Throwable throwable) {
        error(null, throwable);
    }

}
