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

package de.linusdev.lutils.nat;

import de.linusdev.lutils.nat.abi.Types;
import org.jetbrains.annotations.NotNull;

public enum NativeType {

    INTEGER {
        @Override
        public @NotNull MemorySizeable getMemorySizeable(@NotNull Types types) {
            return types.integer();
        }
    },

    INT8 {
        @Override
        @NotNull
        public MemorySizeable getMemorySizeable(@NotNull Types types) {
            return types.int8();
        }
    },
    INT16 {
        @Override
        @NotNull
        public MemorySizeable getMemorySizeable(@NotNull Types types) {
            return types.int16();
        }
    },
    INT32 {
        @Override
        @NotNull
        public MemorySizeable getMemorySizeable(@NotNull Types types) {
            return types.int32();
        }
    },
    INT64 {
        @Override
        @NotNull
        public MemorySizeable getMemorySizeable(@NotNull Types types) {
            return types.int64();
        }
    },
    FLOAT32 {
        @Override
        @NotNull
        public MemorySizeable getMemorySizeable(@NotNull Types types) {
            return types.float32();
        }
    },
    FLOAT64 {
        @Override
        @NotNull
        public MemorySizeable getMemorySizeable(@NotNull Types types) {
            return types.float64();
        }
    },
    POINTER {
        @Override
        @NotNull
        public MemorySizeable getMemorySizeable(@NotNull Types types) {
            return types.pointer();
        }
    };

    public abstract @NotNull MemorySizeable getMemorySizeable(@NotNull Types types);
}
