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

package de.linusdev.lutils.data.json;

import de.linusdev.lutils.data.Data;
import de.linusdev.lutils.data.json.parser.JsonParser;
import de.linusdev.lutils.interfaces.Converter;
import de.linusdev.lutils.interfaces.TConsumer;
import de.linusdev.lutils.interfaces.TConverter;
import de.linusdev.lutils.optional.Container;
import de.linusdev.lutils.optional.ExceptionSupplier;
import de.linusdev.lutils.other.NumberUtils;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.RecordComponent;
import java.util.Arrays;
import java.util.NoSuchElementException;

/**
 * A Json object whose values can be retrieved using the corresponding keys.
 * <br><br>
 * To obtain a json instance use {@link JsonParser}.
 * <br><br>
 * It provides many functions to conveniently access the keys and values of this json. None of these methods
 * will modify this json instance:
 * <ul>
 *     <li>{@link #get(String)}</li>
 *     <li>{@link #getAs(String)}</li>
 *     <li>{@link #getAs(String, Object)}</li>
 *     <li>{@link #getAs(String, Object, Object)}</li>
 *     <li>{@link #getAsOrDefault(String, Object)}</li>
 *     <li>{@link #getNumberOrDefault(String, Number)}</li>
 *     <li>{@link #convert(String, TConverter)}</li>
 *     <li>{@link #convert(String, TConverter, Object)}</li>
 *     <li>{@link #convert(String, TConverter, Object, Object)}</li>
 *     <li>{@link #process(String, TConsumer)}</li>
 *     <li>{@link #processIfNotNull(String, TConsumer)}</li>
 *     <li>{@link #processIfExistent(String, TConsumer)}</li>
 *     <li>{@link #requireNotNullAndGetAs(String)}</li>
 *     <li>{@link #requireNotNullAndGetAs(String, ExceptionSupplier)}</li>
 *     <li>{@link #requireNotNullAndConvert(String, TConverter)}</li>
 *     <li>{@link #requireNotNullAndConvert(String, TConverter, ExceptionSupplier)}</li>
 *     <li>{@link #requireNotNullAndProcess(String, TConsumer)}</li>
 *     <li>{@link #requireNotNullAndProcess(String, TConsumer, ExceptionSupplier)}</li>
 * </ul>
 * For more advanced functions and convenient retrieving of lists please see {@link #grab(String)}.
 */
public interface Json extends Data {

    static @NotNull String toString(@NotNull Json json) {
        return json.toJsonString();
    }

     @ApiStatus.Internal
     @NotNull Object NULL = new Object();

    /**
     * Internal get method.
     * @param key the key
     * @return the value associated with key or {@link #NULL} if the value is {@code null}
     * or {@code null} if the key does not {@link #exists(String) exist} in this json.
     */
     @ApiStatus.Internal
     Object _get(@NotNull String key);

    /**
     * Get the value for given {@code key}
     * @param key the key
     * @return the value
     */
    default Object get(@NotNull String key) {
        Object value = _get(key);
        if(value == NULL)
            return null;
        return value;
    }

    /**
     * Wraps {@link #get(String)} in a container. Example usages:
     * <pre>{@code
     * // Get an Identifier
     * Identifier id = json.grab("some key")
     *                     .requireNotNull()
     *                     .castAndConvert(Identifier::ofString)
     *                     .get();
     *
     * // Get a list of json objects
     * List<Json> listOfJsonObjects = json.grab("some other key")
     *                     .requireNotNull()
     *                     .asList()
     *                     .<Json>cast()
     *                     .get();
     * }</pre>
     * @param key the key
     * @return {@link Container} wrapping {@link #get(String)}.
     */
    default @NotNull Container<Object> grab(@NotNull String key) {
        Object value = _get(key);
        if(value == NULL)
            return Container.of(key, null);
        if(value == null)
            return Container.nonExistent(key);
        return Container.of(key, value);
    }

