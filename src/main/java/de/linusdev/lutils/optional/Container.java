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
import de.linusdev.lutils.interfaces.ExceptionConverter;
import de.linusdev.lutils.optional.impl.BasicContainer;
import de.linusdev.lutils.optional.noaction.NoActionContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

/**
 * This class holds a value possibly from some kind of collection (called parent collection) where it is accessible through {@link #key()}. It
 * offers several methods to process this value. All these methods will not have any effect on the parent collection.
 * The value can be accessed using the {@link #get()} method and processed with the {@link #process(Consumer)} method.
 * <br><br>
 * <h2>Code examples</h2>
 * The following code will throw an exception if the parent collection {@code data} did not contain a key "some_key"
 * or if its value was {@code null}. Otherwise, it will just print the value of "some_key":
 * <pre>{@code
 * data.getContainer("some_key")
 *          .requireNotNull()
 *          .process((Object o) -> System.out.println("some_key: " + o));
 * }</pre>
 * <br>
 * The following code will do nothing if the key "some_value" does not exist. It throws an exception if the key does
 * exist, but its value is {@code null}. And it will print the value of "some_key" if this exists and its value is not
 * {@code null}:
 * <pre>{@code
 * data.getContainer("some_key")
 *          .ifExists()
 *          .requireNotNull()
 *          .process((Object o) -> System.out.println("some_key: " + o));
 * }</pre>
 *
 * @param <V> type of the contained object.
 */
@SuppressWarnings("unused")
public abstract class Container<V> implements OptionalValue<V> {

    /**
     * Creates a new {@link BasicContainer} with no key and existent value {@code value}.
     */
    public static <V> @NotNull Container<V> of(@Nullable V value) {
        return new BasicContainer<>(null, true, value);
    }

    /**
     * Creates a new {@link BasicContainer} with no key and a {@link #exists() non-existent} value.
     */
    public static <V> @NotNull Container<V> of() {
        return new BasicContainer<>(null, false, null);
    }

    protected final @Nullable Object key;
    protected final boolean exists;
    protected final @Nullable V value;

    protected Container(@Nullable Object key, boolean exists, @Nullable V value) {
        this.key = key;
        this.exists = exists;
        this.value = value;
    }

    @Override
    public V get() {
        return value;
    }

    @Override
    public boolean exists() {
        return exists;
    }

    /**
     * The key through which {@link #value} is accessible in the parent collection or {@code null} if this {@link Container}
     * does not have a parent collection.
     */
    public @Nullable Object key() {
        return key;
    }

    protected @NotNull RuntimeException requireNotNullException() {
        return new NullPointerException(key() == null ?
                "Value is null." : "Value of key '" + key() + "' is null."
        );
    }

    protected @NotNull RuntimeException requireExistsException() {
        return new IllegalStateException(key() == null ?
                "Value does not exist." : "Value of key '" + key() + "' does not exist."
        );
    }

    /**
     * Creates a new {@link Container} with given value.
     * @param value the new value
     * @return a new {@link  Container}
     * @param <N> the type of the new value
     */
    protected abstract <N> Container<N> createNewContainer(@Nullable N value);

    /**
     * Creates a new {@link ListContainer} with given value.
     * @param list the list value
     * @return a new {@link ListContainer}
     * @param <N> the list-element type
     */
    protected abstract <N> ListContainer<N> createNewListContainer(@Nullable List<N> list);

    /**
     * This method throws an exception (likely a {@link NullPointerException}) given by
     * {@link #requireNotNullException()} if {@link #isNull()} is {@code true}.
     * @return this
     */
    public @NotNull Container<V> requireNotNull() {
        if(isNull()) throw requireNotNullException();
        return this;
    }

    /**
     * This method throws an exception given by given {@code supplier} if {@link #isNull()} is {@code true}.
     * @param supplier {@link ExceptionSupplier} to supply an exception.
     * @return this
     * @param <E> exception thrown if the value is {@code null}.
     * @throws E if the value is {@code null} ({@link #get()} returns {@code null}).
     */
    public <E extends Throwable> @NotNull Container<V> requireNotNull(@NotNull ExceptionSupplier<E> supplier) throws E {
        if(isNull())
            throw supplier.supply(key());
        return this;
    }

    /**
     * This method throws an exception (likely a {@link NullPointerException}) given by
     * {@link #requireNotNullException()} if {@link #exists()} is {@code false}.
     * @return this
     */
    public @NotNull Container<V> requireExists() {
        if(!exists())
            throw requireExistsException();
        return this;
    }

    /**
     * This method throws an exception given by given {@code supplier} if {@link #exists()} is {@code false}.
     * @param supplier {@link ExceptionSupplier} to supply an exception.
     * @return this
     * @param <E> exception thrown if the value does not {@link #exists() exist}.
     * @throws E if the value does not {@link #exists() exist}.
     */
    public <E extends Throwable> @NotNull Container<V> requireExists(
            @NotNull ExceptionSupplier<E> supplier
    ) throws E {
        if(!exists())
            throw supplier.supply(key());
        return this;
    }

