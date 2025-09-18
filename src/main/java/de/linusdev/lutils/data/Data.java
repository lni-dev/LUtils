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

import de.linusdev.lutils.collections.BiIterable;
import de.linusdev.lutils.data.impl.DataListImpl;
import de.linusdev.lutils.data.impl.DataMapImpl;
import de.linusdev.lutils.data.impl.DataWrapper;
import de.linusdev.lutils.data.impl.EmptyData;
import de.linusdev.lutils.data.json.parser.JsonParser;
import de.linusdev.lutils.optional.OptionalValue;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;

public interface Data extends BiIterable<String, Object>, Datable {

    /* ================================================================================================= *\
    |                                                                                                     |
    |                                            Static Methods                                           |
    |                                                                                                     |
    \* ================================================================================================= */

    @Contract("_ -> new")
    static @NotNull Data ordered(int expectedCapacity) {
        return new DataListImpl(new ArrayList<>(expectedCapacity));
    }

    @Contract(" -> new")
    static @NotNull Data unordered() {
        return new DataMapImpl(new HashMap<>());
    }

    @Contract(" -> new")
    static @NotNull Data empty() {
        return new EmptyData();
    }

    @Contract("_ -> new")
    static @NotNull Data wrap(@Nullable Object object) {
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
     * (for Example {@link DataMapImpl}).
     * </p>
     *
     * @param key key
     * @param value value
     * @return this
     */
    @Contract("_, _ -> this")
    @NotNull Data add(@NotNull String key, @Nullable Object value);

    /**
     * If no entry for given key exists, a new entry is added.<br>
     * If an entry with given key exists, it's value is changed to given value.<br>
     * The implementation of this method might be very slow.
     * @param key key
     * @param value value
     * @return this
     */
    @Contract("_, _ -> this")
    @NotNull Data put(@NotNull String key, @Nullable Object value);

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
    @Nullable Data remove(@NotNull String key);

    /**
     *
     * @return current amount of entries contained in this data instance.
     */
    int size();

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
     * (for Example {@link DataMapImpl}).
     * </p>
     *
     * @param key key
     * @param value value
     * @return this
     */
    @Contract("_, _ -> this")
    default @NotNull Data addIfNotNull(@NotNull String key, @Nullable Object value){
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
     * (for Example {@link DataMapImpl}).
     * </p>
     *
     * @param key key
     * @param optionalValue {@link OptionalValue}
     * @return this
     */
    @Contract("_, _ -> this")
    default @NotNull Data addIfOptionalExists(@NotNull String key, @NotNull OptionalValue<?> optionalValue) {
        if(optionalValue.exists()) {
            add(key, optionalValue.get());
        }

        return this;
    }

    default @NotNull String toJsonString() {
        return JsonParser.DEFAULT_INSTANCE.writeDataToString(this);
    }

    /**
     * How this data should be parsed
     * @return {@link ParseType}
     */
    default @NotNull ParseType parseType() {
        return ParseType.NORMAL;
    }

    @Override
    default @Nullable Data getData() {
        return this;
    }
}
