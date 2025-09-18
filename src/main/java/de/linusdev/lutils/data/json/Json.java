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
import de.linusdev.lutils.interfaces.Converter;
import de.linusdev.lutils.interfaces.TConverter;
import de.linusdev.lutils.optional.Container;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.NoSuchElementException;

/**
 * A Json object whose values can be retrieved using the corresponding keys.
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
     * Wraps {@link #get(String)} in a container.
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
    default  <C, R, T extends Throwable> R getAndConvert(@NotNull String key, @NotNull TConverter<C, R, T> converter) throws T {
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
    default <C, R, T extends Throwable> R getAndConvert(@NotNull String key, @NotNull TConverter<C, R, T> converter, @Nullable R defaultValue) throws T {
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
    default <C, R, T extends Throwable> R getAndConvert(
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

}