    /**
     * Consume {@link #value} by given {@code consumer}.
     * <p>
     *     This method is especially useful in combination with {@link #ifExists()} and {@link #ifNotNull()}.
     * </p>
     * @param consumer to process {@link #value}.
     * @return this
     */
    public @NotNull Container<V> process(@NotNull Consumer<V> consumer) {
        consumer.accept(get());
        return this;
    }

    /**
     * Casts the value to {@link C}.
     * @return a new {@link Container} with the new cast value.
     * @param <C> type to cast to.
     * @throws ClassCastException if the value cannot be cast to {@link C}.
     */
    public <C> @NotNull Container<C> cast() {
        //noinspection unchecked
        return createNewContainer((C) value);
    }

    /**
     * If the value {@link #isNull() is null} a new container with given {@code defaultValue} will be returned.
     * Otherwise, the container itself will be returned.
     * @param defaultValue default value if current value {@link #isNull() is null}.
     * @return this or new container with given {@code defaultValue}.
     */
    public @NotNull Container<V> orDefaultIfNull(V defaultValue) {
        if(isNull())
            return createNewContainer(defaultValue);
        return this;
    }

    /**
     * Casts the value to {@link List}&lt;?&gt; and returns a {@link ListContainer} with this list.
     * @return {@link ListContainer} as specified above.
     * @throws ClassCastException if the value is not a {@link List}&lt;?&gt;.
     */
    public @NotNull ListContainer<?> asList() {
        if(isNull())
            return createNewListContainer(null);
        return createNewListContainer((List<?>)value);
    }

    /**
     * Converts value with given {@code converter}.
     * @param converter the converter to {@link Converter#convert(Object)} from {@link V} to {@link R}.
     * @return a new {@link Container} with the new cast and converted value.
     * @param <R> type to {@link Converter#convert(Object)} to.
     */
    public  <R> @NotNull Container<R> convert(@NotNull Converter<V, R> converter) {
        return createNewContainer(converter.convert(value));
    }

    /**
     * Converts the value with given {@code converter}. The converter can throw an exception.
     * @param converter the converter to {@link ExceptionConverter#convert(Object)} from {@link V} to {@link R}.
     * @return a new {@link Container} with the new cast and converted value.
     * @param <R> type to {@link Converter#convert(Object)} to.
     * @param <E> exception your converter may throw.
     * @throws E if your converter throws this exception
     */
    public  <R, E extends Throwable> @NotNull Container<R> convertE(@NotNull ExceptionConverter<V, R, E> converter) throws E {
        return createNewContainer(converter.convert(get()));
    }

    /**
     * Casts the value to {@link C} and converts it with given {@code converter}.
     * @param converter the converter to {@link Converter#convert(Object)} from {@link C} to {@link R}.
     * @return a new {@link Container} with the new cast and converted value.
     * @param <C> type to cast to.
     * @param <R> type to {@link Converter#convert(Object)} to.
     * @throws ClassCastException if the value is not of type {@link C}.
     */
    @SuppressWarnings("unchecked")
    public  <C, R> @NotNull Container<R> castAndConvert(@NotNull Converter<C, R> converter) {
        return createNewContainer(converter.convert((C) get()));
    }

    /**
     * Casts the value to {@link C} and converts it with given {@code converter}. The converter can throw an exception.
     * @param converter the converter to {@link ExceptionConverter#convert(Object)} from {@link C} to {@link R}.
     * @return a new {@link Container} with the new cast and converted value.
     * @param <C> type to cast to.
     * @param <R> type to {@link Converter#convert(Object)} to.
     * @param <E> exception your converter may throw.
     * @throws E if your converter throws this exception
     */
    @SuppressWarnings("unchecked")
    public  <C, R, E extends Throwable> @NotNull Container<R> castAndConvertE(@NotNull ExceptionConverter<C, R, E> converter) throws E {
        return createNewContainer(converter.convert((C) get()));
    }

    /**
     * <p>
     * If the value does {@link #exists() exist}, the {@link Container} itself (this) is returned.
     * </p>
     * <p>
     * If the value does <b>not</b> {@link #exists() exist}, a new {@link NoActionContainer} will be returned.
     * All Operations on a {@link NoActionContainer} will have no effect. For more information see {@link NoActionContainer}.
     * </p>
     *
     * @return {@link Container} as described above.
     */
    public @NotNull Container<V> ifExists() {
        if(exists()) return this;
        return new NoActionContainer<>(key, NoActionContainer.Reason.NON_EXISTENT);
    }

    /**
     * <p>
     * If the value {@link #isNull() is null}, a new {@link NoActionContainer} will be returned.
     * All Operations on this container will have no effect. This means:
     * </p>
     * <p>
     * If the value does <b>not</b> {@link #exists() exist}, a new {@link NoActionContainer} will be returned.
     * All Operations on a {@link NoActionContainer} will have no effect. For more information see {@link NoActionContainer}.
     * </p>
     * @return {@link Container}
     */
    public @NotNull Container<V> ifNotNull() {
        if(isNull())
            return new NoActionContainer<>(key, NoActionContainer.Reason.NULL);
        return this;
    }
}
