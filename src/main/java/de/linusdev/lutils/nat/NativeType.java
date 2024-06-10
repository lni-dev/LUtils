package de.linusdev.lutils.nat;

import de.linusdev.lutils.nat.abi.Types;
import org.jetbrains.annotations.NotNull;

public enum NativeType {

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
