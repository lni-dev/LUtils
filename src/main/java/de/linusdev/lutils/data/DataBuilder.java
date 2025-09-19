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

package de.linusdev.lutils.data;

import de.linusdev.lutils.data.impl.DataBuilderListImpl;
import de.linusdev.lutils.data.impl.DataBuilderMapImpl;
import de.linusdev.lutils.data.impl.DataWrapper;
import de.linusdev.lutils.data.impl.EmptyData;
import de.linusdev.lutils.optional.OptionalValue;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Builder to build a {@link Data}. There is no actual build method. Instead, the builder itself
 * is also the {@link Data}.
 */
@SuppressWarnings("UnusedReturnValue")
public interface DataBuilder extends Data {

    /* ================================================================================================= *\
    |                                                                                                     |
    |                                            Static Methods                                           |
    |                                                                                                     |
    \* ================================================================================================= */

    /**
     * <p>
     *     This will create a new {@link DataBuilder} which will keep the order in which the elements are added.
     *     This {@link DataBuilder} will be backed by a {@link ArrayList} with given {@code expectedCapacity}.<br>
     *     This is useful for {@link DataBuilder data objects} with known sizes or for {@link DataBuilder data objects} with small
     *     sizes.
     * </p>
     * <p>
     *     The {@link DataBuilder#get(String) get} and {@link DataBuilder#remove(String) remove} methods of the returned
     *     {@link DataBuilder} will be in O(n) (very slow), but the {@link DataBuilder#add(String, Object) add} methods will be in O(1).
     * </p>
     * @param expectedCapacity the initialCapacity of the {@link ArrayList}, which will back the {@link DataBuilder}
     * @return {@link DataBuilder} backed by an {@link ArrayList}.
     */
    @Contract("_ -> new")
    static @NotNull DataBuilder orderedKnownSize(int expectedCapacity) {
        return new DataBuilderListImpl(new ArrayList<>(expectedCapacity));
    }

    /**
     * <p>
     *     This will create a new {@link DataBuilder} which will keep the order the elements are added.
     *     This {@link DataBuilder} will be backed by a backed by a {@link LinkedList}.<br>
     *     This is useful for {@link DataBuilder data objects} with large unknown sizes.
     * </p>
     * <p>
     *     The {@link DataBuilder#get(String) get} and {@link DataBuilder#remove(String) remove} methods of the returned
     *     {@link DataBuilder} will be in O(n) (very slow), but the {@link DataBuilder#add(String, Object) add} methods
     *     will be in O(1).
     * </p>
     * @return {@link DataBuilder} backed by a {@link LinkedList}.
     * @see #orderedKnownSize(int) for very small (less than 10 elemets) you should generrally use the
     * ArrayList implemenation
     * @see #unorderedMapBacked(int) if you intend to use get or remove methods you should use the map implementation
     */
    @Contract(" -> new")
    static @NotNull DataBuilder orderedDynamicSize() {
        return new DataBuilderListImpl(new LinkedList<>());
    }

    /**
     * This will create a new {@link DataBuilder} which is backed by a {@link HashMap}. This should be
     * used if the {@link DataBuilder} will be read from using {@link DataBuilder#get(String) get} or
     * {@link DataBuilder#remove(String) remove}.
     * @param initialCapacity the initialCapacity of the {@link HashMap} which will back the {@link DataBuilder}
     * @return {@link DataBuilder} backed by a {@link HashMap}.
     */
    @Contract("_ -> new")
    static @NotNull DataBuilder unorderedMapBacked(int initialCapacity) {
        return new DataBuilderMapImpl(new HashMap<>(initialCapacity));
    }

    /**
     * Create a {@link DataBuilder} of given {@code map}. The {@link DataBuilder} will have the
     * same content as given map.
     * @return New {@link DataBuilder} backed by given map.
     */
    static @NotNull DataBuilder ofMap(@NotNull Map<String, Object> map) {
        return new DataBuilderMapImpl(map);
    }

    /**
     * Creates an empty {@link DataBuilder}. Adding to an empty {@link DataBuilder} will throw an exception.
     */
    @Contract(" -> new")
    static @NotNull DataBuilder empty() {
        return new EmptyData();
    }

    /**
     * Wrap given {@code object}. This can be used if a value or an
     * object should be parsed directly. See {@link ParseType#CONTENT_ONLY}.
     * @param object object to wrap
     * @return {@link DataWrapper} wrapping given object
     * @see DataWrapper
     */
    @Contract("_ -> new")
    static @NotNull DataBuilder wrap(@Nullable Object object) {
        return new DataWrapper(object);
    }

    /* ================================================================================================= *\
    |                                                                                                     |
    |                                                Methods                                              |
    |                                                                                                     |
    \* ================================================================================================= */

    /**
     * Adds a new entry. <br>
     * <p>
     * This method might not check, if an entry with given key already exits.
     * Depending on the implementation, this might even override existing mappings
     * (for Example {@link DataBuilderMapImpl}).
     * </p>
     *
     * @param key key
     * @param value value
     * @return this
     */
    @Contract("_, _ -> this")
    @NotNull DataBuilder add(@NotNull String key, @Nullable Object value);

    /**
     * If no entry for given key exists, a new entry is added.<br>
     * If an entry with given key exists, it's value is changed to given value.<br>
     * The implementation of this method might be very slow.
     * @param key key
     * @param value value
     * @return this
     */
    @Contract("_, _ -> this")
    @NotNull DataBuilder put(@NotNull String key, @Nullable Object value);

    /**
     * Get the value of the entry with given key. This method is usually not needed and should be
     * avoided as it might be very slow.
     * @param key the key of the entry
     * @return the value of the entry
     */
    Object get(@NotNull String key);

    /**
     * <p>
     *     Removes the entry with given key. If no such entry exists nothing happens.
     *     The implementation of this method might be very slow.
     * </p>
     * @param key key of the entry to remove
     * @return this
     */
    @Contract("_ -> this")
    @Nullable DataBuilder remove(@NotNull String key);

    /* ================================================================================================= *\
    |                                                                                                     |
    |                                           Default Methods                                           |
    |                                                                                                     |
    \* ================================================================================================= */

    /**
     * Adds a new entry if given value is not {@code null}. <br>
     * <p>
     * This method might not check, if an entry with given key already exits.
     * Depending on the implementation, this might even override existing mappings
     * (for Example {@link DataBuilderMapImpl}).
     * </p>
     *
     * @param key key
     * @param value value
     * @return this
     */
    @Contract("_, _ -> this")
    default @NotNull DataBuilder addIfNotNull(@NotNull String key, @Nullable Object value){
        if(value != null){
            add(key, value);
        }
        return this;
    }

    /**
     * Adds a new entry with given {@code key} and {@link OptionalValue#get()} if given {@code optionalValue} {@link OptionalValue#exists() exists}. <br>
     * <p>
     * This method might not check, if an entry with given {@code key} already exits.
     * Depending on the implementation, this might even override existing mappings
     * (for Example {@link DataBuilderMapImpl}).
     * </p>
     *
     * @param key key
     * @param optionalValue {@link OptionalValue}
     * @return this
     */
    @Contract("_, _ -> this")
    default @NotNull DataBuilder addIfOptionalExists(@NotNull String key, @NotNull OptionalValue<?> optionalValue) {
        if(optionalValue.exists()) {
            add(key, optionalValue.get());
        }

        return this;
    }
}
