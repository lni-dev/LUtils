package de.linusdev.lutils.nat.abi;

import de.linusdev.lutils.nat.MemorySizeable;
import org.jetbrains.annotations.NotNull;

public interface Types {

    /**
     * The {@link ABI} to use for operations, which require it.
     */
    @NotNull ABI getAbi();

    @NotNull MemorySizeable int8();

    @NotNull MemorySizeable int16();

    @NotNull MemorySizeable int32();

    @NotNull MemorySizeable int64();

    @NotNull MemorySizeable float32();

    @NotNull MemorySizeable float64();

    @NotNull MemorySizeable pointer();
}