    /**
     * Checks whether given {@code key} exists in this json.
     * <br><br>
     * Note: Even if the key exists, the value for this key can be {@code null.}
     * @param key the key to check
     * @return {@code true} if the key exists. {@code false} otherwise.
     */
    default boolean exists(@NotNull String key) {
        return _get(key) != null;
    }

    /**
     * Checks whether the value of given {@code key} is {@code null}.
     * @param key the key to check the value of
     * @return {@code true} if the value of given {@code key} is {@code null}.
     * @throws NoSuchElementException if given {@code key} does not {@link #exists(String) exist}.
     */
    default boolean isNull(@NotNull String key) {
        Object value = _get(key);
        if(value == null)
            throw new NoSuchElementException("Given key '" + key + "' does not exist.");
        return value == NULL;
    }

    /**
     * Get the value for given {@code key} and cast it to {@link C}.
     * @param key the key
     * @return value (cast to {@link C}) to given key or {@code null} if the value  is {@code null} or if
     * no such {@code key} exits
     * @param <C> class to which the value should be cast to
     * @throws ClassCastException if the value to given key is not of type {@link C}
     * @see #getAs(String, Object)
     * @see #getAs(String, Object, Object) 
     * @see #getAsOrDefault(String, Object) 
     */
    default <C> C getAs(@NotNull String key) {
        //noinspection unchecked
        return (C) get(key);
    }

    /**
     * Get the value for given {@code key} and cast it to {@link C} or return {@code defaultValue} if
     * given {@code key} does not {@link #exists(String) exist}. This is the same as:
     * <pre>{@code
     * if(!exists(key))
     *     return defaultValue;
     * return getAs(key);
     * }</pre>
     * @param key the key
     * @param defaultValue default value if given {@code key} does not {@link #exists(String) exist}.
     * @return value (cast to {@link C}) to given key or {@code null} if the value is {@code null}
     * or {@code defaultValue} if no such {@code key} exits.
     * @param <C> class to which the value should be cast to
     * @throws ClassCastException if the value for given key is not of type {@link C}
     * @see #getAs(String)
     */
    default <C> C getAs(@NotNull String key, @Nullable C defaultValue) {
        Object value = _get(key);
        if(value == NULL)
            return null;
        if(value == null)
            return defaultValue;
        //noinspection unchecked
        return (C) value;
    }

    /**
     * Get the value for given {@code key} and cast it to {@link C} or return {@code defaultValue} if
     * given {@code key} does not {@link #exists(String) exist} or return {@code defaultValueIfNull} if the value for
     * given {@code key} is {@code null}. This is the same as:
     * <pre>{@code
     * if(!exists(key))
     *     return defaultValue;
     * if(isNull(key))
     *     return defaultValueIfNull;
     * return getAs(key);
     * }</pre>
     * @param key the key
     * @param defaultValue default value if given {@code key} does not {@link #exists(String) exist}.
     * @param defaultValueIfNull default value if the value for given {@code key} is {@code null}.
     * @return value (cast to {@link C}) to given key or {@code defaultValueIfNull} if the value is {@code null}
     * or {@code defaultValue} if no such {@code key} exits.
     * @param <C> class to which the value should be cast to
     * @throws ClassCastException if the value for given key is not of type {@link C}
     * @see #getAs(String)
     */
    @Contract("_, !null, !null -> !null")
    default <C> C getAs(@NotNull String key, @Nullable C defaultValue, @Nullable C defaultValueIfNull) {
        Object value = _get(key);
        if(value == NULL)
            return defaultValueIfNull;
        if(value == null)
            return defaultValue;
        //noinspection unchecked
        return (C) value;
    }

