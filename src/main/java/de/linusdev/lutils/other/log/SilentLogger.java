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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SilentLogger implements Logger {

    public static final @NotNull SilentLogger INSTANCE = new SilentLogger();

    private SilentLogger() { }

    @Override
    public void debug(@NotNull String message) {

    }

    @Override
    public void info(@NotNull String message) {

    }

    @Override
    public void warning(@NotNull String message) {

    }

    @Override
    public void error(@NotNull String message) {

    }

    @Override
    public void error(@Nullable String message, @NotNull Throwable throwable) {

    }
}
