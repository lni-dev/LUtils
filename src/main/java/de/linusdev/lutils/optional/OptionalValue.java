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

import de.linusdev.lutils.result.Result;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public interface OptionalValue<V> extends Result {

    /**
     *
     * @param value value
     * @return new {@link OptionalValue}, that exists.
     * @param <V> type
     */
    @Contract(value = "_ -> new", pure = true)
    static <V> @NotNull OptionalValue<V> of(V value) {
        return new OptionalValueImplementation<>(value, true);
    }

    /**
     *
     * @return new {@link OptionalValue}, that does not exist.
     * @param <V> type
     */
    @Contract(value = " -> new", pure = true)
    static <V> @NotNull OptionalValue<V> of() {
        return new OptionalValueImplementation<>(null, false);
    }


    /**
     * The actual value of class {@link V} represented by this {@link OptionalValue}.
     * @return {@link V} or {@code null}
     */
    V get();

    /**
     * Calls {@link #get()} and casts it {@link C}.
     * @return {@link C} or {@code null}
     * @param <C> class to cast to
     * @throws ClassCastException if {@link V} cannot be cast to {@link C}.
     */
    @SuppressWarnings("unchecked")
    default <C> C getAs() {
        return (C) get();
    }

    /**
     *
     * @return {@code true} if {@link #get()} will return {@code null}.
     */
    default boolean isNull() {
        return get() == null;
    }

    /**
     * If a {@link OptionalValue} exists, the value returned by {@link #get()} can still be {@code null}.
     * @return {@code true} if this {@link OptionalValue} exists.
     */
    @SuppressWarnings("unused")
    boolean exists();

    @Override
    default int count() {
        return 1;
    }

    @Override
    default Object get(int index) {
        if(index != 0)
            throw new IndexOutOfBoundsException(index);
        return get();
    }

    /**
     * Casts this value to {@link Number} and calls {@link Number#byteValue()}.
     * It should be ensured that {@link #isNull()} is {@code false} before calling this method.
     */
    default byte getAsByte() {
        return this.<Number>getAs().byteValue();
    }

    /**
     * Casts this value to {@link Number} and calls {@link Number#shortValue()}.
     * It should be ensured that {@link #isNull()} is {@code false} before calling this method.
     */
    default short getAsShort() {
        return this.<Number>getAs().shortValue();
    }

    /**
     * Casts this value to {@link Number} and calls {@link Number#intValue()}.
     * It should be ensured that {@link #isNull()} is {@code false} before calling this method.
     */
    default int getAsInt() {
        return this.<Number>getAs().intValue();
    }

    /**
     * Casts this value to {@link Number} and calls {@link Number#longValue()}.
     * It should be ensured that {@link #isNull()} is {@code false} before calling this method.
     */
    default long getAsLong() {
        return this.<Number>getAs().longValue();
    }

    /**
     * Casts this value to {@link Number} and calls {@link Number#floatValue()}.
     * It should be ensured that {@link #isNull()} is {@code false} before calling this method.
     */
    default float getAsFloat() {
        return this.<Number>getAs().floatValue();
    }

    /**
     * Casts this value to {@link Number} and calls {@link Number#doubleValue()} ()}.
     * It should be ensured that {@link #isNull()} is {@code false} before calling this method.
     */
    default double getAsDouble() {
        return this.<Number>getAs().doubleValue();
    }

    /**
     * If {@link #isNull()} is {@code true}, it will return {@code null}. Otherwise, it returns {@link #getAsByte()}.
     */
    default @Nullable Byte getAsByteW() {
        return isNull() ? null : getAsByte();
    }

    /**
     * If {@link #isNull()} is {@code true}, it will return {@code null}. Otherwise, it returns {@link #getAsShort()}.
     */
    default @Nullable Short getAsShortW() {
        return isNull() ? null : getAsShort();
    }

    /**
     * If {@link #isNull()} is {@code true}, it will return {@code null}. Otherwise, it returns {@link #getAsInt()}.
     */
    default @Nullable Integer getAsIntW() {
        return isNull() ? null : getAsInt();
    }

    /**
     * If {@link #isNull()} is {@code true}, it will return {@code null}. Otherwise, it returns {@link #getAsLong()}.
     */
    default @Nullable Long getAsLongW() {
        return isNull() ? null : getAsLong();
    }

    /**
     * If {@link #isNull()} is {@code true}, it will return {@code null}. Otherwise, it returns {@link #getAsFloat()}.
     */
    default @Nullable Float getAsFloatW() {
        return isNull() ? null : getAsFloat();
    }

    /**
     * If {@link #isNull()} is {@code true}, it will return {@code null}. Otherwise, it returns {@link #getAsDouble()}.
     */
    default @Nullable Double getAsDoubleW() {
        return isNull() ? null : getAsDouble();
    }
}
