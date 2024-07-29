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
