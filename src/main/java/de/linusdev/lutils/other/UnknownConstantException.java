package de.linusdev.lutils.other;

import org.jetbrains.annotations.NotNull;

public class UnknownConstantException extends RuntimeException {

    public UnknownConstantException(@NotNull Object constant) {
        super("Unknown constant with value '" + constant + "'");
    }

}
