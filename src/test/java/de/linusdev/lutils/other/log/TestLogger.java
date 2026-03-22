/*
 * Copyright (c) 2026 Linus Andera
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

public class TestLogger implements LoggerProviderService {

    public static @Nullable String lastErrorMsg = null;

    @Override
    public @NotNull Logger getLogger(@NotNull Class<?> source) {
        return getLogger(source.getSimpleName());
    }

    @Override
    public @NotNull Logger getLogger(@NotNull String source) {
        return new Logger() {
            @Override
            public void debug(@NotNull String message) {
                System.out.println("DEBUG (" + source + "): " + message);
            }

            @Override
            public void info(@NotNull String message) {
                System.out.println("INFO (" + source + "): " + message);
            }

            @Override
            public void warning(@NotNull String message) {
                System.err.println("WARNING (" + source + "): " + message);
            }

            @Override
            public void error(@NotNull String message) {
                lastErrorMsg = message;
                System.err.println("ERROR (" + source + "): " + message);
            }

            @Override
            public void error(@Nullable String message, @NotNull Throwable throwable) {
                lastErrorMsg = message;
                System.err.println("ERROR (" + source + "): " + message);
                throwable.printStackTrace();
            }
        };
    }
}
