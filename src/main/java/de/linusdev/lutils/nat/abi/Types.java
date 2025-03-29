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

package de.linusdev.lutils.nat.abi;

import de.linusdev.lutils.nat.MemorySizeable;
import org.jetbrains.annotations.NotNull;

public interface Types {

    @NotNull MemorySizeable INT8 = MemorySizeable.of(1);
    @NotNull MemorySizeable INT16 = MemorySizeable.of(2);
    @NotNull MemorySizeable INT32 = MemorySizeable.of(4);
    @NotNull MemorySizeable INT64 = MemorySizeable.of(8);
    @NotNull MemorySizeable FLOAT32 = MemorySizeable.of(4);
    @NotNull MemorySizeable FLOAT64 = MemorySizeable.of(8);

    /**
     * The {@link ABI} to use for operations, which require it.
     */
    @NotNull ABI getAbi();

    /**
     * integer with size depending on the ABI
     */
    @NotNull MemorySizeable integer();

    /**
     * pointer with size depending on the ABI
     */
    @NotNull MemorySizeable pointer();

    default @NotNull MemorySizeable int8() {
        return INT8;
    }

    default @NotNull MemorySizeable int16() {
        return INT16;
    }

    default @NotNull MemorySizeable int32() {
        return INT32;
    }

    default @NotNull MemorySizeable int64() {
        return INT64;
    }

    default @NotNull MemorySizeable float32() {
        return FLOAT32;
    }

    default @NotNull MemorySizeable float64() {
        return FLOAT64;
    }
}
