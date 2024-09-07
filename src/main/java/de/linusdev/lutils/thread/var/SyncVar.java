/*
 * Copyright (c) 2023-2024 Linus Andera
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

package de.linusdev.lutils.thread.var;

import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * A variable intended to be used by multiple threads.
 * @param <T> variable type
 */
@SuppressWarnings("unused")
public interface SyncVar<T> {

    static <T> @NotNull SyncVar<T> createSyncVar(T value) {
        return new SyncVarImpl<>(value);
    }

    static <T> @NotNull SyncVar<T> createSyncVar() {
        return new SyncVarImpl<>(null);
    }

    /**
     * Synchronised get.
     * @return value of this variable
     */
    T get();

    /**
     * Execute given {@code consumer} synchronised on this variable.
     * @param consumer {@link Consumer}
     */
    void doSynchronised(@NotNull Consumer<@NotNull SyncVar<T>> consumer);

    <S> S computeSynchronised(@NotNull Function<@NotNull SyncVar<T>, S> computer);

    /**
     * locks on this variable and checks if its value is not {@code null}.
     * If that is the case, given {@code consumer} will be executed, while keeping the lock on the variable.
     * @param consumer to consume the variable
     * @return {@code true} if given {@code consumer} was executed. {@code false} otherwise.
     */
    boolean consumeIfNotNull(@NotNull Consumer<T> consumer);

    /**
     * Sets the variables value if it is {@code null}. thread safe.
     * @param value the value to set
     * @return {@code true} if this variables value was {@code null}
     */
    boolean setIfNull(T value);

    /**
     * Same as {@link #setIfNull(Object)} but with a {@link Supplier}.
     */
    boolean setIfNull(@NotNull Supplier<T> supplier);

    /**
     * Synchronised set.
     * @param value the variables value
     */
    void set(T value);

}
