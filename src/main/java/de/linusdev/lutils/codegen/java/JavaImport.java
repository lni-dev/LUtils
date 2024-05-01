package de.linusdev.lutils.codegen.java;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class JavaImport {

    private final @NotNull JavaPackage jPackage;
    private final @NotNull String className;
    private final @Nullable String variable;

    public JavaImport(
            @NotNull JavaPackage jPackage,
            @NotNull String className,
            @Nullable String variable
    ) {
        this.jPackage = jPackage;
        this.className = className;
        this.variable = variable;
    }

    public JavaImport(@NotNull Class<?> clazz, @Nullable String variable) {
        this.variable = variable;
        if(clazz.isAnonymousClass())
            throw new IllegalStateException("Cannot import an anonymous class.");

        if(clazz.isSynthetic())
            throw new IllegalStateException("Cannot import an synthetic class.");

        if(clazz.isPrimitive())
            throw new IllegalStateException("Cannot import an primitive class.");

        if(clazz.isArray())
            throw new IllegalStateException("Cannot import an array class.");

        this.jPackage = new JavaPackage(clazz.getPackage().getName());
        this.className = clazz.getSimpleName();
    }

    public boolean contains(@NotNull JavaImport other) {
        if(!this.jPackage.equals(other.jPackage))
            return false;
        if(!this.className.equals(other.className))
            return false;

        if(Objects.equals(variable, other.variable))
            return true;

        if(variable == null || other.variable == null)
            return false;

        return this.variable.equals(JavaSourceGeneratorHelper.ASTERISK);
    }

    public @NotNull JavaPackage getPackage() {
        return jPackage;
    }

    public @NotNull String getClassName() {
        return className;
    }

    public @Nullable String getVariable() {
        return variable;
    }
}
