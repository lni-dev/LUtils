package de.linusdev.lutils.codegen.java;

import org.jetbrains.annotations.NotNull;

public interface JavaDocable {

    @NotNull JavaDocGenerator setJavaDoc();

    default @NotNull JavaDocGenerator setJavaDoc(@NotNull String text) {
        var gen = setJavaDoc();
        gen.addText(text);
        return gen;
    }

}
