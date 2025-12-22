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

import de.linusdev.lutils.id.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ExceptionSupplier<E extends Throwable, K> {

    @NotNull
    ExceptionSupplier<NullPointerException, String> STR_TO_NULL_POINTER_EXCEPTION_SUPPLIER =
            (key) -> new NullPointerException("Value of key '" + key + "' is null.");

    @NotNull
    ExceptionSupplier<NullPointerException, Identifier> ID_TO_NULL_POINTER_EXCEPTION_SUPPLIER =
            (id) -> new NullPointerException("Value of key '" + (id == null ? null : id.getAsString()) + "' is null.");

    @NotNull E supply(@Nullable K key);
}