    /**
     * Get the value for given {@code key} and cast it to {@link C} or return {@code defaultValue} if
     * given {@code key} does not {@link #exists(String) exist} or the value for given {@code key} is {@code null}.
     * This method is the same as:
     * <pre>{@code
     * if(!exists(key) || isNull(key))
     *     return defaultValue;
     * return getAs(key);
     * }</pre>
     * @param key the key
     * @param defaultValue default value if the value is {@code null} or given {@code key} does not {@link #exists(String) exist}.
     * @return value (cast to {@link C}) to given key or {@code defaultValueIfNull} if the value is {@code null}
     * or {@code defaultValue} if no such {@code key} exits.
     * @param <C> class to which the value should be cast to
     * @throws ClassCastException if the value for given key is not of type {@link C}
     * @see #getAs(String)
     */
    @Contract("_, !null -> !null")
    default <C> C getAsOrDefault(@NotNull String key, @Nullable C defaultValue) {
        Object value = _get(key);
        if(value == NULL || value == null)
            return defaultValue;
        //noinspection unchecked
        return (C) value;
    }

    /**
     * Get the value, cast it to {@link C} and convert it using given {@code converter}. This method is the same as:
     * <pre>{@code return converter.convert(getAs(key));}</pre>
     * @param key the key for the entry of type {@link C}
     * @param converter {@link Converter} to convert from {@link C} to {@link R}
     * @param <C> the convertible type
     * @param <R> the result type
     * @return result {@link R} or {@code null} if given {@code converter} returns {@code null}.
     * @throws ClassCastException if the value returned by {@link #get(String)} with given key is not of type {@link C}.
     * @throws T if given {@code converter} throws this exception.
     */
    default  <C, R, T extends Throwable> R convert(@NotNull String key, @NotNull TConverter<C, R, T> converter) throws T {
        //noinspection unchecked
        C convertible = (C) get(key);
        return converter.convert(convertible);
    }

    /**
     * Get the value, cast it to {@link C} and convert it using given {@code converter} or use default values as
     * described below. This method is the same as:
     * <pre>{@code
     * if(!exists(key))
     *     return defaultValue;
     * return converter.convert(getAs(key));
     * }</pre>
     *
     * @param key the key for the entry of type {@link C}
     * @param converter {@link Converter} to convert from {@link C} to {@link R}
     * @param defaultValue default value if given {@code key} does not {@link #exists(String) exist}.
     * @param <C> the convertible type
     * @param <R> the result type
     * @return {@link TConverter#convert(Object)} of {@link #get(String)} or {@code defaultValue} if given {@code key} does not {@link #exists(String) exist}.
     * @throws ClassCastException if the value returned by {@link #get(String)} with given key is not of type {@link C}.
     * @throws T if given {@code converter} throws this exception.
     */
    default <C, R, T extends Throwable> R convert(@NotNull String key, @NotNull TConverter<C, R, T> converter, @Nullable R defaultValue) throws T {
        Object value = _get(key);

        if(value == NULL)
            return converter.convert(null);
        if(value == null)
            return defaultValue;

        //noinspection unchecked
        return converter.convert((C) value);
    }

    /**
     * Get the value, cast it to {@link C} and convert it using given {@code converter} or use default values as
     * described below. This method is the same as:
     * <pre>{@code
     * if(!exists(key))
     *     return defaultValue;
     * if(isNull(key))
     *     return defaultValueIfNull;
     * return converter.convert(getAs(key));
     * }</pre>
     *
     * @param key the key for the entry of type {@link C}
     * @param converter {@link Converter} to convert from {@link C} to {@link R}
     * @param defaultValue default value if given {@code key} does not {@link #exists(String) exist}.
     * @param defaultValueIfNull default value if the value for given {@code key} is {@code null}.
     * @param <C> the convertible type
     * @param <R> the result type
     * @return {@link TConverter#convert(Object)} of {@link #get(String)}
     * or {@code defaultValue} if given {@code key} does not {@link #exists(String) exist}
     * or {@code defaultValueIfNull} if the value for given {@code key} is {@code null}.
     * @throws ClassCastException if the value returned by {@link #get(String)} with given key is not of type {@link C}.
     * @throws T if given {@code converter} throws this exception.
     */
    default <C, R, T extends Throwable> R convert(
            @NotNull String key,
            @NotNull TConverter<C, R, T> converter,
            @Nullable R defaultValue,
            @Nullable R defaultValueIfNull
    ) throws T {
        Object value = _get(key);

        if(value == NULL)
            return defaultValueIfNull;
        if(value == null)
            return defaultValue;

        //noinspection unchecked
        return converter.convert((C) value);
    }

