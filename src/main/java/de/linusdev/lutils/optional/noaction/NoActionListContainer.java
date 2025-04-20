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
import de.linusdev.lutils.optional.ListContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

/**
 * List counterpart to {@link NoActionContainer}.
 */
public class NoActionListContainer<T> extends ListContainer<T> {

    private final @NotNull NoActionContainer.Reason reason;

    public NoActionListContainer(@Nullable Object key, boolean exists, @NotNull NoActionContainer.Reason reason) {
        super(key, exists, null);
        this.reason = reason;
    }

    @Override
    protected @NotNull <N> ListContainer<N> createNew(@Nullable List<N> newValue) {
        return new NoActionListContainer<>(key, exists, reason);
    }

    @Override
    public List<T> get() {
        throw new NoActionException(key, reason);
    }

    @Override
    public @NotNull <C> ListContainer<C> cast() {
        return createNew(null);
    }

    @Override
    public @NotNull <C, R> ListContainer<R> castAndConvert(@NotNull Converter<C, R> converter) {
        return createNew(null);
    }

    @Override
    public @NotNull <C, R, E extends Throwable> ListContainer<R> castAndConvertE(@NotNull ExceptionConverter<C, R, E> converter) {
        return createNew(null);
    }

    @Override
    public @NotNull ListContainer<T> process(@NotNull Consumer<List<T>> consumer) {
        return this;
    }

    @Override
    public @NotNull ListContainer<T> orDefaultIfNull(List<T> defaultValue) {
        return this;
    }
}
