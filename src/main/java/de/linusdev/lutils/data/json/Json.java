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

import de.linusdev.lutils.optional.Container;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A Json object whose values can be retrieved using the corresponding keys.
 */
public interface Json {

    /**
     * Get the value for given {@code key}
     * @param key the key
     * @return the value
     */
    Object get(@NotNull String key);

    /**
     * Wraps {@link #get(String)} in a container.
     * @param key the key
     * @return {@link Container} wrapping {@link #get(String)}.
     */
    Container<Object> grab(@NotNull String key);

    /**
     * Get the value for given {@code key} and cast it to {@link C}.
     * @param key the key
     * @return value (cast to {@link C}) to given key or {@code null} if the value  is {@code null} or if
     * no such {@code key} exits
     * @param <C> class to which the value should be cast to
     * @throws ClassCastException if the value to given key is not of type {@link C}
     * @see #getAs(String, Object)
     */
    default <C> C getAs(@NotNull String key) {
        //noinspection unchecked
        return (C) get(key);
    }

    /**
     * Get the value for given {@code key} and cast it to {@link C} or return {@code defaultValue} if
     * given {@code key} does not exist.
     * @param key the key
     * @return value (cast to {@link C}) to given key or {@code null} if the value is {@code null}
     * or {@code defaultValue} if no such {@code key} exits.
     * @param <C> class to which the value should be cast to
     * @throws ClassCastException if the value for given key is not of type {@link C}
     * @see #getAs(String)
     */
    default <C> C getAs(@NotNull String key, @Nullable C defaultValue) {
        // TODO
    }

}