    /**
     * Get the value, cast it to {@link C} and convert it using given {@code converter} or use default values as
     * described below. This method is the same as:
     * <pre>{@code
     * if(!exists(key) || isNull(key))
     *     return defaultValue;
     * return converter.convert(getAs(key));
     * }</pre>
     * @param key the key for the entry of type {@link C}
     * @param converter {@link Converter} to convert from {@link C} to {@link R}
     * @param defaultValue default value if given {@code key} does not {@link #exists(String) exist} or if the value for given {@code key} is {@code null}.
     * @param <C> the convertible type
     * @param <R> the result type
     * @return {@link TConverter#convert(Object)} of {@link #get(String)}
     * or {@code defaultValue} if given {@code key} does not {@link #exists(String) exist}
     * or if the value for given {@code key} is {@code null}.
     * @throws ClassCastException if the value returned by {@link #get(String)} with given key is not of type {@link C}.
     * @throws T if given {@code converter} throws this exception.
     */
    default <C, R, T extends Throwable> R getAndConvertOrDefault(
            @NotNull String key,
            @NotNull TConverter<C, R, T> converter,
            @Nullable R defaultValue
    ) throws T {
        Object value = _get(key);

        if(value == NULL || value == null)
            return defaultValue;

        //noinspection unchecked
        return converter.convert((C) value);
    }

    /**
     * Get the value for given {@code key} and cast it to {@link Number}. If the value is {@code null} or the
     * key does not {@link #exists(String) exist}.
     * return given {@code defaultValue} instead.
     * @param key the key
     * @param defaultValue the default value if {@link #get(String)} returns {@code null}.
     * @return a {@link Number} as described above.
     * @throws ClassCastException if the value of {@code key} is not a {@link Number}.
     */
    @Contract("_, !null -> !null")
    default Number getNumberOrDefault(@NotNull String key, @Nullable Number defaultValue) {
        Number num = getAs(key);
        return num == null ? defaultValue : num;
    }

    /**
     * Process the value of given {@code key} using given {@code consumer}.
     * @param key the key
     * @param consumer the consumer used to process the value
     * @return this
     * @param <C> type to cast to before processing the value.
     * @throws ClassCastException if the value for given {@code key} is not of type {@link C}.
     * @throws T if given {@code consumer} throws this.
     * @implNote The entry will <b>NOT</b> be removed from this {@link Json}.
     */
    @Contract("_, _ -> this")
    default <C, T extends Throwable> @NotNull Json process(@NotNull String key, @NotNull TConsumer<C, T> consumer) throws T {
        consumer.consume(getAs(key));
        return this;
    }

    /**
     * <p>
     *     If given key {@link #exists(String) exists}, its value will be processed by given consumer
     *     and {@code true} will be returned.<br>
     *     If given key does not exist, {@code false} will be returned.
     * </p>
     * This is the same as:
     * <pre>{@code
     * if(!exists(key))
     *     return false;
     * consumer.consume(getAs(key));
     * return true;
     * }</pre>

     * @param key the key
     * @param consumer consumer to process the value, if given {@code key} exists
     * @param <C> type to cast to
     * @throws ClassCastException if the value for given {@code key} is not of type {@link C}.
     * @throws T if given {@code consumer} throws this.
     * @return {@code true} if given key {@link #exists(String) exists}, {@code false} otherwise.
     * @implNote The entry will <b>NOT</b> be removed from this {@link Json}. <br>
     * The entry's value may still be {@code null}. To assure non-null values use {@link #processIfNotNull(String, TConsumer)}.
     */
    default <C, T extends Throwable> boolean processIfExistent(@NotNull String key, @NotNull TConsumer<C, T> consumer) throws T {
        Object value = _get(key);
        if(value == NULL) {
            consumer.consume(null);
            return true;
        }
        if(value == null)
            return false;

        //noinspection unchecked
        consumer.consume((C) value);
        return true;
    }

