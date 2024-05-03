package de.linusdev.lutils.codegen.java;

import org.jetbrains.annotations.NotNull;

public interface JavaUtils {

    static @NotNull JavaLocalVariable createLocalVariable(
            @NotNull JavaClass type,
            @NotNull String name
    ) {
        return new JavaLocalVariable(null, type, name);
    }

}
