/*
 * Copyright (c) 2023-2025 Linus Andera
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

package de.linusdev.lutils.optional.noaction;

import de.linusdev.lutils.interfaces.Converter;
import de.linusdev.lutils.interfaces.ExceptionConverter;
import de.linusdev.lutils.optional.Container;
import de.linusdev.lutils.optional.ExceptionSupplier;
import de.linusdev.lutils.optional.ListContainer;
import org.intellij.lang.annotations.PrintFormat;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;


/**
 * All Operations on a {@link NoActionContainer} will have no effect. This means:
 * </p>
 * <ul>
 *     <li>
 *         {@link #get()} or {@link #getAs()} will throw a {@link NoActionException}.
 *     </li>
 *     <li>
 *         {@link #process(Consumer)} will not execute the given {@link Consumer}.
 *     </li>
 *     <li>
 *         {@link #requireNotNull()} and {@link #requireNotNull(ExceptionSupplier)} will never throw an exception.
 *     </li>
 *     <li>
 *          {@link #requireExists()} and {@link #requireExists(ExceptionSupplier)} will never throw an exception.
 *     </li>
 *     <li>
 *         Calls to {@link #orDefaultIfNull(Object)} will be ignored.
 *     </li>
 *     <li>
 *         Further calls to {@link #ifExists()} or {@link #ifNotNull()} will be ignored.
 *     </li>
 * </ul>
 */
public class NoActionContainer<O> extends Container<O> {
    private final @NotNull Reason reason;

    public enum Reason {
        NON_EXISTENT("Key '%s' does not exist."),
        NULL("The value of key '%s' is null or the key does not exist."),
        ;

        @PrintFormat
        public final @NotNull String message;

        Reason(@PrintFormat @NotNull String message) {
            this.message = message;
        }
    }

    public NoActionContainer(@Nullable Object key, @NotNull Reason reason) {
        super(key, false, null);
        this.reason = reason;
    }

    @Override
    public @NotNull <N> Container<N> createNewContainer(@Nullable N newValue) {
        return new NoActionContainer<>(key, reason);
    }

    @Override
    public <N> ListContainer<N> createNewListContainer(@Nullable List<N> newValue) {
        return new NoActionListContainer<>(key, exists, reason);
    }

    @Override
    public O get() {
        throw new NoActionException(key, reason);
    }

    @Override
    public @NotNull <E extends Throwable> Container<O> requireNotNull(@NotNull ExceptionSupplier<E> supplier) {
        return this;
    }

    @Override
    public @NotNull Container<O> requireNotNull() throws NullPointerException {
        return this;
    }

    @Override
    public @NotNull Container<O> requireExists() {
        return this;
    }

    @Override
    public @NotNull <E extends Throwable> Container<O> requireExists(@NotNull ExceptionSupplier<E> supplier) {
        return this;
    }

    @Override
    public @NotNull ListContainer<?> asList() {
        return createNewListContainer(null);
    }

    @Override
    public @NotNull <C, R> Container<R> castAndConvert(@NotNull Converter<C, R> converter) {
        return createNewContainer(null);
    }

    @Override
    public @NotNull <C> Container<C> cast() {
        return createNewContainer(null);
    }

    @Override
    public @NotNull <C, R, E extends Throwable> Container<R> castAndConvertE(@NotNull ExceptionConverter<C, R, E> converter) {
        return createNewContainer(null);
    }

    @Override
    public @NotNull <R> Container<R> convert(@NotNull Converter<O, R> converter) {
        return createNewContainer(null);
    }

    @Override
    public @NotNull <R, E extends Throwable> Container<R> convertE(@NotNull ExceptionConverter<O, R, E> converter) {
        return createNewContainer(null);
    }

    @Override
    public @NotNull Container<O> ifExists() {
        return this;
    }

    @Override
    public @NotNull Container<O> ifNotNull() {
        return this;
    }

    @Override
    public @NotNull Container<O> process(@NotNull Consumer<O> consumer) {
        return this;
    }

    @Override
    public @NotNull Container<O> orDefaultIfNull(O defaultValue) {
        return this;
    }
}
