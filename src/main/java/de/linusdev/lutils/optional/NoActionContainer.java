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

package de.linusdev.lutils.optional;

import org.intellij.lang.annotations.PrintFormat;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

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
    public O get() {
        throw new NoActionException(getKey(), reason);
    }

    @Override
    public boolean isNull() {
        return true;
    }

    @Override
    public boolean exists() {
        return false;
    }

    @Override
    public @NotNull Object key() {
        return key;
    }

    @Override
    public @NotNull <N> Container<N> createNewContainer(@Nullable N newValue) {
        return new NoActionContainer<>(parentData, key, reason);
    }

    @Override
    public @NotNull <T> ListContainer<T> createNewListContainer(@Nullable List<T> newValue) {
        return new NonExistentListContainer<>();
    }

    @Override
    public @NotNull <E extends Throwable> Container<O> requireNotNull(ExceptionSupplier<E> supplier) throws E {
        throw supplier.supply(parentData, key);
    }

    @Override
    public @NotNull Container<O> requireNotNull() throws NullPointerException {
        return this;
    }

    @Override
    public @NotNull ListContainer<Object> asList() {
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
    public @NotNull <C, R, E extends Throwable> Container<R> castAndConvertWithException(@NotNull ExceptionConverter<C, R, E> converter) {
        return createNewContainer(null);
    }

    @Override
    public @NotNull Container<O> ifExists() {
        return this;
    }

    @Override
    public @NotNull Container<O> process(@NotNull Consumer<O> consumer) {
        return this;
    }
}