    /**
     * <p>
     *     If given key {@link #exists(String) exists} and its value is not {@code null}, the value will be processed by given consumer
     *     and {@code true} will be returned. Otherwise, {@code false} will be returned.
     * </p>
     * This is the same as:
     * <pre>{@code
     * if(!exists(key) || isNull(key))
     *     return false;
     * consumer.consume(getAs(key));
     * return true;
     * }</pre>

     * @param key the key
     * @param consumer consumer to process the value, if given {@code key} exists and the value is not {@code null}
     * @param <C> type to cast to
     * @throws ClassCastException if the value for given {@code key} is not of type {@link C}.
     * @throws T if given {@code consumer} throws this.
     * @return {@code true} if given key {@link #exists(String) exists} and its value is not {@code null}, {@code false} otherwise.
     * @implNote The entry will <b>NOT</b> be removed from this {@link Json}.
     */
    default <C, T extends Throwable> boolean processIfNotNull(@NotNull String key, @NotNull TConsumer<@NotNull C, T> consumer) throws T {
        Object value = _get(key);
        if(value == NULL || value == null)
            return false;

        //noinspection unchecked
        consumer.consume((C) value);
        return true;
    }

    /**
     * Require that given {@code key} {@link #exists(String) exists} and its value is not {@code null}. Then cast the
     * value to {@code C}.
     * @param key the key
     * @param exSupp supplier for exceptions
     * @return {@link #getAs(String)}
     * @param <C> The type to cast the value to
     * @param <T> Your exception type
     * @throws T if given {@code key} does not {@link #exists(String) exist} or if its value {@link #isNull(String) is null}.
     */
    default <C, T extends Throwable> @NotNull C requireNotNullAndGetAs(@NotNull String key, @NotNull ExceptionSupplier<T, String> exSupp) throws T {
        Object value = _get(key);
        if(value == NULL || value == null)
            throw exSupp.supply(key);

        //noinspection unchecked
        return (C) value;
    }

    /**
     * Same as {@link #requireNotNullAndGetAs(String, ExceptionSupplier)} with a {@link NullPointerException} supplier.
     */
    default <C> @NotNull C requireNotNullAndGetAs(@NotNull String key) throws NullPointerException {
        return requireNotNullAndGetAs(key, ExceptionSupplier.STR_TO_NULL_POINTER_EXCEPTION_SUPPLIER);
    }

    /**
     * Require that given {@code key} {@link #exists(String) exists} and its value is not {@code null}. Then cast the
     * value to {@code C} and convert it from {@link C} to {@link R}.
     * @param key the key
     * @param exSupp supplier for exceptions
     * @return {@link #convert(String, TConverter)}
     * @param <C> The type to cast the value to
     * @param <R> Result type of given {@code converter}
     * @param <TC> The exception type given {@code converter} can throw.
     * @param <TR> The exception type to throw if the value would be {@code null}.
     * @throws TC if given {@code converter} throws this.
     * @throws TR if given {@code key} does not {@link #exists(String) exist} or if its value {@link #isNull(String) is null}.
     */
    default <C, R, TC extends Throwable, TR extends Throwable> R requireNotNullAndConvert(
            @NotNull String key,
            @NotNull TConverter<C, R, TC> converter,
            @NotNull ExceptionSupplier<TR, String> exSupp
    ) throws TR, TC {
        Object value = _get(key);
        if(value == NULL || value == null)
            throw exSupp.supply(key);

        //noinspection unchecked
        return converter.convert((C) value);
    }

