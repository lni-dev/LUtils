package de.linusdev.lutils.optional;

import de.linusdev.lutils.interfaces.Converter;
import de.linusdev.lutils.interfaces.ExceptionConverter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public abstract class Container<V> implements OptionalValue<V> {

    private final @Nullable Object key;
    private final boolean exists;
    private final @Nullable V value;

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

    protected abstract <N> Container<N> createNewContainer(@Nullable N value);

    protected abstract <N> ListContainer<V> createNewListContainer(@Nullable List<N> list);

    public @NotNull Container<V> requireNotNull() {
        if(isNull()) throw requireNotNullException();
        return this;
    }

    public <E extends Throwable> @NotNull Container<V> requireNotNull(
            @NotNull ExceptionSupplier<E> supplier
    ) throws E {
        if(isNull())
            throw supplier.supply(key());
        return this;
    }

    public @NotNull Container<V> requireExists() {
        if(!exists())
            throw requireExistsException();
        return this;
    }

    public <E extends Throwable> @NotNull Container<V> requireExists(
            @NotNull ExceptionSupplier<E> supplier
    ) throws E {
        if(!exists())
            throw supplier.supply(key());
        return this;
    }

    public @NotNull Container<V> process(@NotNull Consumer<V> consumer) {
        consumer.accept(get());
        return this;
    }

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

    public @NotNull ListContainer<?> asList() {
        if(isNull())
            return new ListContainer<>(null, exists);
        return new ListContainer<>((List<?>)value, exists);
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
     * If the value does <b>not</b> {@link #exists() exist}, a new {@link NoActionContainer} will be returned.
     * All Operations on this container will have no effect. This means:
     * <ul>
     *     <li>
     *         {@link #get()} will throw a {@link NoActionException}.
     *     </li>
     *     <li>
     *         {@link #process(Consumer)} will not execute the given {@link Consumer}.
     *     </li>
     *     <li>
     *         {@link #requireNotNull()} or {@link #requireNotNull(ExceptionSupplier)} will never throw an exception.
     *     </li>
     * </ul>
     * <p>
     *     If the value does {@link #exists() exist}, the {@link Container} itself (this) is returned.
     * </p>
     * @return {@link Container}
     */
    public @NotNull Container<V> ifExists() {
        if(exists()) return this;
        return new NoActionContainer<>(key, NoActionContainer.Reason.NON_EXISTENT);
    }

    /**
     * If the value {@link #isNull() is null}, a new {@link NoActionContainer} will be returned.
     * All Operations on this container will have no effect. This means:
     * <ul>
     *     <li>
     *         {@link #get()} will throw a {@link NoActionException}.
     *     </li>
     *     <li>
     *         {@link #process(Consumer)} will not execute the given {@link Consumer}.
     *     </li>
     *     <li>
     *         {@link #requireNotNull()} or {@link #requireNotNull(ExceptionSupplier)} will never throw an exception.
     *     </li>
     * </ul>
     * <p>
     *     If the value does {@link #exists() exist}, the {@link Container} itself (this) is returned.
     * </p>
     * <p>
     *     This method should be mainly used in combination with {@link #process(Consumer)}.
     * </p>
     * @return {@link Container}
     */
    public @NotNull Container<V> ifNotNull() {
        if(isNull())
            return new NoActionContainer<>(key, NoActionContainer.Reason.NULL);
        return this;
    }
}
