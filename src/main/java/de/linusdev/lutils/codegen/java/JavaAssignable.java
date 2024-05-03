package de.linusdev.lutils.codegen.java;

import org.jetbrains.annotations.NotNull;

public interface JavaAssignable extends JavaExpression {

    @NotNull String getName();

    @Override
    default @NotNull String getExprString(@NotNull JavaSourceGeneratorHelper sg) {
        return getName();
    }
}
