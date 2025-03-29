/*
 * Copyright (c) 2024-2025 Linus Andera
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

package de.linusdev.lutils.other;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NumberUtils {

    /**
     *
     * @param number the number
     * @param classToCastTo the number class, which will be returned.
     * @return given {@code number} as {@code classToCastTo}.
     * @throws UnknownConstantException if {@code classToCastTo} does not extend {@link Number} or
     * is any primitive type except {@code boolean}.
     */
    @Contract("null, _ -> null; !null, _ -> !null")
    public static Number convertTo(@Nullable Number number, @NotNull Class<?> classToCastTo) {
        if(number == null)
            return null;

        if(classToCastTo.equals(Integer.class) || classToCastTo.equals(int.class))
            return number.intValue();

        if(classToCastTo.equals(Long.class) || classToCastTo.equals(long.class))
            return number.longValue();

        if(classToCastTo.equals(Byte.class) || classToCastTo.equals(byte.class))
            return number.byteValue();

        if(classToCastTo.equals(Short.class) || classToCastTo.equals(short.class))
            return number.shortValue();

        if(classToCastTo.equals(Float.class) || classToCastTo.equals(float.class))
            return number.floatValue();

        if(classToCastTo.equals(Double.class) || classToCastTo.equals(double.class))
            return number.doubleValue();

        throw new UnknownConstantException(classToCastTo);
    }

}