    /**
     * Same as {@link #requireNotNullAndConvert(String, TConverter, ExceptionSupplier)} with a {@link NullPointerException} supplier.
     */
    default <C, R, TC extends Throwable> R requireNotNullAndConvert(
            @NotNull String key,
            @NotNull TConverter<C, R, TC> converter
    ) throws NullPointerException, TC {
        return requireNotNullAndConvert(key, converter, ExceptionSupplier.STR_TO_NULL_POINTER_EXCEPTION_SUPPLIER);
    }

    /**
     * Require that given {@code key} {@link #exists(String) exists} and its value is not {@code null}. Then cast the
     * value to {@code C} and processes it using given {@code consumer}.
     * @param key the key
     * @param exSupp supplier for exceptions
     * @return this
     * @param <C> The type to cast the value to.
     * @param <TP> The exception type given {@code consumer} can throw.
     * @param <TR> The exception type to throw if the value would be {@code null}.
     * @throws TP if given {@code consumer} throws this.
     * @throws TR if given {@code key} does not {@link #exists(String) exist} or if its value {@link #isNull(String) is null}.
     */
    @Contract("_, _, _ -> this")
    default <C, TP extends Throwable, TR extends Throwable> @NotNull Json requireNotNullAndProcess(
            @NotNull String key,
            @NotNull TConsumer<C, TP> consumer,
            @NotNull ExceptionSupplier<TR, String> exSupp
    ) throws TR, TP {
        Object value = _get(key);
        if(value == NULL || value == null)
            throw exSupp.supply(key);

        //noinspection unchecked
        consumer.consume((C) value);
        return this;
    }

    /**
     * Same as {@link #requireNotNullAndProcess(String, TConsumer, ExceptionSupplier)} with a {@link NullPointerException} supplier.
     */
    @Contract("_, _ -> this")
    default <C, TP extends Throwable> @NotNull Json requireNotNullAndProcess(
            @NotNull String key,
            @NotNull TConsumer<C, TP> consumer
    ) throws NullPointerException, TP {
        return requireNotNullAndProcess(key, consumer, ExceptionSupplier.STR_TO_NULL_POINTER_EXCEPTION_SUPPLIER);
    }

    /**
     * Converts this {@link Json} to given {@code recordClass}. This conversion supports all
     * primitive types, {@link String} and records.
     * @param recordClass the class to the record {@link T}.
     * @return record {@link T} filled by this {@link Json}.
     * @param <T> record class
     */
    default <T extends Record> @NotNull T toRecord(Class<T> recordClass) {
        if(!recordClass.isRecord())
            throw new IllegalArgumentException("Given recordClass '" + recordClass.getCanonicalName() + "' is not a record.");

        RecordComponent[] comps = recordClass.getRecordComponents();
        Object[] params = new Object[comps.length];

        loop: for (var entry : this) {
            for (int i = 0; i < comps.length; i++) {
                if (comps[i].getName().equals(entry.getKey())) {
                    if(comps[i].getType().isRecord() && entry.getValue() != null) {
                        //noinspection unchecked
                        params[i] = ((Json) entry.getValue()).toRecord((Class<? extends Record>) comps[i].getType());
                    } else if (entry.getValue() instanceof Number num) {
                        params[i] = NumberUtils.convertTo(num, comps[i].getType());
                    } else {
                        params[i] = entry.getValue();
                    }
                    continue loop;
                }
            }

        }

        Class<?>[] componentTypes = Arrays.stream(comps).map(RecordComponent::getType).toArray(Class<?>[]::new);
        try {
            return recordClass.getDeclaredConstructor(componentTypes).newInstance(params);
        } catch (NoSuchMethodException | InstantiationException e) {
            // Should never happen
            throw new RuntimeException(e);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
