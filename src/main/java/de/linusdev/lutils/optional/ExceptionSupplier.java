package de.linusdev.lutils.optional;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ExceptionSupplier<E extends Throwable> {
    @NotNull E supply(@Nullable Object key);
}
