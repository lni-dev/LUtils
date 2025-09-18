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

package de.linusdev.lutils.optional;

import de.linusdev.lutils.interfaces.Converter;
import de.linusdev.lutils.interfaces.TConverter;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * This class holds a {@link List} value. It offers
 * several methods to process the elements of this list and functions similar to it's
 * single value counterpart: {@link Container}.
 * @param <V> the {@link #list}'s element type.
 */
@SuppressWarnings("unused")
public abstract class ListContainer<V> implements OptionalValue<List<V>> {

    protected final @Nullable Object key;
    protected final boolean exists;
    protected final @Nullable List<V> list;

    public ListContainer(@Nullable Object key, boolean exists, @Nullable List<V> list) {
        this.list = list;
        this.key = key;
        this.exists = exists;
    }

    /**
     * Creates a new {@link ListContainer container} with given value.
     * @param newValue the value for the new container
     * @return a new {@link ListContainer}.
     * @param <N> the new value type.
     */
    @ApiStatus.Internal
    protected abstract <N> @NotNull ListContainer<N> createNew(@Nullable List<N> newValue);

    /**
     * The key {@link #list} is associated with or {@code null} if it is not associated with any key.
     */
    public @Nullable Object key() {
        return key;
    }

    @Override
    public List<V> get() {
        //noinspection UnnecessaryLocalVariable: Stop static analysers to infer nullability on the return value.
        var l = list;
        return l;
    }

    @Override
    public boolean exists() {
        return exists;
    }

    /**
     * Casts each element of the list to {@link C}.
     * @return a new {@link ListContainer} containing the {@link List} of {@link C}.
     * @param <C> the type to cast to.
     * @throws ClassCastException if any element inside this container's list cannot be cast to {@link C}.
     */
    @SuppressWarnings("unchecked")
    public <C> @NotNull ListContainer<C> cast() {
        if(isNull()) return createNew(null);

        List<V> list = get();
        ArrayList<C> converted = new ArrayList<>(list.size());
        for(V t : list) converted.add((C) t);

        return createNew(converted);
    }

    /**
     * Casts each element of the list to {@link C} and then converts it with given converter.
     * @param converter {@link Container} to convert from {@link C} to {@link R}.
     * @return a new {@link ListContainer} containing the {@link List} of {@link R}.
     * @param <C> type to cast to.
     * @param <R> type to convert to.
     * @throws ClassCastException if any element inside this container's list cannot be cast to {@link C},
     */
    public <C, R> @NotNull ListContainer<R> castAndConvert(@NotNull Converter<C, R> converter) {
        return castAndConvertE(converter);
    }

    /**
     * Casts each element of the list to {@link C} and then converts it with given converter.
     * @param converter {@link Container} to convert from {@link C} to {@link R}.
     * @return a new {@link ListContainer} containing the {@link List} of {@link R}.
     * @param <C> type to cast to.
     * @param <R> type to convert to.
     * @param <E> exception type of your {@link TConverter}.
     * @throws ClassCastException if any element inside this container's list cannot be cast to {@link C}.
     * @throws E if your converter throws this exception.
     */
    @SuppressWarnings("unchecked")
    public  <C, R, E extends Throwable> @NotNull ListContainer<R> castAndConvertE(@NotNull TConverter<C, R, E> converter) throws E {
        if(isNull()) return createNew(null);

        List<V> list = get();
        ArrayList<R> converted = new ArrayList<>(list.size());
        for(V t : list) converted.add(converter.convert((C) t));

        return createNew(converted);
    }

    /**
     * Process this container's list with given {@link Consumer}.
     * @param consumer to consume this list.
     * @return this
     */
    public @NotNull ListContainer<V> process(@NotNull Consumer<List<V>> consumer) {
        consumer.accept(get());
        return this;
    }

    /**
     * If the value {@link #isNull() is null} a new container with given {@code defaultValue} will be returned.
     * Otherwise, the container itself will be returned.
     * @param defaultValue default value if current value {@link #isNull() is null}.
     * @return this or new container with given {@code defaultValue}.
     */
    public @NotNull ListContainer<V> orDefaultIfNull(List<V> defaultValue) {
        if(isNull())
            return createNew(defaultValue);
        return this;
    }

    /**
     * Cast this list to a {@link List}&lt;N&gt;. Unlike {@link #cast()} this will not cast every element but
     * instead the whole list.
     * @return new list container of type {@link N}.
     * @param <N> the new component type
     * @throws ClassCastException if {@link #list} cannot be cast to {@link List}&lt;N&gt;.
     */
    @SuppressWarnings("unchecked")
    public <N> ListContainer<N> as() {
        return createNew((List<N>) list);
    }
}
